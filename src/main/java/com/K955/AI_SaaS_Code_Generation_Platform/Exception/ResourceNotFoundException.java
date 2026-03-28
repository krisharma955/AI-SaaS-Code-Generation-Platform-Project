package com.K955.AI_SaaS_Code_Generation_Platform.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResourceNotFoundException extends RuntimeException {
    private final String resourceId;
    private final String resourceName;
}
