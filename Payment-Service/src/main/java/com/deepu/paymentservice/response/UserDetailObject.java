package com.deepu.paymentservice.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class UserDetailObject {
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private Date dob;
    private int age;
    private long phoneNumber;
    private UUID deliveryAddress;
    private List<UUID> addressIds = new ArrayList<>();
}
