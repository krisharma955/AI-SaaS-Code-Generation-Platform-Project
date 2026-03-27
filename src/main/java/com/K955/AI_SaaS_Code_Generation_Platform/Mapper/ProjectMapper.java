package com.K955.AI_SaaS_Code_Generation_Platform.Mapper;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Project.ProjectResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Project.ProjectSummaryResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.Entity.Project;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectResponse toProjectResponse(Project project);

    List<ProjectSummaryResponse> toListOfProjectSummaryResponse(List<Project> projectList);

}
