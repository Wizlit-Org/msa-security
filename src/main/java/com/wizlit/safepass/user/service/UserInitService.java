package com.wizlit.safepass.user.service;

import com.wizlit.safepass.user.entity.Role;
import com.wizlit.safepass.user.entity.User;
import com.wizlit.safepass.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("local")
@RequiredArgsConstructor
public class UserInitService {

    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        // 테스트용 사용자 추가
        if (userRepository.findByUserId("google_123456789").isEmpty()) {
            User testUser = User.builder()
                    .userId("google_123456789")
                    .email("test@example.com")
                    .name("테스트 사용자")
                    .picture("https://example.com/profile.jpg")
                    .role(Role.USER)
                    .build();
                    
            userRepository.save(testUser);
            
            System.out.println("테스트 사용자가 성공적으로 추가되었습니다! ID: " + testUser.getId());
        }
    }
}
