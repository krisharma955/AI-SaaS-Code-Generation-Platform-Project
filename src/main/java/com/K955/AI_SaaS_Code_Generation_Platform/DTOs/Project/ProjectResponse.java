package com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Project;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Auth.UserProfileResponse;

import java.time.Instant;

public record ProjectResponse(
        Long id,
        String name,
        Instant createdAt,
        Instant updatedAt,
        UserProfileResponse owner
) {
}
