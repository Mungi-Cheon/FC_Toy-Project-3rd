package org.hack.travel.global.config.security;

import lombok.AllArgsConstructor;
import org.hack.travel.global.config.security.authentication.CustomAuthenticationEntryPoint;
import org.hack.travel.global.config.security.jwt.filter.CustomLogoutFilter;
import org.hack.travel.global.config.security.jwt.filter.JwtFilter;
import org.hack.travel.global.config.security.jwt.filter.LoginFilter;
import org.hack.travel.global.config.security.jwt.repository.RefreshRepository;
import org.hack.travel.global.config.security.jwt.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final RefreshRepository refreshRepository;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
        throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.formLogin(AbstractHttpConfigurer::disable);

        http.httpBasic(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests((auth) -> auth
            .requestMatchers("/", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
            .requestMatchers("/login", "/api/signup", "/reissue").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/**").hasRole("USER")
            .requestMatchers(HttpMethod.PUT, "/api/**").hasRole("USER")
            .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("USER")
            .anyRequest().authenticated());

        http.sessionManagement((session) -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.exceptionHandling(
            config -> config.authenticationEntryPoint(customAuthenticationEntryPoint));

        http.addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class);

        http.addFilterAt(
            new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil,
                refreshRepository),
            UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository),
            LogoutFilter.class);

        http.sessionManagement((session) -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}


