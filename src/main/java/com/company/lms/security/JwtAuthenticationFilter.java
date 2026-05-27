package com.company.lms.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.
UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.
SecurityContextHolder;
import org.springframework.security.core.userdetails.
UserDetails;
import org.springframework.security.web.authentication.
WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.
OncePerRequestFilter;

import com.company.lms.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) throws ServletException, IOException {

    	String path = request.getServletPath();

    	if (path.startsWith("/auth")
    	        || path.startsWith("/swagger-ui")
    	        || path.startsWith("/v3/api-docs")) {

    	    filterChain.doFilter(request, response);
    	    return;
    	}
    	
        // Read Authorization header

        String authHeader = request.getHeader("Authorization");

        // No token present

        if (authHeader == null
                || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);

            return;
        }

        // Extract JWT token

        String token = authHeader.substring(7);

        String email = null;

        try {

            email = jwtService.extractEmail(token);

        } catch (Exception e) {

            filterChain.doFilter(request, response);
            return;
        }

        // Authenticate only if not already authenticated

        if (email != null
                && SecurityContextHolder
                .getContext()
                .getAuthentication() == null) {

            // Load user from database

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

            // Create authentication token

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            // Attach request details

            authToken.setDetails( new WebAuthenticationDetailsSource().buildDetails(request));

            // Set authentication

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // Continue request

        filterChain.doFilter(request, response);
    }
}