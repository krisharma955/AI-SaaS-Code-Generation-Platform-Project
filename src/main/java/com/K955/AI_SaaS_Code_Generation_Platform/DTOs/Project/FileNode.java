package com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Project;

public record FileNode(
        String path
) {

    @Override
    public String toString() {
        return path;
    }

}
