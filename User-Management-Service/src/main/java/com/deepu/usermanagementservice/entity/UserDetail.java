package com.deepu.usermanagementservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class UserDetail {
    @Id
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private Date dob;
    private int age;
    private long phoneNumber;
    @OneToMany(mappedBy = "userDetail", fetch = FetchType.EAGER)
    private List<Address> addresses = new ArrayList<>();
}
