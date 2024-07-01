package com.deepu.orderprocessingservice.response;

import lombok.Data;
import java.util.Date;
import java.util.UUID;

@Data
public class OrderObject {
    private String uniqueId;
    private String productId;
    private String orderId;
    private String cartId;
    private String email;
    private Date orderDate;
    private Date deliverDate;
    private double productPrice;
    private double deliveryCharge;
    private double handlingFee;
    private long quantity;
    private String status;
    private UUID addressID;
}
