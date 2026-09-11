package com.sashia.shared;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithSashiaUserSecurityContextFactory.class)
public @interface WithSashiaUser {
    long userId() default 1L;

    String username() default "361629708";

    String[] authorities() default {};
}