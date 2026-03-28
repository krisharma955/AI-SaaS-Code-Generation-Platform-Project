package com.K955.AI_SaaS_Code_Generation_Platform.Service.ImpL;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Auth.AuthResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Auth.LoginRequest;
import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Auth.SignupRequest;
import com.K955.AI_SaaS_Code_Generation_Platform.Entity.User;
import com.K955.AI_SaaS_Code_Generation_Platform.Exception.BadRequestException;
import com.K955.AI_SaaS_Code_Generation_Platform.Exception.ResourceNotFoundException;
import com.K955.AI_SaaS_Code_Generation_Platform.Mapper.AuthMapper;
import com.K955.AI_SaaS_Code_Generation_Platform.Repository.UserRepository;
import com.K955.AI_SaaS_Code_Generation_Platform.Security.JwtAuthUtil;
import com.K955.AI_SaaS_Code_Generation_Platform.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpL implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthUtil jwtAuthUtil;
    private final AuthMapper authMapper;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse signup(SignupRequest request) {
        Boolean check = userRepository.existsByUsername(request.email());
        if(check) throw new BadRequestException("User with email: " +request.email()+ " already exists.");

        User user = User.builder()
                .name(request.name())
                .username(request.email())
                .password(request.password())
                .build();
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);

        String accessToken = jwtAuthUtil.generateAccessToken(user);

        return new AuthResponse(accessToken, authMapper.toUserProfileResponse(user));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid email or password");
        }

        User user = userRepository.findByUsername(request.email())
                .orElseThrow(() -> new ResourceNotFoundException(request.email(), "User"));

        String accessToken = jwtAuthUtil.generateAccessToken(user);

        return new AuthResponse(accessToken, authMapper.toUserProfileResponse(user));
    }
}
