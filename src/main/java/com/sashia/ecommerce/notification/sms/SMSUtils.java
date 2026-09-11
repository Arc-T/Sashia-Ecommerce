package com.sashia.ecommerce.notification.sms;

import com.sashia.ecommerce.notification.sms.common.SMSTemplateDTO;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public final class SMSUtils {

    private static final Logger log = LoggerFactory.getLogger(SMSUtils.class);

    private SMSUtils() {
    }

    public static String formatSmsText(SMSTemplateDTO template, String... smsParams) {
        if (template.params() > 0) {
            if (ArrayUtils.isEmpty(smsParams) && smsParams.length == template.params()) {
                String message = "";
                for (int i = 0; i < template.params(); i++) {
                    if (StringUtils.hasText(smsParams[i])) {
                        message = template.message().replace("{" + i + "}", smsParams[i]);
                    } else {
                        log.error("Sms template: {} param: {} is not filled", template.type(), i);
                        throw new IllegalStateException();
                    }
                }
                return message;
            } else {
                log.error("Sms template: {} params are not provided correctly", template.type());
                throw new IllegalStateException();
            }
        } else return template.message();
    }

}
