package com.sashia.ecommerce.billing.payment.opg.provider.zarinpal;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Zarinpal gateway configuration.
 * Bind from {@code payment.gateways.zarinpal.*} in application config.
 * <p>
 * When {@code sandbox=true} and URLs are left at defaults, sandbox endpoints are used.
 */
@ConfigurationProperties(prefix = "payment.gateways.zarinpal")
public class ZarinpalProperties {

    private static final String PROD_REQUEST = "https://payment.zarinpal.com/pg/v4/payment/request.json";
    private static final String PROD_VERIFY = "https://payment.zarinpal.com/pg/v4/payment/verify.json";
    private static final String PROD_START_PAY = "https://payment.zarinpal.com/pg/StartPay/";

    private static final String SANDBOX_REQUEST = "https://sandbox.zarinpal.com/pg/v4/payment/request.json";
    private static final String SANDBOX_VERIFY = "https://sandbox.zarinpal.com/pg/v4/payment/verify.json";
    private static final String SANDBOX_START_PAY = "https://sandbox.zarinpal.com/pg/StartPay/";

    private String merchantId = "";
    private String requestUrl = PROD_REQUEST;
    private String verifyUrl = PROD_VERIFY;
    private String startPayUrl = PROD_START_PAY;
    private String callbackUrl = "";
    private String currency = "IRT";
    private boolean sandbox = false;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getRequestUrl() {
        if (sandbox && PROD_REQUEST.equals(requestUrl)) {
            return SANDBOX_REQUEST;
        }
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getVerifyUrl() {
        if (sandbox && PROD_VERIFY.equals(verifyUrl)) {
            return SANDBOX_VERIFY;
        }
        return verifyUrl;
    }

    public void setVerifyUrl(String verifyUrl) {
        this.verifyUrl = verifyUrl;
    }

    public String getStartPayUrl() {
        if (sandbox && PROD_START_PAY.equals(startPayUrl)) {
            return SANDBOX_START_PAY;
        }
        return startPayUrl;
    }

    public void setStartPayUrl(String startPayUrl) {
        this.startPayUrl = startPayUrl;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isSandbox() {
        return sandbox;
    }

    public void setSandbox(boolean sandbox) {
        this.sandbox = sandbox;
    }
}
