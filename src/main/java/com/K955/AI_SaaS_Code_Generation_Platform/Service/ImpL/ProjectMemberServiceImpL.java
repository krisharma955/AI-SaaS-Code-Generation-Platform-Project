package com.K955.AI_SaaS_Code_Generation_Platform.Service.ImpL;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Member.InviteMemberRequest;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Member.MemberResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Member.UpdateMemberRoleRequest;
import com.K955.AI_SaaS_Code_Generation_Platform.Entity.Project;
import com.K955.AI_SaaS_Code_Generation_Platform.Entity.ProjectMember;
import com.K955.AI_SaaS_Code_Generation_Platform.Entity.ProjectMemberId;
import com.K955.AI_SaaS_Code_Generation_Platform.Entity.User;
import com.K955.AI_SaaS_Code_Generation_Platform.Exception.BadRequestException;
import com.K955.AI_SaaS_Code_Generation_Platform.Exception.ResourceNotFoundException;
import com.K955.AI_SaaS_Code_Generation_Platform.Mapper.ProjectMemberMapper;
import com.K955.AI_SaaS_Code_Generation_Platform.Repository.ProjectMemberRepository;
import com.K955.AI_SaaS_Code_Generation_Platform.Repository.ProjectRepository;
import com.K955.AI_SaaS_Code_Generation_Platform.Repository.UserRepository;
import com.K955.AI_SaaS_Code_Generation_Platform.Service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectMemberServiceImpL implements ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberMapper projectMemberMapper;

    @Override
    public List<MemberResponse> getProjectMembers(Long projectId, Long userId) {
        Project project = projectRepository.findAccessibleProjectById(userId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException(projectId.toString(), "Project"));

        List<MemberResponse> memberResponseList = new ArrayList<>();
        memberResponseList.add(projectMemberMapper.toMemberResponse(project.getOwner()));

        memberResponseList.addAll(
                projectMemberRepository.findByIdProjectId(projectId)
                        .stream()
                        .map(projectMemberMapper::toMemberResponseFromProjectMember)
                        .toList()
        );

        return memberResponseList;
    }

    @Override
    public MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId) {
        Project project = projectRepository.findAccessibleProjectById(userId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException(projectId.toString(), "Project"));

        if(!project.getOwner().getId().equals(userId)) {
            throw new BadRequestException("Only Owner can invite members");
        }

        User invitee = userRepository.findByUsername(request.email())
                .orElseThrow(() -> new ResourceNotFoundException(request.email(), "User"));

        if(invitee.getId().equals(userId)) {
            throw new BadRequestException("Cannot invite yourself");
        }

        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, invitee.getId());

        if(projectMemberRepository.existsById(projectMemberId)) {
            throw new BadRequestException("Invitee is already a member");
        }

        ProjectMember projectMember = ProjectMember.builder()
                .id(projectMemberId)
                .project(project)
                .user(invitee)
                .projectRole(request.role())
                .invitedAt(Instant.now())
                .build();

        projectMemberRepository.save(projectMember);

        return projectMemberMapper.toMemberResponseFromProjectMember(projectMember);
    }

    @Override
    public MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request, Long userId) {
        Project project = projectRepository.findAccessibleProjectById(userId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException(projectId.toString(), "Project"));

        if(!project.getOwner().getId().equals(userId)) {
            throw new BadRequestException("Only Owner can modify member roles");
        }

        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);

        ProjectMember projectMember = projectMemberRepository.findById(projectMemberId)
                        .orElseThrow(() -> new ResourceNotFoundException(projectMemberId.toString(), "Project Member"));

        projectMember.setProjectRole(request.role());

        ProjectMember saved = projectMemberRepository.save(projectMember);

        return projectMemberMapper.toMemberResponseFromProjectMember(saved);
    }

    @Override
    public void deleteProjectMember(Long projectId, Long memberId, Long userId) {
        Project project = projectRepository.findAccessibleProjectById(userId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException(projectId.toString(), "Project"));

        if(!project.getOwner().getId().equals(userId)) {
            throw new BadRequestException("Only Owner can modify member roles");
        }

        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);

        ProjectMember projectMember = projectMemberRepository.findById(projectMemberId)
                .orElseThrow(() -> new ResourceNotFoundException(projectMemberId.toString(), "Project Member"));

        projectMemberRepository.deleteById(projectMemberId);
    }
}
