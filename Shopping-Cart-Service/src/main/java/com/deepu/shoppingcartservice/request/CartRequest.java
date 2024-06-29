package com.deepu.shoppingcartservice.request;

import com.deepu.shoppingcartservice.constant.Constant;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartRequest {
    @NotNull(message = Constant.PRODUCT_ID_NOT_NULL)
    private String productId;
    @Min(value = 1 , message = Constant.NOT_ENOUGH_QUANTITY)
    private long quantity;
}
