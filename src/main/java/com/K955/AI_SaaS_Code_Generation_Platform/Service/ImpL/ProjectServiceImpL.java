package com.K955.AI_SaaS_Code_Generation_Platform.Service.ImpL;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Project.ProjectRequest;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Project.ProjectResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Project.ProjectSummaryResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.Entity.Project;
import com.K955.AI_SaaS_Code_Generation_Platform.Entity.User;
import com.K955.AI_SaaS_Code_Generation_Platform.Exception.ResourceNotFoundException;
import com.K955.AI_SaaS_Code_Generation_Platform.Mapper.ProjectMapper;
import com.K955.AI_SaaS_Code_Generation_Platform.Repository.ProjectRepository;
import com.K955.AI_SaaS_Code_Generation_Platform.Repository.UserRepository;
import com.K955.AI_SaaS_Code_Generation_Platform.Service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpL implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;

    @Override
    public List<ProjectSummaryResponse> getUserProjects(Long userId) {
        List<Project> projectList = projectRepository.findAllAccessibleByUser(userId);
        return projectMapper.toListOfProjectSummaryResponse(projectList);
    }

    @Override
    public ProjectResponse getUserProjectById(Long projectId, Long userId) {
        Project project = projectRepository.findAccessibleProjectById(userId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException(projectId.toString(), "Project"));
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse createProject(ProjectRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId.toString(), "User"));

        Project project = Project.builder()
                .name(request.name())
                .owner(user)
                .build();

        Project saved = projectRepository.save(project);

        return projectMapper.toProjectResponse(saved);
    }

    @Override
    public ProjectResponse updateProject(Long projectId, ProjectRequest request, Long userId) {
        Project project = projectRepository.findAccessibleProjectById(userId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException(projectId.toString(), "Project"));

        if(request.name() != null) {
             project.setName(request.name());
        }

        Project saved = projectRepository.save(project);

        return projectMapper.toProjectResponse(saved);
    }

    @Override
    public void softDelete(Long projectId, Long userId) {
        Project project = projectRepository.findAccessibleProjectById(userId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException(projectId.toString(), "Project"));
        project.setDeletedAt(Instant.now());
        projectRepository.save(project);
    }
}
