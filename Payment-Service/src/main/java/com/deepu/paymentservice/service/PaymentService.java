package com.deepu.paymentservice.service;

import com.deepu.paymentservice.request.PaymentInitiationRequest;
import com.deepu.paymentservice.request.StripeChargeRequest;
import com.deepu.paymentservice.response.CommonResponse;

public interface PaymentService {
    CommonResponse initiatePayment(PaymentInitiationRequest request);
    CommonResponse charge(StripeChargeRequest request);
}
