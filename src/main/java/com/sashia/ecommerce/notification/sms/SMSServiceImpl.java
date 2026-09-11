package com.sashia.ecommerce.notification.sms;

import com.sashia.ecommerce.notification.SMSService;
import com.sashia.ecommerce.notification.dto.SMSDTO;
import com.sashia.ecommerce.notification.dto.SMSEventDTO;
import com.sashia.ecommerce.notification.dto.SMSType;
import com.sashia.ecommerce.notification.sms.common.SMSProvider;
import com.sashia.ecommerce.notification.sms.common.SMSTemplateDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Optional;

@Service
public class SMSServiceImpl implements SMSService {

    private final SMSRepository smsRepository;
    private final SMSFactoryContract smsFactoryContract;

    public SMSServiceImpl(SMSRepository smsRepository, SMSFactoryContract smsFactoryContract) {
        this.smsRepository = smsRepository;
        this.smsFactoryContract = smsFactoryContract;
    }

    @Override
    public Long create(SMSDTO smsDTO) {
        return null;
        // return smsRepository.create(smsDTO);
    }

    @Override
    public Optional<SMSDTO> get(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<SMSTemplateDTO> getTemplateByType(SMSType smsType) {
        // return smsRepository.getTemplateByType(smsType).map(SMSTemplateDTO::toDTO);
        return null;
        
    }

    @Override
    public Optional<SMSDTO> getPhoneLatestSmsMessage(String phone) {
        return Optional.empty();
    }

    // ****************************** OPERATIONS ******************************

    @Override
    @TransactionalEventListener
    public void sendSms(SMSEventDTO event) {
        smsFactoryContract.getService(SMSProvider.MELLI_PAYAMAK).send(event);
    }

}