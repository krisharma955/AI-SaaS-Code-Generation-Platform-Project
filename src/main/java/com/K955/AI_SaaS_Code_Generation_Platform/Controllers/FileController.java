package com.K955.AI_SaaS_Code_Generation_Platform.Controllers;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Project.FileContentResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Project.FileNode;
import com.K955.AI_SaaS_Code_Generation_Platform.Security.JwtAuthUtil;
import com.K955.AI_SaaS_Code_Generation_Platform.Service.ProjectFileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/files")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FileController {

    private final ProjectFileService projectFileService;
    private final JwtAuthUtil jwtAuthUtil;

    @GetMapping
    public ResponseEntity<List<FileNode>> getFileTree(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectFileService.getFileTree(projectId));
    }

    @GetMapping("/{*path}")
    public ResponseEntity<FileContentResponse> getFile(@PathVariable Long projectId, @PathVariable String path) {
        return ResponseEntity.ok(projectFileService.getFileContent(projectId, path));
    }

}
