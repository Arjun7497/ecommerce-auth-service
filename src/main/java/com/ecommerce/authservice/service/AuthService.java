package com.ecommerce.authservice.service;

import com.ecommerce.authservice.dto.AuthResponse;
import com.ecommerce.authservice.dto.LoginRequest;
import com.ecommerce.authservice.dto.RegisterRequest;

public interface AuthService {

    AuthResponse login(LoginRequest loginRequest);

    AuthResponse register(RegisterRequest registerRequest);

    AuthResponse refreshToken(String refreshToken);

    void logout(String refreshToken);

}
