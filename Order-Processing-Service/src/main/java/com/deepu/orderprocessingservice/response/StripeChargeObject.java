package com.deepu.orderprocessingservice.response;

import lombok.Data;

@Data
public class StripeChargeObject {
    private Boolean success;
    private String message;
    private String chargeId;
}

