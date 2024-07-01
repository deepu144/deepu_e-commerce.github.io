package com.deepu.orderprocessingservice.request;

import lombok.Data;
import java.util.Date;

@Data
public class OrderStatusRequest {
    private String status;
    private Date deliverDate;
}
