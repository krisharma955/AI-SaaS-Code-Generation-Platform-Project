package com.K955.AI_SaaS_Code_Generation_Platform.Service;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Auth.AuthResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Auth.LoginRequest;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Auth.SignupRequest;

public interface AuthService {
    AuthResponse signup(SignupRequest request);

    AuthResponse login(LoginRequest request);
}
