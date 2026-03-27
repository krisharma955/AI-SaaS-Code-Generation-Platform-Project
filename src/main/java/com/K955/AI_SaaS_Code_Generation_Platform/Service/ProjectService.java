package com.K955.AI_SaaS_Code_Generation_Platform.Service;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Project.ProjectRequest;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Project.ProjectResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Project.ProjectSummaryResponse;

import java.util.List;

public interface ProjectService {
    List<ProjectSummaryResponse> getUserProjects(Long userId);

    ProjectResponse getUserProjectById(Long projectId, Long userId);

    ProjectResponse createProject(ProjectRequest request, Long userId);

    ProjectResponse updateProject(Long projectId, ProjectRequest request, Long userId);

    void softDelete(Long projectId, Long userId);
}
