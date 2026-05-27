package com.company.lms.security;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.config.http.
SessionCreationPolicy;

import org.springframework.security.web.authentication.
UsernamePasswordAuthenticationFilter;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

	    http

	            .csrf(csrf -> csrf.disable())
	            
	            .exceptionHandling(ex -> ex
	                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
	            )

	            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

	            .authorizeHttpRequests(auth -> auth

	                    .requestMatchers(
	                            "/auth/**",
	                            "/employees/register",
	                            "/swagger-ui/**",
	                            "/v3/api-docs/**"
	                    ).permitAll()

	                    .requestMatchers("/manager/**")
	                    .hasAnyRole("MANAGER", "ADMIN")

	                    .requestMatchers("/admin/**")
	                    .hasRole("ADMIN")

	                    .anyRequest()
	                    .authenticated()
	            )

	            .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}
    
    
}