package com.K955.AI_SaaS_Code_Generation_Platform.Service;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.CheckoutRequest;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.CheckoutResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.PortalResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.SubscriptionResponse;

public interface SubscriptionService {
    SubscriptionResponse getCurrentSubscription(Long userId);

    CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request, Long userId);

    PortalResponse openCustomerPortal(Long userId);
}
