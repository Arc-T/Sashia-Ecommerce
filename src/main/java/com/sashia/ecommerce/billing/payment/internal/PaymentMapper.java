package com.sashia.ecommerce.billing.payment.internal;

import com.sashia.ecommerce.billing.payment.Payment;
import com.sashia.ecommerce.billing.payment.dto.PaymentDTO;
import com.sashia.ecommerce.billing.payment.dto.PaymentMethod;
import com.sashia.ecommerce.billing.payment.dto.PaymentStatus;
import com.sashia.ecommerce.billing.payment.opg.dto.PaymentInitiateRequest;
import com.sashia.ecommerce.billing.payment.opg.dto.PaymentInitiateResult;
import com.sashia.ecommerce.billing.payment.opg.dto.PaymentVerifyResult;
import com.sashia.ecommerce.identity.user.User;
import com.sashia.ecommerce.ordering.order.Order;

public final class PaymentMapper {

    private PaymentMapper() {
    }

    public static Payment toPendingEntity(PaymentInitiateRequest request, PaymentInitiateResult result, User user, Order order) {
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setOrder(order);
        payment.setPaymentMethod(PaymentMethod.GATEWAY);
        payment.setAmount(request.amount());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setAuthority(result.authority());
        payment.setFeeType(result.feeType());
        if (result.fee() != null) {
            payment.setFee(result.fee());
        }
        payment.setDescription(request.description());
        return payment;
    }

    public static void applyVerify(Payment payment, PaymentVerifyResult result) {
        payment.setStatus(result.status());
        payment.setReferenceId(result.referenceId());
        if (result.feeType() != null) {
            payment.setFeeType(result.feeType());
        }
        if (result.fee() != null) {
            payment.setFee(result.fee());
        }
    }

    public static PaymentDTO toDTO(Payment payment) {
        return new PaymentDTO(
                payment.getId(),
                payment.getUser() != null ? payment.getUser().getId() : null,
                payment.getOrder() != null ? payment.getOrder().getId() : null,
                payment.getPaymentMethod(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getAuthority(),
                payment.getReferenceId(),
                payment.getFeeType(),
                payment.getFee() != 0 ? String.valueOf(payment.getFee()) : null,
                payment.getDescription(),
                payment.getCreatedAt()
        );
    }
}
