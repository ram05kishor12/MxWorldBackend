package com.mxworld.mxworld.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mxworld.mxworld.model.Profile;
import com.mxworld.mxworld.model.User;
import com.mxworld.mxworld.repository.UserRepository;
import com.mxworld.mxworld.utility.Jwt;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/mxworld/profile")
@Tag(name = "Profile")
@SecurityRequirement(name = "bearerAuth")
@AllArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;
    private final Jwt jwtUtil;

    @GetMapping("/getProfileById")
    public ResponseEntity<?> getProfileById(HttpServletRequest request) {
        try {
             String header = request.getHeader("Authorization");
            String email = jwtUtil.extractEmail(header.substring(7)); 
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
            Profile profile = user.getProfile();
            if (profile == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Profile not found for user " + email);
            }

            return ResponseEntity.ok(profile);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid token");
        }
    }
}
