package com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Auth;

public record AuthResponse(
        String token,
        UserProfileResponse user
) {
}
