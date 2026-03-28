package com.K955.AI_SaaS_Code_Generation_Platform.Entity;

import com.K955.AI_SaaS_Code_Generation_Platform.Enum.ProjectRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "project_members")
public class ProjectMember {

    @EmbeddedId //composite key
    ProjectMemberId id;

    @ManyToOne
    @MapsId("projectId")
    Project project;

    @ManyToOne
    @MapsId("userId") // we don't want a join table
    User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    ProjectRole projectRole;

    @CreationTimestamp
    Instant invitedAt;

    Instant acceptedAt;

}
