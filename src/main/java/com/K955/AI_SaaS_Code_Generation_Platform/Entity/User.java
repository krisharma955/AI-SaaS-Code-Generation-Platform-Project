package com.K955.AI_SaaS_Code_Generation_Platform.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User {

    Long id;

    String email;

    String passwordHash;

    String name;

    String avatarUrl;

    Instant createdAt;

    Instant updatedAt;

    Instant deletedAt; //soft delete

}
