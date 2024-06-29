package com.deepu.paymentservice.request;

import lombok.Data;

@Data
public class StripeChargeRequest {
    private String stripeToken;
    private Long paymentId;
}

