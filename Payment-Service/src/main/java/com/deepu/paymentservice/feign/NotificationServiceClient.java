package com.deepu.paymentservice.feign;

import com.deepu.paymentservice.request.EmailDetailRequest;
import com.deepu.paymentservice.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "NOTIFICATION-SERVICE",path = "/mail")
public interface NotificationServiceClient {
    @PostMapping("/send")
    ResponseEntity<CommonResponse> sendEmail(@RequestBody EmailDetailRequest emailDetailRequest);
}
