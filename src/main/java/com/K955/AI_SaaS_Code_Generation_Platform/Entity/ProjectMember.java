package com.K955.AI_SaaS_Code_Generation_Platform.Entity;

import com.K955.AI_SaaS_Code_Generation_Platform.Enum.ProjectRole;

import java.time.Instant;

public class ProjectMember {

    ProjectMemberId id;

    Project project;

    User user;

    ProjectRole projectRole;

    Instant invitedAt;

    Instant acceptedAt;

}
