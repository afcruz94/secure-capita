package com.afcruz.securecapita.Dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
    private String title;
    private String bio;
    private Boolean enabled;
    private Boolean isNonLocked;
    private Boolean isUsingMFA;
    private LocalDateTime createAt;
}
