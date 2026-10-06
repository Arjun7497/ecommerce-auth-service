package com.ecommerce.authservice.service;

import com.ecommerce.authservice.dto.AuthResponse;
import com.ecommerce.authservice.dto.LoginRequest;
import com.ecommerce.authservice.dto.RegisterRequest;
import com.ecommerce.authservice.entity.Role;
import com.ecommerce.authservice.entity.User;
import com.ecommerce.authservice.exception.DuplicateResourceException;
import com.ecommerce.authservice.exception.InvalidCredentialsException;
import com.ecommerce.authservice.exception.ResourceNotFoundException;
import com.ecommerce.authservice.repository.RoleRepository;
import com.ecommerce.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return new AuthResponse("ACCESS_TOKEN_PLACEHOLDER", "REFRESH_TOKEN_PLACEHOLDER",
                "Login successful");
    }

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        Role userRole = roleRepository.findByName("USER").orElseThrow(() ->
                new ResourceNotFoundException("Default Role USER not found"));

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setFullName(registerRequest.getFullName());
        user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRoles(Set.of(userRole));

        userRepository.save(user);
        return new AuthResponse(null, null, "User registered successfully");
    }

}
