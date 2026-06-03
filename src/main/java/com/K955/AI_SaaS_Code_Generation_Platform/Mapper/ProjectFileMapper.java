package com.K955.AI_SaaS_Code_Generation_Platform.Mapper;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Project.FileNode;
import com.K955.AI_SaaS_Code_Generation_Platform.Entity.ProjectFile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectFileMapper {

    List<FileNode> toListOfFileNode(List<ProjectFile> projectFileList);

}
