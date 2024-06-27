package com.deepu.notificationservice.controller;

import com.deepu.notificationservice.request.EmailDetailRequest;
import com.deepu.notificationservice.response.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface EmailController {

    @PostMapping("/send")
    ResponseEntity<CommonResponse> sendEmail(@RequestBody EmailDetailRequest emailDetailRequest);

}
