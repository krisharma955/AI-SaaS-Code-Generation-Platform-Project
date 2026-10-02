package com.K955.AI_SaaS_Code_Generation_Platform.Service;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Project.FileContentResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Project.FileNode;

import java.util.List;

public interface ProjectFileService {
    List<FileNode> getFileTree(Long projectId);

    FileContentResponse getFileContent(Long projectId, String path);

    void saveFile(Long projectId, String filePath, String fileContent);
}
