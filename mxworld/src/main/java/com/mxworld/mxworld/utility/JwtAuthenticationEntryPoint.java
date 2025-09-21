package com.mxworld.mxworld.utility;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // Set content type to JSON
        response.setContentType("application/json");
        // Set HTTP status to 401 Unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Prepare JSON response
        String json = "{"
                + "\"status\":401,"
                + "\"message\":\"Unauthorized or invalid JWT\""
                + "}";

        // Write JSON to response
        PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.flush();
        writer.close();
    }
}
