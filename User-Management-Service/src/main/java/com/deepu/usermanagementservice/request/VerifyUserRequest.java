package com.deepu.usermanagementservice.request;

import com.deepu.usermanagementservice.constant.Constant;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VerifyUserRequest {
    @NotNull(message = Constant.EMAIL_NOT_PROVIDED)
    @Size(min = 5)
    private String email;
    @NotNull(message = Constant.OTP_NOT_PROVIDE)
    private String otp;
}
