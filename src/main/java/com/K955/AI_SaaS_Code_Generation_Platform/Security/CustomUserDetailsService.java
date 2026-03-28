package com.K955.AI_SaaS_Code_Generation_Platform.Security;

import com.K955.AI_SaaS_Code_Generation_Platform.Entity.User;
import com.K955.AI_SaaS_Code_Generation_Platform.Exception.ResourceNotFoundException;
import com.K955.AI_SaaS_Code_Generation_Platform.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(username, "User"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

    }

}
