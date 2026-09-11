package com.sashia.shared;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Locale;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MessageSource messageSource;

    // ============================== GETTERS ==============================

    protected String message(String messageKey, Locale locale) {
        return messageSource().getMessage(messageKey, null, locale);
    }

    public MockMvc mockMvc() {
        return mockMvc;
    }

    public ObjectMapper objectMapper() {
        return objectMapper;
    }

    private MessageSource messageSource() {
        return messageSource;
    }

}
