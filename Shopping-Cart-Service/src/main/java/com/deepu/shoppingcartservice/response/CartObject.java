package com.deepu.shoppingcartservice.response;

import lombok.Data;

@Data
public class CartObject {
    private String uniqueId;
    private ProductObject productObject;
    private long quantity;
    private String email;
}
