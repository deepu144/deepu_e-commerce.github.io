package com.deepu.orderprocessingservice.request;

import lombok.Data;

@Data
public class PaymentInitiationRequest {
    private String orderId;
    private Double amount;
    private String jwt;
}
