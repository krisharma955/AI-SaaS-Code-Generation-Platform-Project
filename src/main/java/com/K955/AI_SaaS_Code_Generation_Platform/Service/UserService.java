package com.K955.AI_SaaS_Code_Generation_Platform.Service;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Auth.UserProfileResponse;

public interface UserService {
    UserProfileResponse getProfile(Long userId);
}
