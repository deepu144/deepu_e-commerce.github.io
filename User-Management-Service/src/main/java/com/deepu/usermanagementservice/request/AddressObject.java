package com.deepu.usermanagementservice.request;

import com.deepu.usermanagementservice.constant.Constant;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class AddressObject {
    private UUID id;
    @NotNull(message = Constant.HOUSE_NUMBER_NOT_NULL)
    private String houseNo;
    @NotNull(message = Constant.AREA_NOT_NULL)
    private String area;
    @NotNull(message = Constant.CITY_NOT_NULL)
    private String city;
    @NotNull(message = Constant.STATE_NOT_NULL)
    private String state;
    @Min(value = 100000 , message = Constant.INVALID_PIN_CODE)
    private int pinCode;
    @NotNull(message = Constant.ADDRESS_TYPE_NOT_NULL)
    private String addressType;
}
