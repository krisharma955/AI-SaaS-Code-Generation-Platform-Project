package com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription;

public record UsageTodayResponse(
        int tokensUsed,
        int tokensLimit,
        int previewsRunning,
        int previewsLimit
) {
}
