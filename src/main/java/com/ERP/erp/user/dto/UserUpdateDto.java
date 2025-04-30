package com.ERP.erp.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDto {
    private Long id;

    private String username;

    @Size(min=8, max = 50, message = "First name cannot exceed 50 characters")
    private String password;

    @Email(message = "Invalid email format")
    private String email;

    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    private String department;
    private String jobTitle;
    private boolean enabled = true;
    private Long role;
}
