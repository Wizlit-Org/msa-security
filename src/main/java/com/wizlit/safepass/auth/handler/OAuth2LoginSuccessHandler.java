package com.wizlit.safepass.auth.handler;

import com.wizlit.safepass.auth.service.TokenService;
import com.wizlit.safepass.auth.model.TokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wizlit.safepass.common.dto.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        try {
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
            String email = oAuth2User.getAttribute("email");
            String sub = oAuth2User.getAttribute("sub");

            String userId = "google_" + sub;
            System.out.println("로그인 성공 - userId: " + userId);

            TokenResponse token = tokenService.createTokens(userId);
            System.out.println("토큰 발급 완료 - accessToken: " + token.getAccessToken());

            ApiResponse<TokenResponse> apiResponse = ApiResponse.success(token, "Login successful");
            
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
            response.flushBuffer();
        } catch (Exception e) {
            System.out.println("예외: " + e.getMessage());
            e.printStackTrace();
            
            ApiResponse<Void> errorResponse = ApiResponse.error("Authentication error: " + e.getMessage());
            
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            response.flushBuffer();
        }
    }
}
