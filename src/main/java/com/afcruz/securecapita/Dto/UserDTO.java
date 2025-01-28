package com.afcruz.securecapita.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class UserDTO {
    private Long userId;
    @NotEmpty(message = "Field cannot be empty")
    private String firstName;
    @NotEmpty(message = "Field cannot be empty")
    private String lastName;
    @NotEmpty(message = "Field cannot be empty")
    @Email(message = "Invalid Email. Please enter a valid email address")
    private String email;
    private String password;
    private String address;
    private String phone;
    private String title;
    private String bio;
    private Boolean enabled;
    private Boolean isNonLocked;
    private Boolean isUsingMFA;
    private LocalDateTime createAt;
    private Timestamp updatedAt;
}
