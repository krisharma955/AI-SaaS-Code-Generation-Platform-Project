package com.K955.AI_SaaS_Code_Generation_Platform.Service.ImpL;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.SubscriptionResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.Entity.Plan;
import com.K955.AI_SaaS_Code_Generation_Platform.Entity.Subscription;
import com.K955.AI_SaaS_Code_Generation_Platform.Entity.User;
import com.K955.AI_SaaS_Code_Generation_Platform.Enum.SubscriptionStatus;
import com.K955.AI_SaaS_Code_Generation_Platform.Exception.ResourceNotFoundException;
import com.K955.AI_SaaS_Code_Generation_Platform.Mapper.SubscriptionMapper;
import com.K955.AI_SaaS_Code_Generation_Platform.Repository.PlanRepository;
import com.K955.AI_SaaS_Code_Generation_Platform.Repository.ProjectMemberRepository;
import com.K955.AI_SaaS_Code_Generation_Platform.Repository.SubscriptionRepository;
import com.K955.AI_SaaS_Code_Generation_Platform.Repository.UserRepository;
import com.K955.AI_SaaS_Code_Generation_Platform.Security.JwtAuthUtil;
import com.K955.AI_SaaS_Code_Generation_Platform.Service.SubscriptionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpL implements SubscriptionService {

    private final JwtAuthUtil jwtAuthUtil;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final ProjectMemberRepository projectMemberRepository;

    private final Integer FREE_TIER_PROJECTS_ALLOWED = 1;

    @Override
    public SubscriptionResponse getCurrentSubscription() {
        Long userId = jwtAuthUtil.getCurrentUserId();

        var currentSubscription =  subscriptionRepository.findByUserIdAndStatusIn(
                userId, Set.of(SubscriptionStatus.ACTIVE, SubscriptionStatus.PAST_DUE, SubscriptionStatus.TRAILING)
        ).orElse(new Subscription());

        return subscriptionMapper.toSubscriptionResponse(currentSubscription);
    }

    @Override
    public void activateSubscription(Long userId, Long planId, String subscriptionId, String customerId) {
        Boolean exists = subscriptionRepository.existsByStripeSubscriptionId(subscriptionId);
        if(exists) return; //if a subscription is already present with this subId, no need to do anything

        User user = getUser(userId);
        Plan plan = getPlan(planId);

        Subscription subscription = Subscription.builder()
                .user(user)
                .plan(plan)
                .stripeSubscriptionId(subscriptionId)
                .status(SubscriptionStatus.INCOMPLETE)
                .build(); //the subscription will be active after invoice paid call from renew subs

        subscriptionRepository.save(subscription);
    }

    @Override
    @Transactional
    public void updateSubscription(String gatewaySubscriptionId, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId) {
        Subscription subscription = getSubscription(gatewaySubscriptionId);

        boolean hasSubscriptionBeenUpdated = false;

         if(status != null && status != subscription.getStatus()) {
            subscription.setStatus(status);
            hasSubscriptionBeenUpdated = true;
         }

         if(periodStart != null && !periodStart.equals(subscription.getCurrentPeriodStart())) {
             subscription.setCurrentPeriodStart(periodStart);
             hasSubscriptionBeenUpdated = true;
         }

        if(periodEnd != null && !periodEnd.equals(subscription.getCurrentPeriodEnd())) {
            subscription.setCurrentPeriodStart(periodEnd);
            hasSubscriptionBeenUpdated = true;
        }

        if(cancelAtPeriodEnd != null && cancelAtPeriodEnd != subscription.getCancelAtPeriodEnd()) {
            subscription.setCancelAtPeriodEnd(cancelAtPeriodEnd);
            hasSubscriptionBeenUpdated = true;
        }

        if(planId != null && !planId.equals(subscription.getPlan().getId())) {
            Plan newPlan = getPlan(planId);
            subscription.setPlan(newPlan);
            hasSubscriptionBeenUpdated = true;
        }

        if(hasSubscriptionBeenUpdated) {
            log.debug("Subscription has been Updated: {}", gatewaySubscriptionId);
            subscriptionRepository.save(subscription);
        }

    }

    @Override
    public void cancelSubscription(String gatewaySubscriptionId) {
        Subscription subscription = getSubscription(gatewaySubscriptionId);
        subscription.setStatus(SubscriptionStatus.CANCELED);
        subscriptionRepository.save(subscription);
    }

    @Override
    public void renewSubscriptionPeriod(String gatewaySubscriptionId, Instant periodStart, Instant periodEnd) {
        Subscription subscription = getSubscription(gatewaySubscriptionId);

        Instant newStart = periodStart != null ? periodStart : subscription.getCurrentPeriodEnd();
        subscription.setCurrentPeriodStart(newStart);
        subscription.setCurrentPeriodEnd(periodEnd);

        if(subscription.getStatus() == SubscriptionStatus.PAST_DUE || subscription.getStatus() == SubscriptionStatus.INCOMPLETE) {
            subscription.setStatus(SubscriptionStatus.ACTIVE);
        }

        subscriptionRepository.save(subscription);
    }

    @Override
    public void markSubscriptionPastDue(String gatewaySubscriptionId) {
        Subscription subscription = getSubscription(gatewaySubscriptionId);

        if(subscription.getStatus() == SubscriptionStatus.PAST_DUE) {
            log.debug("Subscription is already PAST DUE, gatewaySubscriptionId: {}", gatewaySubscriptionId);
            return;
        }

        subscription.setStatus(SubscriptionStatus.PAST_DUE);
        subscriptionRepository.save(subscription);

        // Notify User via Email
    }

    @Override
    public boolean canCreateNewProject() {
        Long userId = jwtAuthUtil.getCurrentUserId();
        SubscriptionResponse currentSubscription = getCurrentSubscription();

        int countOfOwnedProjects = projectMemberRepository.countProjectOwnedByUser(userId);

        if(currentSubscription.plan() == null) {
            return countOfOwnedProjects < FREE_TIER_PROJECTS_ALLOWED;
        }

        return countOfOwnedProjects < currentSubscription.plan().maxProjects();
    }

    /// Utility Methods ///

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));
    }

    private Plan getPlan(Long planId) {
        return planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan", planId.toString()));
    }

    private Subscription getSubscription(String gatewaySubscriptionId) {
        return subscriptionRepository.findByStripeSubscriptionId(gatewaySubscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", gatewaySubscriptionId));
    }

}
