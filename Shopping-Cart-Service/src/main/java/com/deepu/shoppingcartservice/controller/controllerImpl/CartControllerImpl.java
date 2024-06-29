package com.deepu.shoppingcartservice.controller.controllerImpl;

import com.deepu.shoppingcartservice.constant.Constant;
import com.deepu.shoppingcartservice.controller.CartController;
import com.deepu.shoppingcartservice.enumeration.ResponseStatus;
import com.deepu.shoppingcartservice.request.CartRequest;
import com.deepu.shoppingcartservice.response.CommonResponse;
import com.deepu.shoppingcartservice.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;

@RestController
@RequestMapping("/cart")
public class CartControllerImpl implements CartController {

    private static final Logger log = LoggerFactory.getLogger(CartControllerImpl.class);
    @Autowired
    private CartService cartService;

    @Override
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @PostMapping("/add/{product-uniqueId}")
    public ResponseEntity<CommonResponse> addProductToCart(@PathVariable("product-uniqueId") @NotNull(message = Constant.PRODUCT_ID_NOT_NULL) String productUniqueId
            ,@RequestBody @NotNull(message = Constant.QUANTITY_NOT_PROVIDED) @Min(value = 1,message = Constant.MINIMUM_QUANTITY_NOT_MEET) Long quantity
            , BindingResult result){
        if(result.hasErrors()){
            log.error("** addProductToCart: {}", Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            CommonResponse commonResponse = new CommonResponse();
            commonResponse.setCode(400);
            commonResponse.setStatus(ResponseStatus.FAILED);
            commonResponse.setData(null);
            commonResponse.setErrorMessage(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResponse);
        }
        try{
            return ResponseEntity.status(HttpStatus.OK).body(cartService.addProductToCart(productUniqueId,quantity));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(setServerError(e));
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @GetMapping("/{cart-uniqueId}")
    public ResponseEntity<CommonResponse> getProductFromCart(@PathVariable("cart-uniqueId") String cartUniqueId){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(cartService.getProductFromCart(cartUniqueId));
        }catch (Exception e){
            log.error("** getProductFromCart: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(setServerError(e));
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @PutMapping("/{cart-uniqueId}")
    public ResponseEntity<CommonResponse> updateCartByUniqueId(@PathVariable("cart-uniqueId") String cartUniqueId
            ,@RequestBody @Valid CartRequest cartRequest
            , BindingResult result){
        if(result.hasErrors()){
            log.error("** updateCartByUniqueId: {}", Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            CommonResponse commonResponse = new CommonResponse();
            commonResponse.setCode(400);
            commonResponse.setStatus(ResponseStatus.FAILED);
            commonResponse.setData(null);
            commonResponse.setErrorMessage(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResponse);
        }
        try{
            return ResponseEntity.status(HttpStatus.OK).body(cartService.updateCart(cartUniqueId,cartRequest));
        }catch (Exception e){
            log.error("** updateCartByUniqueId: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(setServerError(e));
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @DeleteMapping("/{cart-uniqueId}")
    public ResponseEntity<CommonResponse> deleteCartByUniqueId(@PathVariable("cart-uniqueId") String cartUniqueId){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(cartService.deleteCart(cartUniqueId));
        }catch (Exception e){
            log.error("** deleteCartByUniqueId: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(setServerError(e));
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @GetMapping("/")
    public ResponseEntity<CommonResponse> getAllCartByEmail(@RequestParam String email){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(cartService.getCartByEmail(email));
        }catch (Exception e){
            log.error("** getAllCartByEmail: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(setServerError(e));
        }
    }

    public CommonResponse setServerError(Exception e){
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(500);
        commonResponse.setStatus(ResponseStatus.FAILED);
        commonResponse.setData(e.getMessage());
        commonResponse.setErrorMessage(Constant.SERVER_ERROR_MESSAGE);
        return commonResponse;
    }


}
