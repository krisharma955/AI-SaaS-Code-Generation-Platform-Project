package com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(

        @Email
        @NotBlank
        String email,

        @Size(min = 1, max = 30)
        String name,

        @Size(min = 4, max = 50)
        String password
) {
}
