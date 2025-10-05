package com.mxworld.mxworld.utility;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        
        String requestURI = request.getRequestURI();
        System.out.println("AuthenticationEntryPoint triggered for: " + requestURI);
        
        // Check if this is a public endpoint - if yes, just return without error
        if (isPublicEndpoint(requestURI)) {
            System.out.println("Allowing public endpoint without authentication: " + requestURI);
            return; // Let the request continue to the controller
        }
        
        // Only send unauthorized response for protected endpoints
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"status\":401,\"message\":\"Unauthorized or invalid JWT\"}");
    }

    private boolean isPublicEndpoint(String requestURI) {
        return requestURI.startsWith("/swagger-ui") ||
               requestURI.startsWith("/v3/api-docs") ||
               requestURI.startsWith("/swagger-resources") ||
               requestURI.startsWith("/webjars") ||
               requestURI.startsWith("/mxworld/v1/auth") ||
               requestURI.startsWith("/mxworld/v1/attachment/files") ||
               requestURI.equals("/favicon.ico");
    }
}