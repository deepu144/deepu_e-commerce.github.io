package com.deepu.paymentservice.controller.controllerImpl;

import com.deepu.paymentservice.controller.PaymentController;
import com.deepu.paymentservice.request.PaymentInitiationRequest;
import com.deepu.paymentservice.request.StripeChargeRequest;
import com.deepu.paymentservice.response.StripeChargeObject;
import com.deepu.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe")
public class PaymentControllerImpl implements PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<String> initiatePayment(@RequestBody PaymentInitiationRequest request) {
        String paymentPageUrl = paymentService.initiatePayment(request);
        return ResponseEntity.ok(paymentPageUrl);
    }

    @PostMapping("/charge")
    @ResponseBody
    public StripeChargeObject charge(@RequestBody StripeChargeRequest request) {
        return paymentService.charge(request);
    }

}
