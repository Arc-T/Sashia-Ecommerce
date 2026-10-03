# AGENTS.md

Guidance for AI agents working in this repository.

## Project

E-commerce **modular monolith** on Spring Boot **4.1.1** / Java **25** / Spring Modulith **2.1.0**.
Persistence is JPA + Hibernate, schema owned by **Liquibase**, DB is PostgreSQL (H2 for tests).

There is **no** frontend in this repo and no `webapp` submodule (removed in `24cb3f9`).

Two top-level source roots under `com.sashia`:

| Root | Contains | Modulith |
|---|---|---|
| `com.sashia` | `EcommerceApplication` (the `@SpringBootApplication`) | application |
| `com.sashia.ecommerce` | the 9 business modules + `pricing` | `@NamedInterface("ecommerce")` |
| `com.sashia.shared` | cross-cutting infra: `config`, `exception`, `monitoring`, `util`, `web`, `common` | `@NamedInterface("shared")` |

Business modules: `billing`, `catalog`, `identity`, `media`, `notification`, `ordering`,
`pricing`, `promotion`, `shop`. (`platform` is a **DB schema only**, not a Java package.)

## Commands

Use the Maven wrapper (`mvnw.cmd` / `./mvnw`). Plain `mvn` is not guaranteed on PATH.

```powershell
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
.\mvnw.cmd test
.\mvnw.cmd test -Dtest=TagControllerTest
.\mvnw.cmd test -Dtest=TagControllerTest#shouldCreateTag
.\mvnw.cmd clean compile
```

Do not pass `-Dspring-boot.run.profiles` via a space — the value is parsed as a
separate goal argument.

## Architecture

### Feature-slice layout

Every feature follows this shape. `catalog/tag` is the canonical example.

```
<module>/<feature>/
├── package-info.java      @NamedInterface("<feature>")   <- REQUIRED, fully qualified
├── <Feature>.java         JPA entity (public API)
├── <Feature>Service.java  service interface (public API)
├── dto/
│   ├── package-info.java  @NamedInterface("dto")
│   └── <X>Request.java / <X>Response.java   records
└── internal/              NOT exposed to other modules
    ├── <Feature>Controller.java
    ├── <Feature>ServiceImpl.java
    ├── <Feature>Repository.java
    ├── <Feature>Mapper.java
    └── <Feature>Specification.java
```

- `package-info.java` is mandatory for every slice and every `dto` package. Annotation is
  written **fully qualified on its own line above the package declaration**, with no `import`:
  ```java
  @org.springframework.modulith.NamedInterface("order")

  package com.sashia.ecommerce.ordering.order;
  ```
  Name it after the simple package name (`"order"`, `"dto"`, `"status"`). Only
  `shared/config`, `shared/monitoring`, `shared/web` use `@ApplicationModule(CLOSED)`.
- **Placement is not uniform** — repositories are public at the feature root for some
  slices and `internal` for others (`catalog/tag` vs `catalog/attribute`). Prefer `internal`
  for new code; nothing enforces this. `README.md` already flags cross-module repository
  access in `product/media` and `user details` as a known violation — do not add more.
- Nested slices are allowed and each needs its own `package-info.java`
  (`ordering/order/status`, `ordering/order/transaction`).

### Entities

- `Long` id + `@GeneratedValue(strategy = GenerationType.IDENTITY)`. No UUIDs.
- `@Table(name = "plural_snake_case", schema = "<module>")` — **schema is mandatory**
  and must match the module's DB schema (`catalog`, `identity`, `ordering`, `shop`,
  `promotion`, `billing`, `media`, `notification`).
- Enums: `@Enumerated(EnumType.STRING)`, column usually named `code` or `type`.
- Timestamps: Hibernate `@CreationTimestamp` / `@UpdateTimestamp`. Spring Data auditing
  (`@CreatedDate`/`@LastModifiedDate`/`@EnableJpaAuditing`) is **not** used.
- Soft delete: `@SoftDelete` or `@SoftDelete(strategy = SoftDeleteType.TIMESTAMP)` +
  `deletedAt`, only where the feature needs it (`User`, `Address`, `Item`, `ItemVariant`,
  `Promotion`). Do not add it elsewhere.
- `@Version` only on contended aggregates (`Reservation`, `Inventory`).
- Money is `BigDecimal` in the domain, `Long` minor units in the payment gateway API layer.
- `FetchType.LAZY` written explicitly; collections initialized inline
  (`new HashSet<>()`, `new LinkedHashSet<>()`).
- No `equals`/`hashCode`, no builders, no explicit constructors, no `final` fields.
  (Consequence: entities in `Set` fields use identity equality. Follow only knowingly.)
- Group fields under banner comments. Asterisk counts vary; match the file you edit:
  `/* ***** TABLE RELATIONS ***** */`, `/* ***** TRANSIENT ***** */`,
  `/* ***** GETTER & SETTERS ***** */`, `/* ***** HELPERS ***** */`.
- Wildcard `import jakarta.persistence.*;` is the norm.

Lookup/reference data uses an entity + sibling `<Name>Code` enum **in the same package**
(`promotion/type/Type.java` + `TypeCode.java`, `Scope`/`ScopeCode`, `TargetType`/`TargetTypeCode`,
`ConditionOperator`/`ConditionOperatorCode`, `DiscountType`/`DiscountTypeCode`).

### Repositories

- Spring Data interface extending `JpaRepository<Entity, Long>`.
- Add `JpaSpecificationExecutor<Entity>` for search. `Specification` classes are
  package-local with a `private` constructor and `public static` predicate factories
  returning lambdas. No Criteria API.
- Fetch graphs use JPQL text blocks with `JOIN FETCH`, not `@EntityGraph`:
  ```java
  @Query("""
  select distinct i from Item i
           left join fetch i.tags
           """)
  ```
- Contended writes use `@Lock(LockModeType.PESSIMISTIC_WRITE)` in the repository.
- No Spring interface projections; `ProductBriefInfoProjection` is a plain record filled by hand.

### Services

- Public interface at the feature root; `@Service` implementation in `internal`.
- `@Transactional(readOnly = true)` on the class, `@Transactional` on each mutating method.
- Constructor injection with `final` fields — prefer an explicit constructor;
  `@RequiredArgsConstructor` is used in newer code only. Lombok is present but used
  selectively (`README.md` claims it was removed — it is still wired in `pom.xml`).
- Reads return `Optional<T>`; mutations return `void`.
- Throw `ResourceNotFoundException`, `BusinessRuleException`, or `InvalidResourceException`
  with an **i18n message key**, never a user-facing sentence:
  ```java
  throw new ResourceNotFoundException("tag.not.found");
  ```
  Keys live in `src/main/resources/i18n/messages.properties`.
- Business logic is allowed in entities as well as services (e.g.
  `DeliveryOption.getQuantity()`).

**Pluggable integrations** follow Template Method + registry — the strongest non-CRUD
convention in the repo (`billing/payment/opg`):
- `XGateway` interface + `AbstractXGateway` with `public final` template methods wrapping
  `protected abstract doX` hooks, structured logging, catch-log-rethrow.
- `XRegistry` injects `List<X>`, indexes into an `EnumMap<TypeEnum, X>`, throws
  `IllegalStateException` on duplicate registration and
  `ResourceNotFoundException("<key>")` on miss.
- Provider config via `@ConfigurationProperties` + `@EnableConfigurationProperties`.
- Adding a provider = adding one `@Component implements XGateway`. Nothing else changes.

### Web layer

```java
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/payments")
class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/initiate")
    @PreAuthorize("hasAuthority('CREATE_PAYMENT') or isAuthenticated()")
    ResponseEntity<PaymentInitiateResult> initiate(@RequestBody @Valid PaymentInitiateRequest request) {
        return ResponseEntity.ok(paymentService.initiate(request));
    }
}
```

- Controllers are **package-private** unless there is a reason not to be.
- `@RequestMapping` on a plural resource; sub-paths kebab-case
  (`/service-offerings`, `/order-charge`).
- `@Valid` on every `@RequestBody` (`ProductController.create` is a known omission — don't copy it).
- Return conventions: `ResponseEntity.ok(dto)`, `ResponseEntity.of(optional)` for single
  reads, `ResponseEntity.created(URI.create("/x/" + id)).build()`, `ResponseEntity.noContent().build()`,
  `Page<T>` + `Pageable` for lists.
- **No response envelope.** `ApiResponse` in `com.sashia.shared.web` is dead code — do not use it.
- `@PreAuthorize` per endpoint with `SCREAMING_SNAKE` authority names (`CREATE_TAG`,
  `READ_PAYMENT`). `hasRole(...)` and `isAuthenticated()` also appear in use.
- Errors are shaped by the package-private `@RestControllerAdvice`
  `com.sashia.shared.exception.GlobalExceptionHandler` → `APIError(error, message, fieldErrors, globalErrors)`.
  Existing controller tests asserting `$.details.fieldErrors` are **stale** — do not treat
  them as the contract.

### DTOs and mapping

- `record`s in `dto/`, named `<X>Request` / `<X>Response`.
- Validation annotations on record components with i18n keys:
  ```java
  public record TagCreateRequest(
          @NotBlank(message = "{tag.name.not.blank}")
          @Size(max = 50, message = "{tag.name.size}")
          String name
  ) { }
  ```
- `@Nullable` / `@NonNull` (JSpecify) applied selectively for optional fields.
- Mappers are **hand-written**. No MapStruct, no `BeanUtils`. New mappers should be
  `final` with a `private` constructor (`OrderMapper` is the instantiable legacy exception).
- Legacy `*DTO`, `*SearchDTO`, and `Dto` names exist (`PaymentDTO`, `OrderSearchDTO`,
  `ItemDeliveryDto`) — legacy only, do not propagate.
- Where a provider DTO and a domain DTO coexist, the provider-facing one is named
  `*ApiRequest` (`InitiatePaymentApiRequest` vs `PaymentInitiateRequest`).

### Enums, logging, style

- One PascalCase enum per file, `UPPER_SNAKE_CASE` constants, placed near its owner.
- Rich enums are welcome (transition tables, nested records) — `OrderStatusType` keeps its
  transitions in an immutable `EnumMap`; `ZarinpalResponse.Data` is a nested record.
- SLF4J with pipe-delimited structured messages:
  ```java
  private final Logger log = LoggerFactory.getLogger(getClass());
  log.info("[{}] verify success | authority={} | durationMs={}", name, authority, elapsed);
  ```
- 4-space indent, same-line braces. Wildcard imports are common and accepted.
- Use `sealed` + records for closed hierarchies (`ItemSearchRequest`, promotion requests,
  `PromotionEffect`).
- TODO markers and some commented-out code exist. **Do not add new commented-out blocks**
  and do not revive superseded ones (e.g. `ItemVariantStatus.java` is a fully commented-out
  entity).

## Database (Liquibase)

`ddl-auto` is `none` — **Liquibase is the only schema authority**. Never hand-write DDL
in entity annotations.

- One schema per module, all created up front in `changes/schema.yaml`.
- `db/changelog/master.yaml` is the **single ordered entry point**, sectioned by banner comments:
  TYPE → SCHEMA → TABLES → DATA.
- Change files live at `db/changelog/changes/<module>/<feature>/` and are named
  `001-create-table.yaml`, `001-insert-data.yaml`, `002-*.yaml`, etc.
- **Adding a change file is not enough — register it in `master.yaml`.** It will silently
  never run otherwise. (Four committed `identity` seed files are currently missing from
  `master.yaml`: `identity/role/001-insert-data.yaml`, `identity/permission/001-insert-data.yaml`,
  `identity/role/permission/001-insert-data.yaml`, `identity/user/group/role/001-insert-data.yaml`.)
- Never write raw vendor types. Reference the shared type properties:
  `${id.type}`, `${price.type}` (`DECIMAL(12,2)`), `${integer_value.type}`,
  `${timestamp_value.type}`, `${uuid.type}` — defined in
  `db/changelog/postgresql-type.yaml` with a matching `h2-type.yml` for tests.
- Constraint naming: `pk_<table>`, `fk_<table>_<ref>`, `idx_<table>_<column>`.
- Every `createTable` declares `schemaName`, `remarks`, and explicit `nullable: false`.
- changeSet ids are epoch-millis: `id: 1767965111937-1`. Author is `Mr.Arc-T`.
- Tests use `src/test/resources/db/changelog/test-master.yml`, which includes `master.yaml`
  first and then a smaller set of `001-insert-data.yaml` fixtures. Schema changes that break
  tests usually need a fixture added there too.

## Configuration & secrets

- Profiles: `dev` (default for local runs), `prod`, plus `src/test/resources/application.yml`.
- `dev` uses lazy initialization, virtual threads, `DriverManagerDataSource` with
  `connection-fetch: lazy`, JPA `ddl-auto: none` + batching, custom Liquibase tracking
  tables, `authority-prefix: ''`.
- Cache: **the `CacheManager` bean in `shared/config/CacheConfiguration.java` is the source of
  truth** (2h `expireAfterWrite`, max 10 000, `recordStats`). The `spring.cache.caffeine.spec`
  block in `application-dev.yaml` is dead config — don't tune it.
- `spring.modulith` is configured only in `src/test/resources/application.yml`
  (`events.jdbc.schema: platform`). Main relies on the Liquibase-created
  `platform.event_publication` table.
- Resource dirs: `i18n/` (message keys), `sms/` (SMS provider), `bank/` (payment gateway),
  `statics/` (served at `/media/**` in dev).

**Secret handling** — these paths are gitignored and may contain local-only credentials:

```
src/main/resources/application-prod.yaml
src/main/resources/db/postgresql.yaml
src/main/resources/bank/zarinpal.yaml
src/main/resources/sms/melipayamak.yml
```

- Never commit real keys. Prefer environment variables / placeholders in new config.
- Note `bank/zarinpal.yaml` and `sms/melipayamak.yml` are gitignored **but already tracked** —
  edits to them will not show in `git status`. Flag this rather than committing around it.
- `uploads/` is gitignored and does not exist yet, though `dev` maps media to `file:uploads/`.

## Testing

12 test classes plus 5 support classes under `src/test/java`. All are Spring integration tests.

- `com.sashia.shared.BaseControllerTest` is the shared base: `@SpringBootTest` with
  autowired `MockMvc`, `ObjectMapper`, `MessageSource`.
- Support classes: `TestWithLocale`, `Language` (locale enum, test-only), `WithSashiaUser`,
  `WithSashiaUserSecurityContextFactory`.
- Class names: `<Class>Test`. Methods: `should<Behaviour>` (e.g. `shouldCreateTag`).
  Test classes are package-private.
- Group endpoints with `@Nested` + `@DisplayName`; cover locales with `@ParameterizedTest`
  over `Language` or BCP-47 tags.
- External/paid integrations are **gated, not silently skipped**:
  `@EnabledIfEnvironmentVariable(named = "ZARINPAL.MERCHANT_ID", matches = ".+")`.
- Boot 4 per-starter test artifacts are used (`starter-webmvc-test`, `starter-data-jpa-test`,
  `starter-liquibase-test`, `starter-restclient-test`, `starter-security-test`,
  `starter-validation-test`, `starter-actuator-test`) — **not** `spring-boot-starter-test`.
  Match this when adding a dependency.
- Note `PlatformTest` only prints the transaction manager; there is **no**
  `ApplicationModules.verify()` anywhere despite `spring-modulith-starter-test` being on the
  classpath. Module boundaries are therefore unverified — be careful when adding cross-module imports.

## Known gaps / do not copy these

- `ApiResponse` (`shared/web`) — dead code, no envelope is used.
- Commented-out entities (`catalog/item/variant/status/ItemVariantStatus.java`).
- `*DTO` / `*SearchDTO` / `Dto` legacy naming.
- `ProductController.create` missing `@Valid`.
- Controller tests asserting `$.details.fieldErrors` (stale vs `GlobalExceptionHandler`).
- `Shop.deletedAt` present without `@SoftDelete`.
- Entities used in `Set` without `equals`/`hashCode`.
- Cross-module repository access (`README.md`: product media, user details).
- `@ConfigurationProperties` classes registered ad hoc — only add one to the
  `@EnableConfigurationProperties` list when it is actually needed.

## Tooling gaps

No Checkstyle, Spotless, PMD, SpotBugs, or JaCoCo. No OpenAPI/Swagger. No ArchUnit.
No CI config (`.github/workflows` absent). Formatting is by hand, so **match the file you
are editing** rather than imposing a new style.

`hibernate-maven-plugin` runs the `enhance` goal with `enableAssociationManagement=true`
at build time — lazy loading outside a transaction depends on it, so a plain
`mvn compile` without that plugin behaves differently from a packaged build.

## Known issues tracked in README.md

The README is a scratch log, not documentation — do not treat it as current. Active items:

- `README.md:1-10` — schema review (deleted_at semantics, `order_display` in addresses,
  active address id on users, validations not yet set).
- `README.md:16` — moving the authorization filter to the base package causes test errors.
- `README.md:18-19` — unique-constraint checks on create/update; cross-module repository
  access violations.
- Discount logic is deferred; `order_remaining_balances` is reserved for multi-payment-method.
- Attribute types are hardcoded in `AttributeRepository` pending dynamic attribute types.