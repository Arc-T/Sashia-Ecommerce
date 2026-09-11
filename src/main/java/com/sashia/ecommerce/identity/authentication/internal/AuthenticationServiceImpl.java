package com.sashia.ecommerce.identity.authentication.internal;

import com.sashia.ecommerce.identity.authentication.AuthenticationService;
import com.sashia.ecommerce.identity.authentication.dto.AuthenticationDTO;
import com.sashia.ecommerce.identity.authentication.dto.AuthenticationResponse;
import com.sashia.ecommerce.identity.authentication.dto.LoginRequest;
import com.sashia.shared.util.SecurityUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static com.sashia.shared.config.JWTConfiguration.JWT_ALGORITHM;

@Service
@Transactional(readOnly = true)
class AuthenticationServiceImpl implements AuthenticationService {

    private static final String ISSUER = "https://sashia.dev";
    private static final long DEFAULT_EXPIRATION_TIME = 3_600_000L; // 1 hour.
    private static final long REMEMBER_ME_EXPIRATION_TIME = 2_592_000_000L; // 1 month

    private final JwtEncoder jwtEncoder;
    private final AuthenticationManager authenticationManager;
    private final ApplicationEventPublisher applicationEventPublisher;

    public AuthenticationServiceImpl(JwtEncoder jwtEncoder, AuthenticationManager authenticationManager, ApplicationEventPublisher applicationEventPublisher) {
        this.jwtEncoder = jwtEncoder;
        this.authenticationManager = authenticationManager;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public AuthenticationResponse authenticateAdmin(LoginRequest request) {
        // TODO: authentication token should be dynamic ex: Username&Password - OTP
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.username(), request.password());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        return new AuthenticationResponse(generateToken(authentication, request.rememberMe()));
    }

    @Override
    public AuthenticationDTO authenticateCustomer(AuthenticationDTO authentication) {
        return null;
//        return new AuthenticationDTO(
//                120,
//                smsService.getOrCreateTtl(authentication.username()),
//                false,
//                userService.existsByPhone(authentication.username()), //TODO
//                authentication.username()
//        );?
    }

    // =================================== HELPERS ===================================

    private String generateToken(Authentication authentication, boolean rememberMe) {
        List<String> authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        Instant now = Instant.now();
        Instant validity;
        if (rememberMe) {
            validity = now.plus(REMEMBER_ME_EXPIRATION_TIME, ChronoUnit.SECONDS);
        } else {
            validity = now.plus(DEFAULT_EXPIRATION_TIME, ChronoUnit.SECONDS);
        }

        JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
                .id(UUID.randomUUID().toString())
                .subject(authentication.getName())
                .issuer(ISSUER)
                .issuedAt(now)
                .expiresAt(validity)
                .audience(List.of("sashia-ecommerce")) //TODO: configuration constant is needed !
                .claim("scp", authorities) //TODO: configuration constant is needed !
                .claim("amr", "something") //TODO: Authentication method reference
                .claim("vip", SecurityUtils.getCurrentUserVipGroup());

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, builder.build())).getTokenValue();
    }

}