package com.wizlit.safepass.auth.filter;

import com.wizlit.safepass.user.entity.User;
import com.wizlit.safepass.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secretKey;
    
    private SecretKey key;
    
    @PostConstruct
    protected void init() {
        // 직접 바이트 배열에서 키 생성 (Base64 인코딩 단계 건너뜀)
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String token = getJwtFromRequest(request);
        
        if (StringUtils.hasText(token)) {
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
                
                String userId = claims.getSubject();
                
                Optional<User> userOptional = userRepository.findByUserId(userId);
                
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    
                    // OAuth2User 형태로 인증 정보 설정
                    Map<String, Object> attributes = new HashMap<>();
                    attributes.put("sub", userId.replace("google_", "")); // Google sub 값 추출
                    attributes.put("name", user.getName());
                    attributes.put("email", user.getEmail());
                    attributes.put("picture", user.getPicture());
                    
                    DefaultOAuth2User oauth2User = new DefaultOAuth2User(
                            Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                            attributes,
                            "sub"
                    );
                    
                    OAuth2AuthenticationToken authentication = new OAuth2AuthenticationToken(
                            oauth2User,
                            oauth2User.getAuthorities(),
                            "google"
                    );
                    
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                logger.error("Failed to set user authentication in security context", e);
            }
        }
        
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
