package com.wizlit.safepass.auth.service;

import com.wizlit.safepass.auth.model.TokenResponse;
import com.wizlit.safepass.common.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtProvider jwtProvider;

    public TokenResponse createTokens(String userId) {
        String accessToken = jwtProvider.generateAccessToken(userId);
        String refreshToken = jwtProvider.generateRefreshToken(userId);

        // Refresh 토큰 저장은 나중에 Redis 연동 시 구현

        return new TokenResponse(accessToken, refreshToken);
    }
}
