package com.sashia.ecommerce.catalog.product;

import com.sashia.shared.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class ProductControllerTest extends BaseControllerTest {

    @Test
    @WithMockUser(authorities = "READ_ALL_PRODUCTS")
    void testGetAllProducts_returnsOk() throws Exception {
        mockMvc().perform(MockMvcRequestBuilders.get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

}
