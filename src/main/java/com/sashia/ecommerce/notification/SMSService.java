package com.sashia.ecommerce.notification;

import com.sashia.ecommerce.notification.dto.SMSDTO;
import com.sashia.ecommerce.notification.dto.SMSEventDTO;
import com.sashia.ecommerce.notification.dto.SMSType;
import com.sashia.ecommerce.notification.sms.common.SMSTemplateDTO;

import java.util.Optional;

public interface SMSService {

    // ****************************** CRUD ******************************

    Long create(SMSDTO smsDTO);

    Optional<SMSDTO> get(Long id);

    Optional<SMSDTO> getPhoneLatestSmsMessage(String phone);

    Optional<SMSTemplateDTO> getTemplateByType(SMSType type);

    // ****************************** OPERATIONS ******************************

    void sendSms(SMSEventDTO event);

}