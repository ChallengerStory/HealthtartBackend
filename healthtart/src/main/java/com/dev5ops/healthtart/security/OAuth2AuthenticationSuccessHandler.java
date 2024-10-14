package com.dev5ops.healthtart.security;

import com.dev5ops.healthtart.security.JwtUtil;
import com.dev5ops.healthtart.user.domain.dto.JwtTokenDTO;
import com.dev5ops.healthtart.user.domain.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    public OAuth2AuthenticationSuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Authentication Success");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.info("OAuth2User attributes: {}", oAuth2User.getAttributes());

        // OAuth2 제공자 정보 추출
        String provider = extractProvider(authentication);

        // User 객체 추출
        Object userObj = oAuth2User.getAttribute("user");
        if (!(userObj instanceof UserEntity)) {
            log.error("User object not found or invalid type in OAuth2User attributes");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invalid user data");
            return;
        }
        UserEntity user = (UserEntity) userObj;

        String userCode = user.getUserCode();
        String userEmail = user.getUserEmail();
        String userName = user.getUserName();

        log.info("userCode: {}", userCode);
        log.info("userEmail: {}", userEmail);
        log.info("userName: {}", userName);
        log.info("provider: {}", provider);

        // 사용자 권한 설정 (예: "ROLE_USER")
        List<String> roles = List.of("ROLE_MEMBER");

        // JWT 토큰에 들어갈 데이터(JwtTokenDTO)
        JwtTokenDTO tokenDTO = new JwtTokenDTO(userCode, userEmail, userName);

        // JWT 토큰 생성
        String token = jwtUtil.generateToken(tokenDTO, roles, provider);
        log.info("Generated JWT token: {}", token);

        // 응답 헤더에 토큰 추가
        response.addHeader("Authorization", "Bearer " + token);


        // websecurity에서 oauth2 로그인 설정 추가 부분에서 해주기 때문에 따로 구현할 필요가 없음.
//        // **Authentication 객체 생성 및 설정**
//        OAuth2AuthenticationToken oAuth2AuthenticationToken = new OAuth2AuthenticationToken(
//                oAuth2User,
//                oAuth2User.getAuthorities(),
//                ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId()
//        );
//
//        // **SecurityContext에 Authentication 객체 저장**
//        SecurityContextHolder.getContext().setAuthentication(oAuth2AuthenticationToken);



        String redirectUrl = String.format("/users/login/%s", provider.toLowerCase());
        redirectUrl = redirectUrl + "?token=" + token;
        log.info("Redirecting to: {}", redirectUrl);

        // 프론트엔드 리다이렉트 URL 설정 (필요에 따라 수정)
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private String extractProvider(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken) {
            return ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        }
        return "unknown";
    }
}
