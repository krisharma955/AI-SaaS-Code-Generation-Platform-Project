package com.K955.AI_SaaS_Code_Generation_Platform.Service;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.PlanLimitsResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.UsageTodayResponse;

public interface UsageService {
    UsageTodayResponse getTodayUsageOfUser(Long userId);

    PlanLimitsResponse getCurrentSubscriptionLimitsOfUser(Long userId);
}
