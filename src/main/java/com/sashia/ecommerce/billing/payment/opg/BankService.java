package com.sashia.ecommerce.billing.payment.opg;

public interface BankService {

    void initiatePayment();

    boolean checkPayment();

}
