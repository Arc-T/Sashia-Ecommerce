package com.sashia.shared;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@ParameterizedTest(name = "Language: {0}")
@EnumSource(Language.class)
public @interface TestWithLocale {
}
