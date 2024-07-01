package com.deepu.usermanagementservice.feign;

import com.deepu.usermanagementservice.response.CommonResponse;
import com.deepu.usermanagementservice.request.EmailDetailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "NOTIFICATION-SERVICE",path = "/mail")
public interface NotificationServiceClient {
    @PostMapping("/send")
    ResponseEntity<CommonResponse> sendEmail(@RequestBody EmailDetailRequest emailDetailRequest);
}
