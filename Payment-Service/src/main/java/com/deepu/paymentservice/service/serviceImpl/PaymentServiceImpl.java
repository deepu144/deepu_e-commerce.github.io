package com.deepu.paymentservice.service.serviceImpl;

import com.deepu.paymentservice.entity.Payment;
import com.deepu.paymentservice.repository.PaymentRepository;
import com.deepu.paymentservice.request.PaymentInitiationRequest;
import com.deepu.paymentservice.request.StripeChargeRequest;
import com.deepu.paymentservice.response.StripeChargeObject;
import com.deepu.paymentservice.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    @Value("${api.stripe.key}")
    private String stripeApiKey;

    @PostConstruct
    public void init(){
        Stripe.apiKey = stripeApiKey;
    }

    @Autowired
    private PaymentRepository paymentRepository;

    public String initiatePayment(PaymentInitiationRequest request) {
        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setEmail(request.getEmail());
        payment.setAmount(request.getAmount());
        payment.setStatus("INITIATED");
        payment.setDateTime(LocalDateTime.now());
        paymentRepository.save(payment);

        return "/payment.html?paymentId=" + payment.getId();
    }

    public StripeChargeObject charge(StripeChargeRequest stripeChargeRequest) {
        try {
            Payment payment = paymentRepository.findById(stripeChargeRequest.getPaymentId())
                    .orElseThrow(() -> new RuntimeException("Invalid payment ID"));

            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", (int) (payment.getAmount() * 100));
            chargeParams.put("currency", "USD");
            chargeParams.put("description", "Payment for order " + payment.getOrderId());
            chargeParams.put("source", stripeChargeRequest.getStripeToken());
            Charge charge = Charge.create(chargeParams);

            payment.setTransactionId(charge.getId());
            payment.setStatus(charge.getPaid() ? "COMPLETED" : "FAILED");
            payment.setDateTime(LocalDateTime.now());
            paymentRepository.save(payment);

            StripeChargeObject response = new StripeChargeObject();
            response.setSuccess(charge.getPaid());
            response.setMessage(charge.getOutcome().getSellerMessage());
            response.setChargeId(charge.getId());
            return response;
        } catch (StripeException e) {
            log.error("StripeService (charge)", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}

