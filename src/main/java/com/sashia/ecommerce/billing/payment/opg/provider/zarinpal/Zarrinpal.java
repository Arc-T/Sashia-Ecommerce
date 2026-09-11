package com.sashia.ecommerce.billing.payment.opg.provider.zarinpal;

import com.sashia.ecommerce.billing.payment.opg.Bank;
import org.springframework.stereotype.Component;

@Component
public class Zarrinpal extends Bank {

    @Override
    public void initiatePayment() {

    }

    @Override
    public boolean checkPayment() {
        return false;
    }

}
