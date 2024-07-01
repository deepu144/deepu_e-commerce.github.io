package com.deepu.paymentservice.request;

import lombok.Data;

import java.util.Date;

@Data
public class OrderStatusRequest {
    private String status;
    private Date deliverDate;
}
