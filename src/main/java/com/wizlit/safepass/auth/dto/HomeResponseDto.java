package com.wizlit.safepass.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeResponseDto {
    private String message;
    private String status;
    private String loginUrl;
}
