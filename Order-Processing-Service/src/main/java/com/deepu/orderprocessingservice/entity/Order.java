package com.deepu.orderprocessingservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.UUID;

@Data
@Document
public class Order {
    @Id
    private String _id;
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
