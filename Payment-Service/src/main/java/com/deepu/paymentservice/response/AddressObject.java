package com.deepu.paymentservice.response;

import lombok.Data;
import java.util.UUID;

@Data
public class AddressObject {
    private UUID id;
    private String houseNo;
    private String area;
    private String city;
    private String state;
    private int pinCode;
    private String addressType;
}
