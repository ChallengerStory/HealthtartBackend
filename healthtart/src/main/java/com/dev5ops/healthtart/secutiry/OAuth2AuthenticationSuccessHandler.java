package com.dev5ops.healthtart.secutiry;
import com.dev5ops.healthtart.user.domain.dto.JwtTokenDTO;
import org.springframework.security.oauth2.core.user.OAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    public OAuth2AuthenticationSuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String userCode = oAuth2User.getAttribute("userCode");
        String userEmail = oAuth2User.getAttribute("userEmail");
        String userName = oAuth2User.getAttribute("userName");


        // 사용자 권한 설정 (예: "ROLE_USER")
        List<String> roles = List.of("ROLE_MEMBER");

        // JWT 토큰에 들어갈 데이터(JwtTokenDTO)
        JwtTokenDTO tokenDTO = new JwtTokenDTO(userCode, userEmail, userName);

        // JWT 토큰 생성
        String token = jwtUtil.generateToken(tokenDTO, roles);

        // 응답 헤더에 토큰 추가
        response.addHeader("Authorization", "Bearer " + token);

        // 프론트엔드 리다이렉트 URL 설정 (필요에 따라 수정)
        getRedirectStrategy().sendRedirect(request, response, "/oauth2-redirect?token=" + token);
    }
}
