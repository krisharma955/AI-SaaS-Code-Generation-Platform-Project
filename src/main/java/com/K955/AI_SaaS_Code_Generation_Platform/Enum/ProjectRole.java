package com.K955.AI_SaaS_Code_Generation_Platform.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import static com.K955.AI_SaaS_Code_Generation_Platform.Enum.ProjectPermission.*;

@Getter
@RequiredArgsConstructor
public enum ProjectRole {

    OWNER(VIEW, EDIT, DELETE, MANAGE_MEMBERS, VIEW_MEMBERS),
    EDITOR(VIEW, EDIT, DELETE, VIEW_MEMBERS),
    VIEWER(VIEW, VIEW_MEMBERS);

    ProjectRole(ProjectPermission... permissions) {
        this.permissions = Set.of(permissions);
    }

    private final Set<ProjectPermission> permissions;
}
