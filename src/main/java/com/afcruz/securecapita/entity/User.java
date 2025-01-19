package com.afcruz.securecapita.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class User {
    private Long userId;
    @NotEmpty(message = "Field cannot be empty")
    private String firstName;
    @NotEmpty(message = "Field cannot be empty")
    private String lastName;
    @NotEmpty(message = "Field cannot be empty")
    @Email(message = "Invalid Email. Please enter a valid email address")
    private String email;
    @NotEmpty(message = "Field cannot be empty")
    private String password;
    private String address;
    private String phone;
    private String title;
    private String bio;
    private Boolean enabled;
    private Boolean isNonLocked;
    private Boolean isUsingMFA;
    private LocalDateTime createAt;
    @UpdateTimestamp
    private Timestamp updatedAt;
}
