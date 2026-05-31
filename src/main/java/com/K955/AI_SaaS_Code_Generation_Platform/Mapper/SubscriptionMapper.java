package com.K955.AI_SaaS_Code_Generation_Platform.Mapper;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.PlanResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Subscription.SubscriptionResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.Entity.Plan;
import com.K955.AI_SaaS_Code_Generation_Platform.Entity.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    SubscriptionResponse toSubscriptionResponse(Subscription subscription);

    PlanResponse toPlanResponse(Plan plan);
}
