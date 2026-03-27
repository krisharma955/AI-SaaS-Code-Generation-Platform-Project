package com.K955.AI_SaaS_Code_Generation_Platform.Service.ImpL;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.PlanLimitsResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.UsageTodayResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.Service.UsageService;
import org.springframework.stereotype.Service;

@Service
public class UsageServiceImpL implements UsageService {
    @Override
    public UsageTodayResponse getTodayUsageOfUser(Long userId) {
        return null;
    }

    @Override
    public PlanLimitsResponse getCurrentSubscriptionLimitsOfUser(Long userId) {
        return null;
    }
}
