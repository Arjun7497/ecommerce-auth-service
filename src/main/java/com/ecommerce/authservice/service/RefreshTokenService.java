package com.ecommerce.authservice.service;

import com.ecommerce.authservice.entity.RefreshToken;
import com.ecommerce.authservice.entity.User;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user, String tokenValue);
    RefreshToken validateRefreshToken(String token);
    void revokeToken(String token);
}
