package com.K955.AI_SaaS_Code_Generation_Platform.Repository;

import com.K955.AI_SaaS_Code_Generation_Platform.Entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    Optional<Plan> findByStripePriceId(String id);
}
