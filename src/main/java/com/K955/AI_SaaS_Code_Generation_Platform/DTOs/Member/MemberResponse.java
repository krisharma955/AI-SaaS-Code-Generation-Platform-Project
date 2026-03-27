package com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Member;

import com.K955.AI_SaaS_Code_Generation_Platform.Enum.ProjectRole;

import java.time.Instant;

public record MemberResponse(
        Long userId,
        String email,
        String name,
        String avatarUrl,
        ProjectRole role,
        Instant invitedAt
) {
}
