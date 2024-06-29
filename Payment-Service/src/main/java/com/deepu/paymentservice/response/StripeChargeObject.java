package com.deepu.paymentservice.response;

import lombok.Data;

@Data
public class StripeChargeObject {
    private Boolean success;
    private String message;
    private String chargeId;
}

