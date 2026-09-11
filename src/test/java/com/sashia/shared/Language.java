package com.sashia.shared;

import java.util.Locale;

public enum Language {

    ENGLISH("en-US"), PERSIAN("fa-IR");

    private final String locale;

    Language(String locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return Locale.forLanguageTag(locale);
    }

}
