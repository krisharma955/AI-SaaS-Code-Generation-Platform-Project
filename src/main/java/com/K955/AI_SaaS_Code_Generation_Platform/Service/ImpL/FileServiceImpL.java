package com.K955.AI_SaaS_Code_Generation_Platform.Service.ImpL;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Project.FileContentResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Project.FileNode;
import com.K955.AI_SaaS_Code_Generation_Platform.Service.FileService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpL implements FileService {
    @Override
    public List<FileNode> getFileTree(Long projectId, Long userId) {
        return List.of();
    }

    @Override
    public FileContentResponse getFileContent(Long projectId, String path, Long userId) {
        return null;
    }
}
