package com.K955.AI_SaaS_Code_Generation_Platform.Service.ImpL;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.CheckoutRequest;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.CheckoutResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.PortalResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.Entity.Plan;
import com.K955.AI_SaaS_Code_Generation_Platform.Entity.User;
import com.K955.AI_SaaS_Code_Generation_Platform.Enum.SubscriptionStatus;
import com.K955.AI_SaaS_Code_Generation_Platform.Exception.ResourceNotFoundException;
import com.K955.AI_SaaS_Code_Generation_Platform.Repository.PlanRepository;
import com.K955.AI_SaaS_Code_Generation_Platform.Repository.UserRepository;
import com.K955.AI_SaaS_Code_Generation_Platform.Service.PaymentProcessor;
import com.K955.AI_SaaS_Code_Generation_Platform.Service.SubscriptionService;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StripePaymentProcessor implements PaymentProcessor {

    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;

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
    public PortalResponse openCustomerPortal() {
        return null;
    }

    @Override
    public void handleWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metaData) {
        log.debug("Handling Stripe Event: {}", type);

        switch (type) {
            case "checkout.session.completed" -> handleCheckoutSessionCompleted((Session) stripeObject, metaData); //one-time, on checkout completion
            case "customer.subscription.updated" -> handleCustomerSubscriptionUpdated((Subscription) stripeObject); //when user cancels, upgrades or any other updates
            case "customer.subscription.deleted" -> handleCustomerSubscriptionDeleted((Subscription) stripeObject); //when subscription ends - revoke the access
            case "invoice.paid" -> handleInvoicePaid((Invoice) stripeObject); //when invoice is paid
            case "invoice.payment_failed" -> handleInvoicePaymentFailed((Invoice) stripeObject); //when invoice is not paid, mark as PAST_DUE
            default -> log.debug("Ignoring the event: {}", type);
        }

    }

    private void handleCheckoutSessionCompleted(Session session, Map<String, String> metaData) {
        if(session == null) {
            log.error("Session Object was null");
            return;
        }

        Long userId = Long.parseLong(metaData.get("user_id"));
        Long planId = Long.parseLong(metaData.get("plan_id"));

        String subscriptionId = session.getSubscription();
        String customerId = session.getId(); //Stripe Customer id

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId.toString(), "User"));
        if(user.getStripeCustomerId() == null) {
            user.setStripeCustomerId(customerId);
            userRepository.save(user);
        }

        subscriptionService.activateSubscription(userId, planId, subscriptionId, customerId);
    }

    private void handleCustomerSubscriptionUpdated(Subscription subscription) {
        if(subscription == null) {
            log.error("Subscription Object was null inside handleCustomerSubscriptionUpdated");
            return;
        }

        SubscriptionStatus status = mapStripeStatusToEnum(subscription.getStatus()); //mapping stripe status to our Enums
        if(status == null) {
            log.warn("Unknown Status '{}' for subscription {}", subscription.getStatus(), subscription.getId());
            return;
        }

        SubscriptionItem item = subscription.getItems().getData().get(0);
        Instant periodStart = toInstant(item.getCurrentPeriodStart());
        Instant periodEnd = toInstant(item.getCurrentPeriodEnd());

        Long planId = resolvePlanId(item.getPrice()); //get the planId from the Price Object (Different Plans - Different Prices)

        subscriptionService.updateSubscription(
                subscription.getId(), status, periodStart, periodEnd,
                subscription.getCancelAtPeriodEnd(), planId
        ); //if user wants to cancel the subscription immediately, getCancelAtPeriodEnd - It will be True
    }

    private void handleCustomerSubscriptionDeleted(Subscription subscription) {
        if(subscription == null) {
            log.error("Subscription Object was null inside handleCustomerSubscriptionDeleted");
            return;
        }

        subscriptionService.cancelSubscription(subscription.getId());
    }

    private void handleInvoicePaid(Invoice invoice) {
        String subscriptionId = extractSubscriptionId(invoice);
        if(subscriptionId == null) return;

        try {
            Subscription subscription = Subscription.retrieve(subscriptionId); //sdk making api call to stripe server

            var item = subscription.getItems().getData().get(0);
            Instant periodStart = toInstant(item.getCurrentPeriodStart());
            Instant periodEnd = toInstant(item.getCurrentPeriodEnd());

            subscriptionService.renewSubscriptionPeriod(
                    subscriptionId, periodStart, periodEnd
            );
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

    }

    private void handleInvoicePaymentFailed(Invoice invoice) {
        String subscriptionId = extractSubscriptionId(invoice);
        if(subscriptionId == null) return;

        subscriptionService.markSubscriptionPastDue(subscriptionId);
    }

    /// Utility Methods ///

    private SubscriptionStatus mapStripeStatusToEnum(String status) { //small case - Stripe Status
        return switch (status) {
            case "active" -> SubscriptionStatus.ACTIVE;
            case "trailing" -> SubscriptionStatus.TRAILING;
            case "past_due" -> SubscriptionStatus.PAST_DUE;
            case "canceled" -> SubscriptionStatus.CANCELED;
            case "incomplete" -> SubscriptionStatus.INCOMPLETE;
            default -> {
                log.warn("Unmapped Stripe Status, {}", status);
                yield null;
            }
        };
    }

    private Instant toInstant(Long epoch) {
        return epoch != null ? Instant.ofEpochSecond(epoch) : null;
    }

    private Long resolvePlanId(Price price) {
        if(price == null || price.getId() == null) return null;
        return planRepository.findByStripePriceId(price.getId())
                .map(Plan::getId)
                .orElse(null);
    }

    private String extractSubscriptionId(Invoice invoice) { //extract Subscription from Invoice Object
        var parent = invoice.getParent();
        if(parent == null) return null;

        var subscriptionDetails = parent.getSubscriptionDetails();
        if(subscriptionDetails == null) return null;

        return subscriptionDetails.getSubscription();
    }

}
