package com.delivery.storeadmin.config.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity  // Web Security 활성화
public class SecurityConfig {

    private List<String> SWAGGER = List.of(
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeHttpRequests(it -> {
                    it
                            // resource 요청은 모두 허용
                            .requestMatchers(
                                    PathRequest.toStaticResources().atCommonLocations()
                            ).permitAll()

                            // swagger 요청은 인증 없이 통과
                            .mvcMatchers(
                                    SWAGGER.toArray(new String[0])
                            ).permitAll()

                            // open-api/** 하위 모는 주소는 인증 없이 통과
                            .mvcMatchers("/open-api/**").permitAll()

                            // 나머지 요청은 인증 필요
                            .anyRequest().authenticated();
                })
                .formLogin(Customizer.withDefaults());

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // hash 로 암호화
        return new BCryptPasswordEncoder();
    }
}
