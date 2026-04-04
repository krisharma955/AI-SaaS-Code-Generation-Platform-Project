package com.K955.AI_SaaS_Code_Generation_Platform.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Plan {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    @Column(unique = true)
    String stripePriceId;

    Integer maxProjects;

    Integer maxTokensPerDay;

    Integer maxPreviews;

    Boolean unlimitedAi;

    Boolean active;

}
