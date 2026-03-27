package com.K955.AI_SaaS_Code_Generation_Platform.Service.ImpL;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Member.InviteMemberRequest;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Member.MemberResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Member.UpdateMemberRoleRequest;
import com.K955.AI_SaaS_Code_Generation_Platform.Entity.ProjectMember;
import com.K955.AI_SaaS_Code_Generation_Platform.Service.ProjectMemberService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectMemberServiceImpL implements ProjectMemberService {
    @Override
    public List<ProjectMember> getProjectMembers(Long projectId, Long userId) {
        return List.of();
    }

    @Override
    public MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId) {
        return null;
    }

    @Override
    public MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request, Long userId) {
        return null;
    }

    @Override
    public MemberResponse deleteProjectMember(Long projectId, Long memberId, Long userId) {
        return null;
    }
}
