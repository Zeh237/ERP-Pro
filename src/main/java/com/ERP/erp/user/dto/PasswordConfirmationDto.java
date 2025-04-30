package com.ERP.erp.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordConfirmationDto {
    @Size(min=8, max = 50)
    @NotBlank(message = "Password is required")
    private String password;
}
