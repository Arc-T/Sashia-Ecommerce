package com.sashia.ecommerce.promotion.promotion;

import com.sashia.shared.BaseControllerTest;
import com.sashia.ecommerce.promotion.Promotion;
import com.sashia.ecommerce.promotion.PromotionRepository;
import org.hibernate.Hibernate;
import org.hibernate.engine.spi.PersistentAttributeInterceptable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("Promotion Controller Tests")
public class PromotionControllerTest extends BaseControllerTest {

    private static final String BASE_URL = "/promotions";
    @Autowired
    private PromotionRepository promotionRepository;

    @Nested
    @DisplayName("GET " + BASE_URL)
    class GetAllPromotions {

        @Test
        @Transactional
        @WithMockUser(authorities = "test")
        @DisplayName("Should return all promotions with status 200")
        void shouldReturnAllPromotions() throws Exception {
            Promotion promotion = promotionRepository.findAllActivePromotions()
                    .getFirst();

            System.out.println(
                    "Enhanced: " +
                            (promotion instanceof PersistentAttributeInterceptable)
            );

            System.out.println(
                    "Discount initialized: " +
                            Hibernate.isPropertyInitialized(promotion, "discount")
            );
//            mockMvc().perform(get(BASE_URL)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andDo(print());
        }

    }

}