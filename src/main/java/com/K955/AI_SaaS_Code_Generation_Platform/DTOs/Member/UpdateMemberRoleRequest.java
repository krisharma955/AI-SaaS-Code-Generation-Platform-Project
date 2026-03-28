package com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Member;

import com.K955.AI_SaaS_Code_Generation_Platform.Enum.ProjectRole;
import jakarta.validation.constraints.NotNull;

public record UpdateMemberRoleRequest(

        @NotNull
        ProjectRole role
) {
}
