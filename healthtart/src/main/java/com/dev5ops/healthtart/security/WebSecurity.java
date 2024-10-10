package com.dev5ops.healthtart.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    private final AuthenticationConfiguration authenticationConfiguration;

    public WebSecurity(AuthenticationConfiguration authenticationConfiguration) {
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{

        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // csrf 비활성화
        // jwt는 세션을 stateless 상태로 관리하므로 csrf 공격 방어할 필요 없음
        http.csrf((auth) -> auth.disable());

        // form 로그인 방식 disable (jwt 방식 로그인에서 불필요하므로)
        http.formLogin((auth) -> auth.disable());

        // http basic 인증 방식 disable (jwt 방식 로그인에서 불필요하므로)
        http.httpBasic((auth) -> auth.disable());

        // UserController에 대해 어떤 권한 가져야하는지에 대한 인가작업
        http.authorizeHttpRequests((auth) ->
                auth.requestMatchers(new AntPathRequestMatcher("/login", "POST")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/users/signUp", "POST")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/users/login", "POST")).permitAll()
                        .anyRequest().authenticated());

        http.addFilterAt(new AuthenticationFilter(authenticationManager(authenticationConfiguration)), UsernamePasswordAuthenticationFilter.class);

        // jwt Token 사용 시 session 방식 사용하지 않음 (stateless 상태)
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));



        return http.build();
    }

//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//    private UserService userService;
//    private Environment env;
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    public WebSecurity(BCryptPasswordEncoder bCryptPasswordEncoder
//            , UserService userService
//            , Environment env
//            , JwtUtil jwtUtil) {
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//        this.userService = userService;
//        this.env = env;
//        this.jwtUtil = jwtUtil;
//    }
//
//    /* 설명. 인가(Authoriazation)용 메소드(인증 필터 추가) */
//    @Bean
//    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
//
//        /* 설명. csrf 비활성화 */
//        http.csrf((csrf) -> csrf.disable());
//
//        /* 설명. 로그인 시 추가할 authenticationManager 만들기 */
//        AuthenticationManagerBuilder authenticationManagerBuilder =
//                http.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder.userDetailsService(userService)
//                .passwordEncoder(bCryptPasswordEncoder);
//
//        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
//
//        http.authorizeHttpRequests((authz) ->
//                        authz.requestMatchers(new AntPathRequestMatcher("/health", "GET")).permitAll()
////                                .requestMatchers(new AntPathRequestMatcher("/welcome", "GET")).permitAll()
////                                .requestMatchers(new AntPathRequestMatcher("/users/**", "POST")).permitAll()
////                                .requestMatchers(new AntPathRequestMatcher("/users/**", "GET")).hasRole("ENTERPRISE")
////                                .requestMatchers(new AntPathRequestMatcher("/actuator/**")).permitAll()
//                                .anyRequest().authenticated()
//                )
//                /* 설명. authenticationManager 등록(UserDetails를 상속받는 Service 계층 + BCrypt 암호화) */
//                .authenticationManager(authenticationManager)
//
//                /* 설명. session 방식을 사용하지 않음(JWT Token 방식 사용 시 설정할 내용) */
//                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//        http.addFilter(getAuthenticationFilter(authenticationManager));
//        http.addFilterBefore(new com.dev5ops.healthtart.security.JwtFilter(userService, jwtUtil), UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    /* 설명. 인증(Authentication)용 메소드(인증 필터 반환) */
//    private Filter getAuthenticationFilter(AuthenticationManager authenticationManager) {
//        return new AuthenticationFilter(authenticationManager, userService, env);
//    }
}
