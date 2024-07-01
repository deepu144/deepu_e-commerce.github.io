package com.deepu.usermanagementservice.request;

import com.deepu.usermanagementservice.constant.Constant;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.Date;
import java.util.UUID;

@Data
public class UserDetailRequest {
    @NotNull(message = Constant.FIRST_NAME_NOT_NULL)
    private String firstName;
    @NotNull(message = Constant.LAST_NAME_NOT_NULL)
    private String lastName;
    @NotNull(message = Constant.GENDER_NOT_NULL)
    private String gender;
    @NotNull(message = Constant.DOB_NOT_NULL)
    private Date dob;
    @Min(value = 10,message = Constant.AGE_NOT_ENOUGH)
    private int age;
    @Min(value = 1000000000,message = Constant.INVALID_PHONE_NUMBER)
    private long phoneNumber;
    private UUID deliveryAddress;
}
