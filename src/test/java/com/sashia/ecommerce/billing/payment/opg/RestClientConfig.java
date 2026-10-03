package com.sashia.ecommerce.billing.payment.opg;

import org.springframework.boot.restclient.autoconfigure.RestClientSsl;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslManagerBundle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * Test-only {@link RestClient.Builder} configuration that disables TLS certificate validation.
 *
 * <p><b>Why this class exists</b><br>
 * The JVM's default truststore ({@code cacerts}) does not contain a valid certificate chain
 * for {@code sandbox.zarinpal.com}, so HTTPS calls fail with:
 * <pre>
 *   PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException:
 *   unable to find valid certification path to requested target
 * </pre>
 * This blocks the Zarinpal sandbox integration test ({@code ZarinpalSandboxIT}) from reaching
 * the gateway. Rather than mutating the JVM-wide truststore (which would affect every
 * process on the machine and require manual {@code keytool} setup on every dev/CI box),
 * this class overrides {@link RestClient.Builder} for the test context only.
 *
 * <p><b>What it does</b><br>
 * Registers a {@code @Primary} {@link RestClient.Builder} backed by an {@link X509TrustManager}
 * that accepts <i>every</i> certificate. The trust manager is wrapped in an {@link SslBundle}
 * and applied via {@link RestClientSsl}, which is Spring Boot 4's supported way of attaching
 * SSL configuration to a {@code RestClient.Builder} (the older {@code HttpClientSettings}
 * record is deprecated since Boot 4.1 and marked for removal).
 *
 * <p><b>Why it is safe here</b>
 * <ul>
 *   <li>It lives in {@code src/test/java}, so it is never packaged into the application jar
 *       and never active in production.</li>
 *   <li>It only affects the Spring test context that loads it — not other JVMs or tools.</li>
 *   <li>It is only reached by tests that are additionally gated on an environment variable
 *       (see {@code ZarinpalSandboxIT}), so accidental CI execution is prevented.</li>
 * </ul>
 *
 * <p><b>⚠️ Do not copy this into {@code src/main}</b><br>
 * Trusting all certificates disables a core security guarantee: it makes the client vulnerable
 * to man-in-the-middle attacks and silent certificate spoofing. For production, import the
 * Zarinpal certificate into a dedicated truststore and reference it via
 * {@code javax.net.ssl.trustStore}, or wire a scoped {@link SslBundle} containing only that
 * certificate.
 *  <a href="https://freedium-mirror.cfd/https://medium.com/javarevisited/understanding-the-jdk-truststore-common-issues-tools-and-practical-solutions-dfcd03c14024">...</a>
 *
 * @see com.sashia.ecommerce.billing.payment.opg.provider.zarinpal.ZarinpalGateway
 */
@Configuration
public class RestClientConfig {

    @Bean
    @Primary
    RestClient.Builder testRestClientBuilder(RestClientSsl restClientSsl) {
        // Trust manager that unconditionally accepts any client/server certificate.
        // Required because the sandbox CA chain is not present in the default JVM cacerts.
        X509TrustManager trustAll = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

        // Wrap the permissive trust manager in a Spring Boot SslBundle so it can be applied
        // through the standard RestClientSsl mechanism instead of low-level request factories.
        SslManagerBundle sslManagerBundle = SslManagerBundle.from(null, trustAll);
        SslBundle sslBundle = SslBundle.of(null, null, null, "TLS", sslManagerBundle);

        // @Primary ensures this builder wins over Boot's default autoconfigured one
        // wherever a RestClient.Builder is injected inside the test context.
        return RestClient.builder().apply(restClientSsl.fromBundle(sslBundle));
    }
}