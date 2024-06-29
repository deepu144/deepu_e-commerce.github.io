package com.deepu.paymentservice.request;

import lombok.Data;

@Data
public class PaymentInitiationRequest {
    private String orderId;
    private String email;
    private Double amount;
}
