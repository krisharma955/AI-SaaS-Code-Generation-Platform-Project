package com.K955.AI_SaaS_Code_Generation_Platform.Controllers;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.*;
import com.K955.AI_SaaS_Code_Generation_Platform.Security.JwtAuthUtil;
import com.K955.AI_SaaS_Code_Generation_Platform.Service.PaymentProcessor;
import com.K955.AI_SaaS_Code_Generation_Platform.Service.PlanService;
import com.K955.AI_SaaS_Code_Generation_Platform.Service.SubscriptionService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BillingController {

    private final PaymentProcessor paymentProcessor;
    private final SubscriptionService subscriptionService;
    private final PlanService planService;
    private final JwtAuthUtil jwtAuthUtil;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @GetMapping("/api/plans")
    public ResponseEntity<List<PlanResponse>> getAllPlans() {
        return ResponseEntity.ok(planService.getAllActivePlans());
    }

    @GetMapping("/api/me/subscription")
    public ResponseEntity<SubscriptionResponse> getMySubscription() {
        Long userId = 1L;
        return ResponseEntity.ok(subscriptionService.getCurrentSubscription(userId));
    }

    @PostMapping("/api/payments/checkout")
    public ResponseEntity<CheckoutResponse> createCheckoutResponse(@RequestBody @Valid CheckoutRequest request) {
        Long userId = jwtAuthUtil.getCurrentUserId();
        return ResponseEntity.ok(paymentProcessor.createCheckoutSessionUrl(request, userId));
    }

    @PostMapping("/api/payments/portal")
    public ResponseEntity<PortalResponse> openCustomerPortal() {
        Long userId = 1L;
        return ResponseEntity.ok(paymentProcessor.openCustomerPortal(userId));
    }

    @PostMapping("/webhooks/payment")
    public ResponseEntity<String> handlePaymentWebhooks(@RequestBody String payload,
                                                        @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
            StripeObject stripeObject = null;

            if(deserializer.getObject().isPresent()) {
                stripeObject = deserializer.getObject().get();
            } else {
                try {
                    stripeObject = deserializer.deserializeUnsafe();
                    if(stripeObject == null) {
                        log.warn("Failed to deserialize webhook object for event: {}", event.getType());
                        return ResponseEntity.ok().build();
                    }
                } catch (Exception e) {
                    log.error("Unsafe deserialization failed for event: {}: {}", event.getType(), e.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Deserialization failed");
                }
            }

            Map<String, String> metaData = new HashMap<>();
            if(stripeObject instanceof Session session) {
                metaData = session.getMetadata();
            }

            paymentProcessor.handleWebhookEvent(event.getType(), stripeObject, metaData);

            return ResponseEntity.ok().build();

        } catch (SignatureVerificationException e) {
            throw new RuntimeException(e);
        }
    }

}
