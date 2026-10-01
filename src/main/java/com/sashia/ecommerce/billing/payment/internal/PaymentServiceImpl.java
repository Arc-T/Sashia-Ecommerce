package com.sashia.ecommerce.billing.payment.internal;

import com.sashia.ecommerce.billing.payment.Payment;
import com.sashia.ecommerce.billing.payment.PaymentRepository;
import com.sashia.ecommerce.billing.payment.PaymentService;
import com.sashia.ecommerce.billing.payment.dto.PaymentDTO;
import com.sashia.ecommerce.billing.payment.dto.PaymentStatus;
import com.sashia.ecommerce.billing.payment.opg.PaymentGateway;
import com.sashia.ecommerce.billing.payment.opg.PaymentGatewayRegistry;
import com.sashia.ecommerce.billing.payment.opg.PaymentGatewayType;
import com.sashia.ecommerce.billing.payment.opg.PaymentLockService;
import com.sashia.ecommerce.billing.payment.opg.dto.PaymentInitiateRequest;
import com.sashia.ecommerce.billing.payment.opg.dto.PaymentInitiateResult;
import com.sashia.ecommerce.billing.payment.opg.dto.PaymentVerifyRequest;
import com.sashia.ecommerce.billing.payment.opg.dto.PaymentVerifyResult;
import com.sashia.ecommerce.identity.user.User;
import com.sashia.ecommerce.identity.user.UserRepository;
import com.sashia.ecommerce.ordering.order.Order;
import com.sashia.ecommerce.ordering.order.OrderRepository;
import com.sashia.ecommerce.ordering.order.OrderService;
import com.sashia.ecommerce.ordering.order.dto.OrderStatusType;
import com.sashia.shared.exception.BusinessRuleException;
import com.sashia.shared.exception.ResourceNotFoundException;
import com.sashia.shared.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final OrderService orderService;
    private final UserRepository userRepository;
    private final PaymentLockService lockService;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayRegistry gatewayRegistry;

    @Override
    @Transactional
    public PaymentInitiateResult initiate(PaymentInitiateRequest request) {
        return lockService.execute(request.orderId(), () -> doInitiate(request));
    }

    @Override
    @Transactional
    public PaymentVerifyResult verify(PaymentVerifyRequest request) {
        return lockService.execute(request.orderId(), () -> doVerify(request));
    }

    @Override
    public Optional<PaymentDTO> get(Long id) {
        return paymentRepository.findById(id).map(PaymentMapper::toDTO);
    }

    @Override
    public Optional<PaymentDTO> getByAuthority(String authority) {
        return paymentRepository.findByAuthority(authority).map(PaymentMapper::toDTO);
    }

    @Override
    public PaymentVerifyResult verifyCallback(String authority, String status, PaymentGatewayType gatewayType) {
        return null;
    }

    // -------------------------------------------------------------------------
    // Internal
    // -------------------------------------------------------------------------

    private PaymentInitiateResult doInitiate(PaymentInitiateRequest request) {
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new ResourceNotFoundException("order.not.found"));

        if (order.getStatus() != OrderStatusType.PENDING) {
            throw new BusinessRuleException("payment.order.status.not.pending");
        }

        User user = userRepository.findById(SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("user.not.found"));

        boolean alreadyPaid = paymentRepository.findByOrderId(request.orderId()).stream()
                .anyMatch(p -> p.getStatus() == PaymentStatus.PAID);

        if (alreadyPaid) {
            throw new BusinessRuleException("payment.order.already.paid");
        }

        PaymentGateway gateway = gatewayRegistry.get(request.gatewayType());
        PaymentInitiateResult result = gateway.initiate(request);

        Payment payment = PaymentMapper.toPendingEntity(request, result, user, order);
        paymentRepository.save(payment);

        return result;
    }

    private PaymentVerifyResult doVerify(PaymentVerifyRequest request) {
        Payment payment = paymentRepository.findByAuthority(request.authority())
                .orElseThrow(() -> new ResourceNotFoundException("payment.not.found"));

        if (payment.getStatus() == PaymentStatus.PAID) {
            return new PaymentVerifyResult(
                    request.gatewayType(),
                    PaymentStatus.PAID,
                    payment.getAuthority(),
                    payment.getReferenceId(),
                    payment.getFeeType(),
                    payment.getFee() != 0 ? payment.getFee() : null
            );
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new BusinessRuleException("payment.status.not.pending");
        }

        if (payment.getAmount() != request.amount()) {
            throw new BusinessRuleException("payment.amount.mismatch");
        }

        PaymentGateway gateway = gatewayRegistry.get(request.gatewayType());
        PaymentVerifyResult result = gateway.verify(request);

        PaymentMapper.applyVerify(payment, result);
        paymentRepository.save(payment);

        applyOrderStatusAfterPayment(payment.getOrder().getId(), result); //TODO: could be done without join

        return result;
    }

    /**
     * On successful gateway verify → order PENDING → PAID.
     * On failed verify → order stays PENDING (user may retry another payment).
     * Rejected payments do not cancel the order automatically.
     */
    private void applyOrderStatusAfterPayment(Long orderId, PaymentVerifyResult result) {
        if (result.isSuccessful()) {
            orderService.transitionStatus(
                    orderId,
                    OrderStatusType.PAID,
                    "Payment verified | authority=" + result.authority()
                            + (result.referenceId() != null ? " ref=" + result.referenceId() : "")
            );
        }
        // FAILED: leave order PENDING so the customer can try again
    }
}
