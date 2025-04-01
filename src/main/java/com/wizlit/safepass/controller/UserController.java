package com.wizlit.safepass.controller;

import com.wizlit.safepass.common.dto.ApiResponse;
import com.wizlit.safepass.user.dto.UserResponseDto;
import com.wizlit.safepass.user.entity.User;
import com.wizlit.safepass.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> getCurrentUser(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.ok(ApiResponse.error("User not authenticated"));
        }

        String sub = principal.getAttribute("sub");
        String userId = "google_" + sub;
        
        Optional<User> userOptional = userRepository.findByUserId(userId);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserResponseDto userResponseDto = UserResponseDto.fromEntity(user);
            return ResponseEntity.ok(ApiResponse.success(userResponseDto, "User information retrieved successfully"));
        } else {
            return ResponseEntity.ok(ApiResponse.error("User not found in database"));
        }
    }
}
