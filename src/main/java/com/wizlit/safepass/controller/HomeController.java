package com.wizlit.safepass.controller;

import com.wizlit.safepass.auth.dto.HomeResponseDto;
import com.wizlit.safepass.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<ApiResponse<HomeResponseDto>> home() {
        HomeResponseDto homeResponse = HomeResponseDto.builder()
                .message("SafePass API")
                .status("running")
                .loginUrl("/oauth2/authorization/google")
                .build();
                
        return ResponseEntity.ok(ApiResponse.success(homeResponse));
    }
    
    @GetMapping("/login/oauth2/callback")
    public ResponseEntity<ApiResponse<String>> oauth2Callback() {
        return ResponseEntity.ok(
            ApiResponse.success("OAuth2 Callback - You should be redirected automatically")
        );
    }
}
