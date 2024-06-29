package com.deepu.paymentservice.service;

import com.deepu.paymentservice.request.PaymentInitiationRequest;
import com.deepu.paymentservice.request.StripeChargeRequest;
import com.deepu.paymentservice.response.StripeChargeObject;

public interface PaymentService {
    String initiatePayment(PaymentInitiationRequest request);
    StripeChargeObject charge(StripeChargeRequest request);
}
