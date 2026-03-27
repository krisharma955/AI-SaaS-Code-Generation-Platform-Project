package com.K955.AI_SaaS_Code_Generation_Platform.Entity;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Plan {

    Long id;

    String name;

    String stripePriceId;

    Integer maxProjects;

    Integer maxTokensPerDay;

    Integer maxPreviews;

    Boolean unlimitedAi;

    Boolean active;

}
