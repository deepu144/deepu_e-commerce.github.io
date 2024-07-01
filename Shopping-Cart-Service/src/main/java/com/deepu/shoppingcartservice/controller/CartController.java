package com.deepu.shoppingcartservice.controller;

import com.deepu.shoppingcartservice.constant.Constant;
import com.deepu.shoppingcartservice.request.CartRequest;
import com.deepu.shoppingcartservice.response.CommonResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

public interface CartController {

    @ApiResponse(description = "Add Product to Cart by Product-UniqueId")
    @PostMapping("/add/{product-uniqueId}")
    ResponseEntity<CommonResponse> addProductToCart(@PathVariable("product-uniqueId") @NotNull(message = Constant.PRODUCT_ID_NOT_NULL) String productUniqueId
            , @RequestBody @NotNull(message = Constant.QUANTITY_NOT_PROVIDED) @Min(value = 1,message = Constant.MINIMUM_QUANTITY_NOT_MEET) Long quantity
            , BindingResult result
    );

    @ApiResponse(description = "Get Product From Cart By Cart-UniqueId")
    @GetMapping("/{cart-uniqueId}")
    ResponseEntity<CommonResponse> getProductFromCart(@PathVariable("cart-uniqueId") String cartUniqueId);

    @ApiResponse(description = "Update the Cart By Cart-UniqueID")
    @PutMapping("/{cart-uniqueId}")
    ResponseEntity<CommonResponse> updateCartByUniqueId(@PathVariable("cart-uniqueId") String cartUniqueId, @RequestBody @Valid CartRequest cartRequest, BindingResult result);

    @ApiResponse(description = "Delete Cart By Cart-UniqueID")
    @DeleteMapping("/{cart-uniqueId}")
    ResponseEntity<CommonResponse> deleteCartByUniqueId(@PathVariable("cart-uniqueId") String cartUniqueId);

    @ApiResponse(description = "Get All Cart From User E-mail")
    @GetMapping("/")
    ResponseEntity<CommonResponse> getAllCartByEmail();

    @ApiResponse(description = "Get Total Price of User Cart Products")
    @GetMapping("/price")
    ResponseEntity<CommonResponse> getTotalPriceFromUserCart();

}
