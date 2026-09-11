package com.sashia.shared.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import com.sashia.shared.monitoring.SecurityMetersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class JWTConfiguration {

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;
    private static final Logger log = LoggerFactory.getLogger(JWTConfiguration.class);
    //TODO: hardcoded in code !!!!
    private static final String JWT_KEY = "ZjJlOGM0ZDliN2E1ZjNlNmMyZDhiNGE5ZjdlNWMzZDZiOGEyZjllN2M0ZDFiNmE4ZjNlNWM5ZDdiMmE0ZjhlNmMzZDliNWE3ZjJlNGM4ZDZiM2E5ZjVlN2MyZDRiOGE2ZjNlOWM1ZDdiMWE0ZjhlNmMyZDliN2E1ZjNlOGM0ZDZiMmE5ZjdlNWMzZDhiNmE0ZjJlOWM3ZDViM2E4ZjZlNGMyZDliN2E1ZjNlOGM2ZDQ=";

    @Bean
    JwtDecoder jwtDecoder(SecurityMetersService metersService) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey()).macAlgorithm(JWT_ALGORITHM).build();
        return token -> {
            try {
                log.error("_________________ valid signature ______________________");
                return jwtDecoder.decode(token);
            } catch (Exception e) {
                if (e.getMessage().contains("Invalid signature")) {
                    log.error("_________________ Invalid signature ______________________");
                } else if (e.getMessage().contains("Jwt expired at")) {
                    metersService.trackTokenExpired();
                } else if (
                        e.getMessage().contains("Invalid JWT serialization") ||
                                e.getMessage().contains("Malformed token") ||
                                e.getMessage().contains("Invalid unsecured/JWS/JWE")
                ) {
                    metersService.trackTokenMalformed();
                    log.error("_________________ Invalid JWT serialization ______________________");
                } else {
                    log.error("Unknown JWT error {}", e.getMessage());
                }
                throw e;
            }
        };
    }

//    @Bean
//    JwtAuthenticationConverter jwtAuthenticationConverter() {
//        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
//        grantedAuthoritiesConverter.setAuthorityPrefix("");
//        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
//        return jwtAuthenticationConverter;
//    }

    @Bean
    JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(JWT_KEY).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }

}