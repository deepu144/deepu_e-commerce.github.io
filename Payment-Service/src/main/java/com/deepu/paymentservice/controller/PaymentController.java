package com.deepu.paymentservice.controller;

import com.deepu.paymentservice.request.PaymentInitiationRequest;
import com.deepu.paymentservice.request.StripeChargeRequest;
import com.deepu.paymentservice.response.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

public interface PaymentController {

    @PostMapping("/initiate")
    ResponseEntity<CommonResponse> initiatePayment(@RequestBody PaymentInitiationRequest request);

    @PostMapping("/charge")
    @ResponseBody
    ResponseEntity<CommonResponse> charge(@RequestBody StripeChargeRequest request);

}
