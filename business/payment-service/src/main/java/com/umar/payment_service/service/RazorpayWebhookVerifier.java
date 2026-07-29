package com.umar.payment_service.service;

import com.umar.exceptions.common.exception.ApiException;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class RazorpayWebhookVerifier {

    @Value("${razorpay.webhook.secret}")
    private String secret;

    public void verify(String payload, String signature) {
        String calculated = HmacUtils.hmacSha256Hex(secret, payload);
        if (!calculated.equals(signature)) {
            throw new ApiException(HttpStatus.BAD_REQUEST,"Invalid signature","");
        }
    }

}
