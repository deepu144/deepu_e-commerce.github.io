package com.deepu.shoppingcartservice.feign;

import com.deepu.shoppingcartservice.config.FeignConfig;
import com.deepu.shoppingcartservice.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "PRODUCT-CATLOG-SERVICE",path = "/product",configuration = FeignConfig.class)
public interface ProductServiceClient {
    @GetMapping("/{product-uniqueId}")
    ResponseEntity<CommonResponse> getProductByUniqueId(@PathVariable("product-uniqueId") String uniqueId);
}
