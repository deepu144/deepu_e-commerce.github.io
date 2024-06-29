package com.deepu.shoppingcartservice.util;

import com.deepu.shoppingcartservice.entity.Cart;
import com.deepu.shoppingcartservice.response.CartObject;
import com.deepu.shoppingcartservice.response.ProductObject;

public class Mapper {

    public static CartObject convertToCartObject(Cart cart, ProductObject productObject){
        CartObject cartObject = new CartObject();
        cartObject.setEmail(cart.getEmail());
        cartObject.setProductObject(productObject);
        cartObject.setQuantity(cart.getQuantity());
        cartObject.setUniqueId(cart.getUniqueId());
        return cartObject;
    }

}
