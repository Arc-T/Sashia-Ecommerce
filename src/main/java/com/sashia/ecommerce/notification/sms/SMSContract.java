package com.sashia.ecommerce.notification.sms;

import com.sashia.ecommerce.notification.dto.SMSEventDTO;
import com.sashia.ecommerce.notification.sms.common.SMSProvider;

public interface SMSContract {

    SMSProvider getProvider();

    void send(SMSEventDTO event);

}
