package com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Project;

import jakarta.validation.constraints.NotBlank;

public record ProjectRequest(

        @NotBlank
        String name
) {
}
