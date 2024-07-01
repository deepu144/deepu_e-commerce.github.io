package com.deepu.usermanagementservice.request;

import com.deepu.usermanagementservice.constant.Constant;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserObject {
    private UUID id;
    @NotNull(message = Constant.EMAIL_NOT_PROVIDED)
    @Size(min = 5)
    private String email;
    @NotNull(message = Constant.PASSWORD_NOT_PROVIDED)
    @Size(min = 8 , message = Constant.PASSWORD_SIZE_NOT_MATCH)
    private String password;
    @NotNull(message = Constant.PASSWORD_NOT_PROVIDED)
    @Size(min = 8 , message = Constant.PASSWORD_SIZE_NOT_MATCH)
    private String confirmPassword;
    private String role;
}
