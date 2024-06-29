package com.deepu.shoppingcartservice.service;

import com.deepu.shoppingcartservice.request.CartRequest;
import com.deepu.shoppingcartservice.response.CommonResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.rmi.NoSuchObjectException;

public interface CartService {
    CommonResponse addProductToCart(String productUniqueId,long quantity) throws Exception;

    CommonResponse getProductFromCart(String cartUniqueId) throws Exception;

    CommonResponse updateCart(String cartUniqueId, CartRequest cartRequest) throws Exception;

    CommonResponse deleteCart(String cartUniqueId) throws NoSuchObjectException;

    CommonResponse getCartByEmail(String email) throws Exception;
}
