package com.sashia.ecommerce.platform;

import com.sashia.shared.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

public class PlatformTest extends BaseControllerTest {

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    void testManager() {
        IO.println("________________________________________");
        IO.println(transactionManager);
    }

}
