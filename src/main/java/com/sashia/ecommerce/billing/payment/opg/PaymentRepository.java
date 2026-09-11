package com.sashia.ecommerce.billing.payment.opg;

import com.sashia.ecommerce.billing.payment.Payment;
import com.sashia.ecommerce.billing.payment.dto.PaymentDTO;

import java.util.Optional;

interface PaymentRepository {

    Long create(PaymentDTO payment);

    void update(Long id, PaymentDTO payment);

    Optional<Payment> get(Long id);

    Long count();

    boolean exists(Long id);

}
