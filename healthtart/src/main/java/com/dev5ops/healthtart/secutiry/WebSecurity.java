package com.dev5ops.healthtart.secutiry;

import com.dev5ops.healthtart.user.service.CustomOAuth2UserService;
import com.dev5ops.healthtart.user.service.UserService;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private CustomOAuth2UserService customOAuth2UserService;
    private UserService userService;
    private Environment env;
    private JwtUtil jwtUtil;

    @Autowired
    public WebSecurity(BCryptPasswordEncoder bCryptPasswordEncoder, CustomOAuth2UserService customOAuth2UserService, UserService userService, Environment env, JwtUtil jwtUtil) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.customOAuth2UserService = customOAuth2UserService;
        this.userService = userService;
        this.env = env;
        this.jwtUtil = jwtUtil;
    }

    /* 설명. 인가(Authoriazation)용 메소드(인증 필터 추가) */
    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.csrf((csrf) -> csrf.disable());

        /* 로그인 시 추가할 authenticationManager */
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http.authorizeHttpRequests((authz) ->
                                authz
//                                        .requestMatchers(new AntPathRequestMatcher("/users/mypage", "GET")).hasRole("ADMIN")
                                        .requestMatchers(new AntPathRequestMatcher("/users/**", "POST")).permitAll()
                                        .requestMatchers(new AntPathRequestMatcher("/users/**", "GET")).permitAll()
                                        .requestMatchers(new AntPathRequestMatcher("/users/**", "PATCH")).permitAll()

                                        .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
                                        .requestMatchers(new AntPathRequestMatcher("/oauth2/**")).permitAll()
                                        .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                                        .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                                        .anyRequest().authenticated()
                )
                /* UserDetails를 상속받는 Service 계층 + BCrypt 암호화 */
                .authenticationManager(authenticationManager)

                /* OAuth2 로그인 설정 추가 */
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorization -> authorization
                                .baseUri("/oauth2/authorization"))
                        .redirectionEndpoint(redirection -> redirection
                                .baseUri("/login/oauth2/code/*"))
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(new OAuth2AuthenticationSuccessHandler(jwtUtil))
//                        .failureHandler(new OAuth2AuthenticationFailureHandler())
                )

                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilter(getAuthenticationFilter(authenticationManager));
        http.addFilterBefore(new JwtFilter(userService, jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /* Authentication 용 메소드(인증 필터 반환) */
    private Filter getAuthenticationFilter(AuthenticationManager authenticationManager) {
        return new AuthenticationFilter(authenticationManager, userService, env, jwtUtil);
    }
}