package com.deepu.orderprocessingservice.feign;

import com.deepu.orderprocessingservice.request.EmailDetailRequest;
import com.deepu.orderprocessingservice.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "NOTIFICATION-SERVICE",path = "/mail")
public interface NotificationServiceClient {
    @PostMapping("/send")
    ResponseEntity<CommonResponse> sendEmail(@RequestBody EmailDetailRequest emailDetailRequest);
}
