package com.sashia.ecommerce.billing.payment;

import com.sashia.ecommerce.billing.payment.dto.PaymentDTO;

interface PaymentService {

    /* ******************************** CRUD ******************************** */

    Long create(PaymentDTO payment);

    /* ******************************** OPERATIONS ******************************** */
    //int amount, String authority, long userId
    PaymentDTO verifyPayment();

}
