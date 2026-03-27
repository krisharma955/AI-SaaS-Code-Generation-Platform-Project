package com.K955.AI_SaaS_Code_Generation_Platform.Entity;

import com.K955.AI_SaaS_Code_Generation_Platform.Enum.SubscriptionStatus;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subscription {

    Long id;

    User user;

    Plan plan;

    SubscriptionStatus status;

    String stripeCustomerId;

    String stripeSubscriptionId;

    Instant currentPeriodStart;

    Instant currentPeriodEnd;

    Boolean cancelAtPeriodEnd = false;

    Instant createdAt;

    Instant updatedAt;

}
