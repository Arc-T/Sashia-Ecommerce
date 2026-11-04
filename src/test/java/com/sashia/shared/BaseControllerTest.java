package com.sashia.shared;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.util.Locale;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @Autowired
    private MessageSource messageSource;

    // ============================== GETTERS ==============================

    protected String message(String messageKey, Locale locale) {
        return messageSource().getMessage(messageKey, null, locale);
    }

    public MockMvc mockMvc() {
        return mockMvc;
    }

    public JsonMapper jsonMapper() {
        return jsonMapper;
    }

    private MessageSource messageSource() {
        return messageSource;
    }

}
