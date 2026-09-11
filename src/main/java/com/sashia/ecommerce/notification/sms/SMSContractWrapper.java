package com.sashia.ecommerce.notification.sms;

import com.sashia.ecommerce.notification.SMSService;
import com.sashia.ecommerce.notification.dto.SMSDTO;
import com.sashia.ecommerce.notification.dto.SMSEventDTO;
import com.sashia.ecommerce.notification.sms.common.SMSTemplateDTO;
import com.sashia.ecommerce.notification.sms.common.SMSWrapperResponseDTO;
import jakarta.persistence.EntityNotFoundException;

public abstract class SMSContractWrapper implements SMSContract {

    private final SMSService smsService;

    protected SMSContractWrapper(SMSService smsService) {
        this.smsService = smsService;
    }

    @Override
    public void send(SMSEventDTO event) {
        SMSTemplateDTO template = smsService.getTemplateByType(event.type())
                .orElseThrow(() -> new EntityNotFoundException("SMS.TEMPLATE.IS.INVALID"));
        String smsText = SMSUtils.formatSmsText(template, event.params());
        SMSWrapperResponseDTO response = send(event.phone(), smsText);
        logSmsResponse(response, event.phone(), smsText, template.id());
    }

    protected abstract SMSWrapperResponseDTO send(String to, String message);

    /* =============================== HELPERS ====================================== */

    private void logSmsResponse(SMSWrapperResponseDTO response, String phone, String message, Long templateId) {
        smsService.create(
                new SMSDTO(
                        phone,
                        response.statusId(),
                        templateId,
                        message,
                        response.result(),
                        response.description()
                )
        );
    }

}