package com.deepu.paymentservice.feign;

import com.deepu.paymentservice.config.FeignConfig;
import com.deepu.paymentservice.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(value = "USER-MANAGEMENT-SERVICE",path = "/user",configuration = FeignConfig.class)
public interface UserServiceClient {
    @GetMapping("/address")
    ResponseEntity<CommonResponse> getAddressById(@RequestParam UUID addressUniqueId);
    @GetMapping("/")
    ResponseEntity<CommonResponse> getUserDetails();
}
