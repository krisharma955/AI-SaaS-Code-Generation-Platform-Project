package com.K955.AI_SaaS_Code_Generation_Platform.Service.ImpL;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Auth.AuthResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Auth.LoginRequest;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Auth.SignupRequest;
import com.K955.AI_SaaS_Code_Generation_Platform.Service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpL implements AuthService {
    @Override
    public AuthResponse signup(SignupRequest request) {
        return null;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        return null;
    }
}
