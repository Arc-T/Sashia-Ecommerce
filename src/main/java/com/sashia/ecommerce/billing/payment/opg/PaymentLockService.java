package com.sashia.ecommerce.billing.payment.opg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * Per-transaction lock to prevent concurrent initiate/verify races
 * on the same order (or payment id).
 */
@Component
public class PaymentLockService {

    private static final Logger log = LoggerFactory.getLogger(PaymentLockService.class);

    private final ConcurrentHashMap<Long, ReentrantLock> locks = new ConcurrentHashMap<>();

    public void execute(long lockKey, Runnable task) {
        execute(lockKey, () -> {
            task.run();
            return null;
        });
    }

    public <T> T execute(long lockKey, Supplier<T> task) {
        ReentrantLock lock = locks.computeIfAbsent(lockKey, _ -> new ReentrantLock());
        lock.lock();
        try {
            log.debug("Lock acquired for key {}", lockKey);
            return task.get();
        } catch (Exception e) {
            log.error("Error while executing locked task for key {}", lockKey, e);
            throw e;
        } finally {
            try {
                lock.unlock();
            } finally {
                if (!lock.hasQueuedThreads()) {
                    locks.remove(lockKey, lock);
                    log.debug("Lock removed for key {}", lockKey);
                }
            }
        }
    }
}
