package com.sashia.ecommerce.notification.sms;

import com.sashia.ecommerce.notification.sms.common.SMSProvider;

public interface SMSFactoryContract {

    SMSContract getService(SMSProvider provider);

}
