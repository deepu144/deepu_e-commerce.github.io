package com.deepu.notificationservice.service;

import com.deepu.notificationservice.request.EmailDetailRequest;
import com.deepu.notificationservice.response.CommonResponse;

public interface EmailService {
    CommonResponse sendSimpleEmail(EmailDetailRequest emailDetailRequest);
}
