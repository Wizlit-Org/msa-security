package com.wizlit.safepass.user.dto;

import com.wizlit.safepass.user.entity.Role;
import com.wizlit.safepass.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private String picture;
    private Role role;

    public static UserResponseDto fromEntity(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .picture(user.getPicture())
                .role(user.getRole())
                .build();
    }
}
