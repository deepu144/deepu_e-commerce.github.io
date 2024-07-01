package com.deepu.orderprocessingservice.feign;

import com.deepu.orderprocessingservice.config.FeignConfig;
import com.deepu.orderprocessingservice.response.CommonResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "USER-MANAGEMENT-SERVICE",path = "/user",configuration = FeignConfig.class)
public interface UserServiceClient {
    @GetMapping("/")
    ResponseEntity<CommonResponse> getUserDetails();
}
