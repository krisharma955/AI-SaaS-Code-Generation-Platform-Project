package com.K955.AI_SaaS_Code_Generation_Platform.Service.ImpL;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.CheckoutRequest;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.CheckoutResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.PortalResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.Entity.Plan;
import com.K955.AI_SaaS_Code_Generation_Platform.Entity.User;
import com.K955.AI_SaaS_Code_Generation_Platform.Exception.ResourceNotFoundException;
import com.K955.AI_SaaS_Code_Generation_Platform.Repository.PlanRepository;
import com.K955.AI_SaaS_Code_Generation_Platform.Repository.UserRepository;
import com.K955.AI_SaaS_Code_Generation_Platform.Service.PaymentProcessor;
import com.stripe.exception.StripeException;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StripePaymentProcessor implements PaymentProcessor {

    private final PlanRepository planRepository;
    private final UserRepository userRepository;

    @Value("${client.url}")
    private String frontendUrl;

    @Override
    public CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request, Long userId) {
        Plan plan = planRepository.findById(request.planId())
                .orElseThrow(() -> new ResourceNotFoundException(request.planId().toString(), "Plan"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId.toString(), "User"));

        var params = SessionCreateParams.builder()
                .addLineItem(
                        SessionCreateParams.LineItem.builder().setPrice(plan.getStripePriceId()).setQuantity(1L).build())
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSubscriptionData(
                        new SessionCreateParams.SubscriptionData.Builder()
                                .setBillingMode(SessionCreateParams.SubscriptionData.BillingMode.builder()
                                        .setType(SessionCreateParams.SubscriptionData.BillingMode.Type.FLEXIBLE)
                                        .build())
                                .build()
                )
                .setSuccessUrl(frontendUrl + "/success.html")
                .setCancelUrl(frontendUrl + "/cancel.html")
                .putMetadata("user_id", userId.toString())
                .putMetadata("plan_id", plan.getId().toString());

        try {
            String stripeCustomerId = user.getStripeCustomerId();
            if(stripeCustomerId == null || stripeCustomerId.isEmpty()) {
                params.setCustomerEmail(user.getUsername());
            } else {
                params.setCustomer(stripeCustomerId);
            }

            Session session = Session.create(params.build()); //making api call to stripe sdk
            return new CheckoutResponse(session.getUrl());
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PortalResponse openCustomerPortal(Long userId) {
        return null;
    }

    @Override
    public void handleWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metaData) {

        log.info("type");

    }

}
