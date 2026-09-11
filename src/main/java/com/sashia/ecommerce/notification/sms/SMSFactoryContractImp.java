package com.sashia.ecommerce.notification.sms;

import com.sashia.ecommerce.notification.sms.common.SMSProvider;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;

@Component
public class SMSFactoryContractImp implements SMSFactoryContract {

    private final EnumMap<SMSProvider, SMSContract> providers = new EnumMap<>(SMSProvider.class);

    public SMSFactoryContractImp(List<SMSContract> SMSContracts) {
        for (SMSContract smsContract : SMSContracts) {
            providers.put(smsContract.getProvider(), smsContract);
        }
    }

    @Override
    public SMSContract getService(SMSProvider provider) {
        SMSContract service = providers.get(provider);
        if (service == null) {
            throw new IllegalArgumentException("Unknown provider: " + provider);
        }
        return service;
    }

}