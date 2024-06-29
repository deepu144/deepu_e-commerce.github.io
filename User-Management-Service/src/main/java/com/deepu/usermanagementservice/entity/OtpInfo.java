package com.deepu.usermanagementservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class OtpInfo {
    @Id
    private String email;
    private String password;
    private String role;
    private String otp;
    private Long createdAt;
    private Long expireAt;
}
