package com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Project;

import java.time.Instant;

public record ProjectSummaryResponse(
        Long id,
        String name,
        Instant createdAt,
        Instant updatedAt
) {
}
