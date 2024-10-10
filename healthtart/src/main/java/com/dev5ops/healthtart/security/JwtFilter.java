package com.dev5ops.healthtart.security;

import com.dev5ops.healthtart.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
/* 설명. OncePerRequestFilter를 상속받아 doFilterInternal을 오버라이딩 한다.(한번만 실행되는 필터) */
public class JwtFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public JwtFilter(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    /* 설명. 들고 온(Request Header) 토큰이 유효한지 판별 및 인증(Authentication 객체로 관리) */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        log.info("JwtFilter processing request to {}", request.getRequestURI());

        log.info("UsernamePasswordAuthenticationFilter보다 먼저 동작하는 필터");
        String token = extractToken(request);
//        String authorizationHeader = request.getHeader("Authorization");
//        log.info("Authorization header: {}", authorizationHeader);
        log.info("jwt token값은 : {}", token);
//        /* 설명. JWT 토큰이 Request Header에 있는 경우(로그인 이후 요청일 경우) */
//        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            String token = authorizationHeader.substring(7);    // "Bearer "를 제외한 뒤에 토큰 부분만 추출
//            log.info("토큰 값: " + token);
//            if(jwtUtil.validateToken(token)) {
//                Authentication authentication = jwtUtil.getAuthentication(token);
//                log.info("JwtFilter를 통과한 유효한 토큰을 통해 security가 관리할 principal 객체: {}", authentication);
//                SecurityContextHolder.getContext().setAuthentication(authentication);   // 인증이 완료되었고 이후 필터 건너뜀
//            }else {
//                log.warn("Invalid token received");
//            }
//        }else {
//            log.info("No Bearer token found in the request");
//        }

        if (token != null) {
            log.info("Found token: {}", token);
            if (jwtUtil.validateToken(token)) {
                Authentication authentication = jwtUtil.getAuthentication(token);
                log.info("Valid token. Setting authentication: {}", authentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.warn("Invalid token received");
            }
        } else {
            log.info("No token found in the request");
        }

        /* 설명. 위의 if문으로 인증된 Authentication 객체가 principal 객체로 관리되지 않는다면 다음 필터 실행 */
        filterChain.doFilter(request, response);    // 실행 될 다음 필터는 UsernamePasswordAuthenticationFilter
    }

    private String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }

        // URL 파라미터에서 토큰 추출
        String tokenParam = request.getParameter("token");
        log.info("extractToken의 tokenParam: {}", tokenParam);
        if (tokenParam != null && !tokenParam.isEmpty()) {
            return tokenParam;
        }

        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/oauth2") ||
                path.startsWith("/login") ||
                path.equals("/users/login/google") ||
                path.equals("/users/login/kakao") ||
                isPublicEndpoint(path);
    }

    private boolean isPublicEndpoint(String path) {
        // 여기에 공개 엔드포인트 목록을 정의
        return path.equals("/api/public") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs");
    }
}