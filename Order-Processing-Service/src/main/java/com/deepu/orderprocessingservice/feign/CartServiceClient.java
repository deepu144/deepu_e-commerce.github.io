package com.deepu.orderprocessingservice.feign;

import com.deepu.orderprocessingservice.config.FeignConfig;
import com.deepu.orderprocessingservice.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "SHOPPING-CART-SERVICE",path = "/cart",configuration = FeignConfig.class)
public interface CartServiceClient {
    @GetMapping("/{cart-uniqueId}")
    ResponseEntity<CommonResponse> getProductFromCart(@PathVariable("cart-uniqueId") String cartUniqueId);
    @DeleteMapping("/{cart-uniqueId}")
    ResponseEntity<CommonResponse> deleteCartByUniqueId(@PathVariable("cart-uniqueId") String cartUniqueId);
}
