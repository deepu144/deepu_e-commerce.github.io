package com.deepu.shoppingcartservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Cart {
    @Id
    private String _id;
    private String uniqueId;
    private String productId;
    private long quantity;
    private String email;
}
