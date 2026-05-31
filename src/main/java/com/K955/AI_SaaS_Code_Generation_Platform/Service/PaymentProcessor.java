package com.K955.AI_SaaS_Code_Generation_Platform.Service;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.CheckoutRequest;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.CheckoutResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.PortalResponse;
import com.stripe.model.StripeObject;

import java.util.Map;

public interface PaymentProcessor {

    CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request, Long userId);

    PortalResponse openCustomerPortal();

    void handleWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metaData);
}
