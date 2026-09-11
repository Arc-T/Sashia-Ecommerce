package com.sashia.ecommerce.billing.payment.opg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class PaymentLockService {

    private static final Logger log = LoggerFactory.getLogger(PaymentLockService.class);

    private final ConcurrentHashMap<Long, ReentrantLock> locks = new ConcurrentHashMap<>();

    public void executeTask(long transactionId, Runnable task) {
        ReentrantLock reentrantLock = locks.computeIfAbsent(transactionId, _ -> new ReentrantLock());
        reentrantLock.lock();
        try {
            log.debug("Lock acquired for transactionId {}", transactionId);
            task.run();
            log.debug("Task executed for transactionId {}", transactionId);
        } catch (Exception e) {
            log.error("Error occurred while executing task", e);
            throw e;
        } finally {
            try {
                reentrantLock.unlock();
            } finally {
                if (!reentrantLock.hasQueuedThreads()) {
                    locks.remove(transactionId, reentrantLock);
                    log.debug("Lock removed for transactionId {}", transactionId);
                }
            }
        }
    }

}
