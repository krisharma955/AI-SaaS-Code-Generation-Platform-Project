package com.K955.AI_SaaS_Code_Generation_Platform.Controllers;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.PlanLimitsResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.UsageTodayResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.Security.JwtAuthUtil;
import com.K955.AI_SaaS_Code_Generation_Platform.Service.UsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usage")
public class UsageController {

    private final UsageService usageService;
    private final JwtAuthUtil jwtAuthUtil;

    @GetMapping("/today")
    public ResponseEntity<UsageTodayResponse> getTodayUsage() {
        Long userId = jwtAuthUtil.getCurrentUserId();
        return ResponseEntity.ok(usageService.getTodayUsageOfUser(userId));
    }

    @GetMapping("/limits")
    public ResponseEntity<PlanLimitsResponse> getPlanLimits() {
        Long userId = jwtAuthUtil.getCurrentUserId();
        return ResponseEntity.ok(usageService.getCurrentSubscriptionLimitsOfUser(userId));
    }


}
