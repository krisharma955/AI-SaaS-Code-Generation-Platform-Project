package com.K955.AI_SaaS_Code_Generation_Platform.Service;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Project.FileContentResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Project.FileNode;

import java.util.List;

public interface FileService {
    List<FileNode> getFileTree(Long projectId, Long userId);

    FileContentResponse getFileContent(Long projectId, String path, Long userId);
}
