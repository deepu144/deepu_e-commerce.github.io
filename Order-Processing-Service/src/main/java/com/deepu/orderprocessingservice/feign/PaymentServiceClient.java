package com.deepu.orderprocessingservice.feign;

import com.deepu.orderprocessingservice.config.FeignConfig;
import com.deepu.orderprocessingservice.request.PaymentInitiationRequest;
import com.deepu.orderprocessingservice.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "PAYMENT-SERVICE",path = "/stripe",configuration = FeignConfig.class)
public interface PaymentServiceClient {
    @PostMapping("/initiate")
    ResponseEntity<CommonResponse> initiatePayment(@RequestBody PaymentInitiationRequest request);
}
