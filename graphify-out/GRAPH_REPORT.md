# Graph Report - Backend  (2026-11-16)

## Corpus Check
- 490 files · ~53,958 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 6 file(s) not represented in the graph (top: .properties 3, (none) 2, .cmd 1)

## Summary
- 2505 nodes · 6783 edges · 185 communities (59 shown, 126 thin omitted)
- Extraction: 91% EXTRACTED · 9% INFERRED · 0% AMBIGUOUS · INFERRED: 579 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- Community 0
- Community 1
- Community 2
- Community 3
- Community 4
- Community 5
- Community 6
- Community 8
- Community 9
- Community 10
- Community 11
- Community 12
- Community 13
- Community 14
- Community 15
- Community 16
- Community 17
- Community 18
- Community 19
- Community 20
- Community 21
- Community 22
- Community 23
- Community 24
- Community 25
- Community 26
- Community 27
- Community 28
- Community 29
- Community 30
- Community 31
- Community 32
- Community 33
- Community 34
- Community 35
- Community 36
- Community 37
- Community 38
- Community 39
- Community 40
- Community 41
- Community 42
- Community 43
- Community 44
- Community 45
- Community 46
- Community 47
- Community 48
- Community 49
- Community 50
- Community 51
- Community 52
- Community 53
- Community 54
- Community 55
- Community 56
- Community 57
- Community 58
- Community 59
- Community 60
- Community 61
- Community 62
- Community 63
- Community 64
- Community 65
- Community 66
- Community 67
- Community 68
- Community 69
- Community 70
- Community 71
- Community 72
- Community 73
- Community 74
- Community 75
- Community 76
- Community 78
- Community 79
- Community 80
- Community 81
- Community 82
- Community 84
- Community 85
- Community 86
- Community 87
- Community 88
- Community 89
- Community 91
- Community 92
- Community 93
- Community 94
- Community 95
- Community 96
- Community 97
- Community 98
- Community 100
- Community 101
- Community 102
- Community 103
- Community 104
- Community 105
- Community 106
- Community 107
- Community 108
- Community 109
- Community 110
- Community 111
- Community 112
- Community 113
- Community 114
- Community 115
- Community 116
- Community 117
- Community 118
- Community 119
- Community 120
- Community 121
- Community 122

## God Nodes (most connected - your core abstractions)
1. `Promotion` - 85 edges
2. `ItemVariant` - 68 edges
3. `DeliveryOption` - 55 edges
4. `Category` - 52 edges
5. `Order` - 50 edges
6. `WithSashiaUser` - 50 edges
7. `OrderDetails` - 46 edges
8. `PromotionContext` - 46 edges
9. `ItemType` - 40 edges
10. `Language` - 39 edges

## Surprising Connections (you probably didn't know these)
- `EcommerceApplication` --references--> `MelipayamakProvider`  [EXTRACTED]
  src/main/java/com/sashia/EcommerceApplication.java → src/main/java/com/sashia/ecommerce/notification/sms/provider/mellipayamak/MelipayamakProvider.java
- `Invoice` --references--> `Order`  [EXTRACTED]
  src/main/java/com/sashia/ecommerce/billing/invoice/Invoice.java → src/main/java/com/sashia/ecommerce/ordering/order/Order.java
- `Invoice` --references--> `PricingDetails`  [EXTRACTED]
  src/main/java/com/sashia/ecommerce/billing/invoice/Invoice.java → src/main/java/com/sashia/ecommerce/ordering/order/PricingDetails.java
- `InvoiceCharge` --references--> `InvoiceChargeType`  [EXTRACTED]
  src/main/java/com/sashia/ecommerce/billing/invoice/InvoiceCharge.java → src/main/java/com/sashia/ecommerce/billing/invoice/InvoiceChargeType.java
- `InvoiceItem` --references--> `ItemType`  [EXTRACTED]
  src/main/java/com/sashia/ecommerce/billing/invoice/InvoiceItem.java → src/main/java/com/sashia/ecommerce/catalog/item/dto/ItemType.java

## Import Cycles
- None detected.

## Communities (185 total, 126 thin omitted)

### Community 0 - "Community 0"
Cohesion: 0.07
Nodes (26): AttributeControllerTest, CreateAttribute, DeleteAttribute, GetAllAttributes, GetAllAttributeTypes, GetAttributeById, UpdateAttribute, CategoryControllerTest (+18 more)

### Community 1 - "Community 1"
Cohesion: 0.12
Nodes (14): CategoryUpdateRequest, ServiceOfferingCreateRequest, TagCreateRequest, LoginRequest, UpdateCategory, CreateServiceOffering, UpdateServiceOffering, CreateTag (+6 more)

### Community 2 - "Community 2"
Cohesion: 0.09
Nodes (12): InitiatePaymentApiRequest, GetPayment, InitiatePayment, PaymentCallback, PaymentControllerTest, VerifyPayment, DraftVisibility, MediaControllerTest (+4 more)

### Community 3 - "Community 3"
Cohesion: 0.06
Nodes (17): Attribute, AttributeService, AttributeCreateRequest, AttributeResponse, AttributeSearchRequest, AttributeType, DECIMAL, MULTISELECT (+9 more)

### Community 4 - "Community 4"
Cohesion: 0.08
Nodes (4): PlatformTest, GetAllPromotions, PromotionControllerTest, BaseControllerTest

### Community 5 - "Community 5"
Cohesion: 0.06
Nodes (14): DeliveryDetails, DeliveryTransaction, DeliveryMapper, DeliveryMethod, DIGITAL, LOCAL_DELIVERY, SERVICE, SHIPPING (+6 more)

### Community 6 - "Community 6"
Cohesion: 0.05
Nodes (9): ItemType, PRODUCT, SERVICE_OFFERING, ItemSearchRequest, Item, ProductSummary, ServiceOffering, TagMapper (+1 more)

### Community 8 - "Community 8"
Cohesion: 0.10
Nodes (10): PaymentLockService, SMSUtils, ConditionEvaluator, ConditionEvaluatorFactory, CategoryConditionEvaluator, ActivationHandler, ConditionHandler, EffectGenerationHandler (+2 more)

### Community 9 - "Community 9"
Cohesion: 0.08
Nodes (13): ItemSummaryResponse, ItemServiceImpl, ProductSearchRequest, ItemMapper, ItemRepository, ItemService, ProductBriefInfoProjection, ProductCreateRequest (+5 more)

### Community 10 - "Community 10"
Cohesion: 0.07
Nodes (10): Priceable, Discount, PromotionCandidates, CartPromotionRequest, DiscountedItemVariant, DiscountEffect, DiscountCalculator, FixedDiscountCalculator (+2 more)

### Community 11 - "Community 11"
Cohesion: 0.08
Nodes (8): AppliedPromotionResponse, ItemPricing, CurrencyCode, IRR, USD, AppliedPromotion, Promotable, PromotionMapper

### Community 12 - "Community 12"
Cohesion: 0.08
Nodes (12): Invoice, InvoiceCharge, InvoiceChargeType, InvoiceItem, AttributeValue, Product, Role, UserGroup (+4 more)

### Community 13 - "Community 13"
Cohesion: 0.06
Nodes (4): BuyXGetY, PromotionResolverImpl, Promotion, PromotionRepository

### Community 14 - "Community 14"
Cohesion: 0.12
Nodes (7): ProductServiceImpl, TagServiceImpl, DeliveryServiceImpl, DeliveryOptionRepository, OrderServiceImpl, PromotionEngine, ResourceNotFoundException

### Community 15 - "Community 15"
Cohesion: 0.09
Nodes (10): PromotableType, ITEM_VARIANT, SHIPPING, USER, PromotionContext, BuyXGetYEffect, FreeShippingEffect, PromotionEffect (+2 more)

### Community 16 - "Community 16"
Cohesion: 0.10
Nodes (9): MediaResponse, MediaSyncRequest, MediaUpdateRequest, MediaUploadRequest, MediaController, MediaResourceType, PRODUCT, SERVICE (+1 more)

### Community 17 - "Community 17"
Cohesion: 0.10
Nodes (7): CategoryRepository, CategoryService, CategoryCreateRequest, CategoryResponse, CategorySearchRequest, CategoryController, CategoryServiceImpl

### Community 18 - "Community 18"
Cohesion: 0.07
Nodes (12): Condition, ConditionMapper, ConditionDTO, ConditionTypeCode, CATEGORY_ID, COUPON, CUSTOMER_SEGMENT, DELIVERY_METHOD (+4 more)

### Community 19 - "Community 19"
Cohesion: 0.06
Nodes (7): ConditionType, Scope, ScopeCode, CART, PRODUCT, SERVICE_OFFERING, USER

### Community 20 - "Community 20"
Cohesion: 0.11
Nodes (7): ServiceOfferingSearchRequest, ServiceOfferingResponse, ServiceUpdateDTO, ServiceOfferingController, ServiceOfferingServiceImpl, ServiceOfferingRepository, ServiceOfferingService

### Community 21 - "Community 21"
Cohesion: 0.12
Nodes (5): CheckoutRequest, OrderDTO, OrderSearchDTO, OrderController, OrderService

### Community 23 - "Community 23"
Cohesion: 0.07
Nodes (8): PromotionStrategy, Type, TypeCode, BUY_X_GET_Y, COUPON, DISCOUNT, GIFT, SHIPPING

### Community 26 - "Community 26"
Cohesion: 0.10
Nodes (4): MediaContent, LocalMediaStorage, MediaStorage, StoredFile

### Community 27 - "Community 27"
Cohesion: 0.16
Nodes (6): AbstractPaymentGateway, PaymentInitiateRequest, PaymentInitiateResult, PaymentVerifyRequest, ZarinpalGateway, BusinessRuleException

### Community 28 - "Community 28"
Cohesion: 0.10
Nodes (14): PaymentMethod, APP, CARD, CASH, GATEWAY, WEB, PaymentStatus, FAILED (+6 more)

### Community 30 - "Community 30"
Cohesion: 0.15
Nodes (5): AttributeSpecification, CategorySpecification, ItemSpecification, TagSpecification, MediaSpecification

### Community 31 - "Community 31"
Cohesion: 0.14
Nodes (5): TagResponse, TagSearchRequest, TagUpdateRequest, TagController, TagService

### Community 32 - "Community 32"
Cohesion: 0.08
Nodes (5): OrderCharge, OrderChargeType, ITEM_VARIANT, PACKAGING, SHIPPING

### Community 33 - "Community 33"
Cohesion: 0.08
Nodes (9): ConditionOperator, ConditionOperatorCode, EQ, GT, GTE, IN, LT, LTE (+1 more)

### Community 34 - "Community 34"
Cohesion: 0.10
Nodes (3): Inspection, MediaInspector, InvalidResourceException

### Community 35 - "Community 35"
Cohesion: 0.14
Nodes (7): AttributeRepository, ItemVariantRepository, ProductRepository, TagRepository, DeliveryRepository, OrderRepository, OrderTransactionRepository

### Community 36 - "Community 36"
Cohesion: 0.15
Nodes (4): CreateProduct, DeleteProduct, ProductControllerTest, UpdateProduct

### Community 37 - "Community 37"
Cohesion: 0.11
Nodes (6): DiscountMapper, DiscountDTO, DiscountType, DiscountTypeCode, FIXED, PERCENT

### Community 38 - "Community 38"
Cohesion: 0.15
Nodes (4): SMSDTO, SMSEventDTO, SMSTemplateDTO, SMSServiceImpl

### Community 39 - "Community 39"
Cohesion: 0.17
Nodes (6): MediaSearchRequest, MediaRepository, Media, MediaStatus, DRAFT, SAVED

### Community 40 - "Community 40"
Cohesion: 0.12
Nodes (3): SMSType, OTP, SMSTemplate

### Community 41 - "Community 41"
Cohesion: 0.17
Nodes (4): AuthenticationService, AuthenticationResponse, AuthenticationController, PromotionController

### Community 42 - "Community 42"
Cohesion: 0.17
Nodes (6): DiscountRepository, PromotionCreateRequest, PromotionDTO, PromotionUpdateRequest, PromotionServiceImpl, PromotionService

### Community 43 - "Community 43"
Cohesion: 0.20
Nodes (3): APIError, APIException, GlobalExceptionHandler

### Community 47 - "Community 47"
Cohesion: 0.15
Nodes (4): PaymentMethodDTO, PriceResponse, ItemVariantResponse, ItemDeliveryDto

### Community 48 - "Community 48"
Cohesion: 0.22
Nodes (3): DeliveryService, DeliveryController, DeliveryDTO

### Community 49 - "Community 49"
Cohesion: 0.18
Nodes (5): SMSWrapperResponseDTO, MelipayamakProvider, MeliPayamakSendSMSRequestBody, SMSContractWrapper, SMSService

### Community 51 - "Community 51"
Cohesion: 0.20
Nodes (3): UserDTO, UserServiceImp, UserService

### Community 52 - "Community 52"
Cohesion: 0.22
Nodes (3): MediaServiceImpl, TransactionSynchronization, TransactionSynchronization

### Community 54 - "Community 54"
Cohesion: 0.21
Nodes (3): VerifyPaymentApiRequest, PaymentGatewayType, ZARINPAL

### Community 61 - "Community 61"
Cohesion: 0.18
Nodes (3): PaymentDTO, PaymentController, PaymentService

### Community 62 - "Community 62"
Cohesion: 0.13
Nodes (9): OrderStatusType, APPROVED, CANCELED, FINISHED, PAID, PENDING, PROCESSING, REFUND (+1 more)

### Community 67 - "Community 67"
Cohesion: 0.21
Nodes (6): SMSProvider, MELLI_PAYAMAK, SMSContract, SMSFactoryContract, SMSFactoryContractImp, SMSRepository

### Community 69 - "Community 69"
Cohesion: 0.23
Nodes (5): PromotionEffectApplier, PromotionHandler, PromotionPipeline, PromotionEngineImpl, PromotionResolver

### Community 72 - "Community 72"
Cohesion: 0.14
Nodes (11): DeliveryStatusType, CANCELLED, DELIVERED, IN_TRANSIT, LOST, OUT_FOR_DELIVERY, PENDING, PROCESSING (+3 more)

### Community 73 - "Community 73"
Cohesion: 0.21
Nodes (4): Coupon, DeliveryPromotionRequest, ItemPromotionRequest, PromotionRequest

### Community 82 - "Community 82"
Cohesion: 0.20
Nodes (5): Reservation, ReservationStatus, ACTIVE, CANCELLED, RELEASED

### Community 84 - "Community 84"
Cohesion: 0.31
Nodes (3): ZarinpalRequest, Data, ZarinpalResponse

### Community 87 - "Community 87"
Cohesion: 0.22
Nodes (5): ShopDTO, ShopStatus, ACTIVE, INACTIVE, PENDING

### Community 92 - "Community 92"
Cohesion: 0.29
Nodes (4): AddressType, HOME, OTHER, WORK

### Community 93 - "Community 93"
Cohesion: 0.33
Nodes (3): ItemVariantStatusCode, MARKETABLE, OUT_OF_STOCK

### Community 94 - "Community 94"
Cohesion: 0.33
Nodes (5): OrderDetailStatus, CANCELLED, COMPLETED, PENDING, REJECTED

### Community 96 - "Community 96"
Cohesion: 0.40
Nodes (4): ProductStatus, ACTIVE, INACTIVE, OUT_OF_STOCK

### Community 97 - "Community 97"
Cohesion: 0.40
Nodes (4): LoginType, EMAIL, PASSWORD, SMS

## Knowledge Gaps
- **108 isolated node(s):** `com.sashia:ecommerce`, `CARD`, `WEB`, `APP`, `CASH` (+103 more)
  These have ≤1 connection - possible missing edges. (Counts symbols only; 795 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **126 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Promotion` connect `Community 13` to `Community 4`, `Community 5`, `Community 69`, `Community 7`, `Community 8`, `Community 73`, `Community 10`, `Community 11`, `Community 12`, `Community 41`, `Community 15`, `Community 18`, `Community 19`, `Community 22`, `Community 23`?**
  _High betweenness centrality (0.083) - this node is a cross-community bridge._
- **What connects `com.sashia:ecommerce`, `CARD`, `WEB` to the rest of the system?**
  _108 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Community 0` be split into smaller, more focused modules?**
  _Cohesion score 0.06895745675549322 - nodes in this community are weakly interconnected._
- **Why does `Order` connect `Community 5` to `Community 32`, `Community 35`, `Community 6`, `Community 7`, `Community 11`, `Community 12`, `Community 13`, `Community 14`, `Community 44`, `Community 50`, `Community 53`, `Community 22`, `Community 27`, `Community 28`, `Community 62`?**
  _High betweenness centrality (0.059) - this node is a cross-community bridge._
- **Should `Community 1` be split into smaller, more focused modules?**
  _Cohesion score 0.12103174603174603 - nodes in this community are weakly interconnected._
- **Why does `DeliveryOption` connect `Community 5` to `Community 6`, `Community 7`, `Community 73`, `Community 10`, `Community 11`, `Community 12`, `Community 14`, `Community 54`, `Community 58`?**
  _High betweenness centrality (0.052) - this node is a cross-community bridge._
- **Should `Community 2` be split into smaller, more focused modules?**
  _Cohesion score 0.08852459016393442 - nodes in this community are weakly interconnected._

## Extraction limitation

Documentation/configuration semantics were not extracted in this run (96 files); graph contains structural code extraction only.
