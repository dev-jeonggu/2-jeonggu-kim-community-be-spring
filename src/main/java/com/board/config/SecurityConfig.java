package com.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.board.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
                // NOTE : 회원가입 (POST 요청은 인증 없이 허용)
                .antMatchers(HttpMethod.POST, "/users/**").permitAll() 
                // NOTE : 수정, 삭제, 조회 (GET, PUT, DELETE 요청은 인증 필요)
                .antMatchers(HttpMethod.GET, "/users/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/users/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/users/**").authenticated()
                // NOTE : boards, notifications, comments는 인증 필요
                .antMatchers("/boards/**").authenticated()
                .antMatchers("/notifications/**").authenticated()
                .antMatchers("/comments/**").authenticated()
                // NOTE : 그 외 모든 요청은 인증 필요
                .anyRequest().authenticated()
            .and()
            .formLogin().disable();

        return http.build();
    }
}