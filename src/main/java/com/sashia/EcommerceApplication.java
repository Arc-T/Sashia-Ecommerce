package com.sashia;

import com.sashia.ecommerce.notification.sms.provider.mellipayamak.MelipayamakProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableConfigurationProperties(MelipayamakProvider.class)
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class EcommerceApplication {
    static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }
}