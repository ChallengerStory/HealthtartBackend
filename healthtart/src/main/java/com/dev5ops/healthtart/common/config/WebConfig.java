package com.dev5ops.healthtart.common.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//                Ingress 적용 이후 CORS 불필요로 인한 경로 제거
                .allowedOrigins()
                .allowedMethods("GET", "POST", "PUT","PATCH", "DELETE");
    }
}
