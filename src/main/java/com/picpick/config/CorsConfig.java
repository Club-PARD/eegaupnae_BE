package com.picpick.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        // url 경로별 corsConfig 정책 등록 객체
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // corsConfig 정책 객체
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // 쿠키허용

        config.addAllowedOriginPattern("*"); // 모든 도메인 허용 (Credentials true일 경우 Pattern 사용)

        // 수정 후 (실제 도메인으로 제한)
        config.addAllowedOrigin("https://pickpick.store"); // 본인 도메인
        config.addAllowedOrigin("http://localhost:3000");  // 로컬 프론트엔드 테스트용

        config.addAllowedHeader("*"); // 모든 http header 허용

        config.addAllowedMethod("*"); // 모든 http method 허용

        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
