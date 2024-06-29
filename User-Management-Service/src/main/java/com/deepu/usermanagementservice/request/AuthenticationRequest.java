package com.deepu.usermanagementservice.request;

import com.deepu.usermanagementservice.constant.Constant;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthenticationRequest {
    @NotNull(message = Constant.EMAIL_NOT_PROVIDED)
    @Size(min = 5)
    private String email;
    @NotNull(message = Constant.PASSWORD_NOT_PROVIDED)
    @Size(min = 8 , message = Constant.PASSWORD_SIZE_NOT_MATCH)
    private String password;
}
