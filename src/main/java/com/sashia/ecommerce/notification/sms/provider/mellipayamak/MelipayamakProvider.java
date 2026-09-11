package com.sashia.ecommerce.notification.sms.provider.mellipayamak;

import com.sashia.ecommerce.notification.SMSService;
import com.sashia.ecommerce.notification.sms.SMSContractWrapper;
import com.sashia.ecommerce.notification.sms.common.SMSProvider;
import com.sashia.ecommerce.notification.sms.common.SMSWrapperResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@ConfigurationProperties(prefix = "sms")
public class MelipayamakProvider extends SMSContractWrapper {

    private static final Logger log = LoggerFactory.getLogger(MelipayamakProvider.class);

    private final RestClient restClient;

    private final String url;
    private final String from;
    private final String password;
    private final String username;

    public MelipayamakProvider(RestClient restClient, SMSService smsService, String url,
                               String from, String password, String username) {
        super(smsService);
        this.url = url;
        this.from = from;
        this.password = password;
        this.username = username;
        this.restClient = restClient;
    }

    @Override
    protected SMSWrapperResponseDTO send(String phone, String message) {
        try {
            MeliPayamakSendSMSResponseBody response = restClient.post()
                    .uri(url)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new MeliPayamakSendSMSRequestBody(username, password, from, phone, message, true))
                    .retrieve()
                    .toEntity(MeliPayamakSendSMSResponseBody.class)
                    .getBody();

            if (response != null) {
                return new SMSWrapperResponseDTO(response.RetStatus(), response.StrRetStatus(), response.Value());
            } else
                return new SMSWrapperResponseDTO(0L, null, null);
        } catch (Exception ex) {
            log.error("Failed to send Sms to phone={}: error={}", phone, ex.getMessage());
            return new SMSWrapperResponseDTO(-1L, null, null);
        }
    }

    @Override
    public SMSProvider getProvider() {
        return SMSProvider.MELLI_PAYAMAK;
    }

}
