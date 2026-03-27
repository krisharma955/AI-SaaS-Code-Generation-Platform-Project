package com.K955.AI_SaaS_Code_Generation_Platform.Service.ImpL;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.CheckoutRequest;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.CheckoutResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.PortalResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.SubscriptionResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.Service.SubscriptionService;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpL implements SubscriptionService {
    @Override
    public SubscriptionResponse getCurrentSubscription(Long userId) {
        return null;
    }

    @Override
    public CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request, Long userId) {
        return null;
    }

    @Override
    public PortalResponse openCustomerPortal(Long userId) {
        return null;
    }
}
