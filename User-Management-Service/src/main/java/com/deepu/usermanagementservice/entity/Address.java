package com.deepu.usermanagementservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;
import java.util.UUID;

@Data
@Entity
public class Address {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;
    private String houseNo;
    private String area;
    private String city;
    private String state;
    private String pinCode;
    private String addressType;
    @ManyToOne
    private UserDetail userDetail;
}
