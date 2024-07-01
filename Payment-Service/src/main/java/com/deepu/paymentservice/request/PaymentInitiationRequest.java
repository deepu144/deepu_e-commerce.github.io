package com.deepu.paymentservice.request;

import lombok.Data;

@Data
public class PaymentInitiationRequest {
    private String orderId;
    private Double amount;
    private String jwt;
}
