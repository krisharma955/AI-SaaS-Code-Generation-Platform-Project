package com.K955.AI_SaaS_Code_Generation_Platform.Repository;

import com.K955.AI_SaaS_Code_Generation_Platform.Entity.Subscription;
import com.K955.AI_SaaS_Code_Generation_Platform.Enum.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    /*
    Get the current active subscription
     */
    Optional<Subscription> findByUserIdAndStatusIn(Long userId, Set<SubscriptionStatus> statusSet);

    Boolean existsByStripeSubscriptionId(String subscriptionId);

    Optional<Subscription> findByStripeSubscriptionId(String gatewaySubscriptionId);

}
