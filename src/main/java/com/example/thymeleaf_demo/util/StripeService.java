package com.example.thymeleaf_demo.util;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;

/*@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String secretKey;

    public StripeService(@Value("${stripe.secret.key}") String secretKey) {
        Stripe.apiKey = secretKey;
        this.secretKey = secretKey;
    }

    public PaymentIntent createPaymentIntent(Long amount,String currency) throws StripeException {
        PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
                .setAmount(amount)
                .setCurrency(currency)
                .build();
        return PaymentIntent.create(createParams);
    }

}
*/




























