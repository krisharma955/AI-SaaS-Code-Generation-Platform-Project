package com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription;

public record PlanLimitsResponse(
        String planName,
        int maxTokensPerDay,
        int maxProjects,
        boolean unlimitedAi
) {
}
