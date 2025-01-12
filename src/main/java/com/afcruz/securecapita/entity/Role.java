package com.afcruz.securecapita.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Role {
    private Long roleId;
    @NotEmpty(message = "Field cannot be empty")
    private String name;
    @NotEmpty(message = "Field cannot be empty")
    private String permission;
}
