package me.meptael.linkro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // antMatchers("/test") 는 정확한 /test URL만
    // mvcMatchers("/test") 는 /test, /test/, /test.html, /test.xyz 등 다양하게 일치

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/", "/login", "/sign-up", "/login-form").permitAll()
                .mvcMatchers("/resources/**").permitAll()
                .mvcMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated();
        
        http.sessionManagement()
        	.maximumSessions(1)
        	.maxSessionsPreventsLogin(false);
        
        http.csrf().ignoringAntMatchers("/h2-console/**").disable();
        http.headers().frameOptions().sameOrigin();

        http.logout()
        		.logoutSuccessUrl("/");

        return http.build();
    }
}
