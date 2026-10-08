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
import com.ecommerce.authservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;


    @Transactional
    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findWithRolesByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String role = user.getRoles().stream()
                .findFirst()
                .map(Role::getName)
                .orElse("USER");

        String accessToken = jwtService.generateAccessToken(user.getEmail(), role);
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        refreshTokenService.createRefreshToken(user, refreshToken);

        return new AuthResponse(accessToken, refreshToken, "Login successful");
    }

    @Transactional
    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new ResourceNotFoundException("Default role USER not found"));

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user.getEmail(), "USER");
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        refreshTokenService.createRefreshToken(user, refreshToken);

        return new AuthResponse(accessToken, refreshToken, "User registered successfully");
    }

    @Transactional
    @Override
    public AuthResponse refreshToken(String refreshToken) {
        refreshTokenService.validateRefreshToken(refreshToken);

        String email = jwtService.extractEmail(refreshToken);
        User user = userRepository.findWithRolesByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid token user"));

        String role = user.getRoles().stream()
                .findFirst()
                .map(Role::getName)
                .orElse("USER");
        String newAccessToken = jwtService.generateAccessToken(email, role);

        return new AuthResponse(newAccessToken, refreshToken, "Token refreshed successfully");
    }

    @Override
    public void logout(String refreshToken) {
        refreshTokenService.revokeToken(refreshToken);
    }

}
