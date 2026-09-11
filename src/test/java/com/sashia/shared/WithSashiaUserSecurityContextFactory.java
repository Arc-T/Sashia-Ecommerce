package com.sashia.shared;

import com.sashia.shared.util.SashiaUser;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.List;

public class WithSashiaUserSecurityContextFactory implements WithSecurityContextFactory<WithSashiaUser> {

    @NonNull
    @Override
    public SecurityContext createSecurityContext(WithSashiaUser annotation) {
        List<SimpleGrantedAuthority> authorities = Arrays.stream(annotation.authorities())
                .map(SimpleGrantedAuthority::new)
                .toList();

        SashiaUser user = new SashiaUser("361629708", null, authorities, 1L, "1");

        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        return context;
    }

}
