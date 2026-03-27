package com.K955.AI_SaaS_Code_Generation_Platform.Service;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Member.InviteMemberRequest;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Member.MemberResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Member.UpdateMemberRoleRequest;
import com.K955.AI_SaaS_Code_Generation_Platform.Entity.ProjectMember;

import java.util.List;

public interface ProjectMemberService {
    List<ProjectMember> getProjectMembers(Long projectId, Long userId);

    MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId);

    MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request, Long userId);

    MemberResponse deleteProjectMember(Long projectId, Long memberId, Long userId);
}
