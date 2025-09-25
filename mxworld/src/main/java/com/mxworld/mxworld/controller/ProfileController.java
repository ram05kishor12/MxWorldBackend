package com.mxworld.mxworld.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mxworld.mxworld.interfaces.ProfileInterface;
import com.mxworld.mxworld.model.Profile;
import com.mxworld.mxworld.model.User;
import com.mxworld.mxworld.repository.UserRepository;
import com.mxworld.mxworld.service.ProfileService;
import com.mxworld.mxworld.syntax.ApiResponseDto;
import com.mxworld.mxworld.syntax.Profile.ProfileResponse;
import com.mxworld.mxworld.utility.Jwt;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/mxworld/profile")
@Tag(name = "Profile")
@SecurityRequirement(name = "bearerAuth")
@AllArgsConstructor
public class ProfileController {

    private final ProfileInterface profileInterface;

    @GetMapping("/getProfileById")
    public ResponseEntity<ApiResponseDto<ProfileResponse>> getProfileById(HttpServletRequest request) {
        try {
            String header = request.getHeader("Authorization");
            String token = header.substring(7);

            ApiResponseDto<ProfileResponse> response = profileInterface.getProfileById(token);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDto<>(404, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto<>(500, "Internal Server Error", null));
        }
    }

    @PutMapping("/updateProfile")
    public ResponseEntity<ApiResponseDto<?>> updateProfile(
            @RequestBody com.mxworld.mxworld.syntax.Profile.Profile profileRequest,
            HttpServletRequest request) {
        try {
            String header = request.getHeader("Authorization");
            String token = header.substring(7);
            ApiResponseDto<?> response = profileInterface.updateProfile(token, profileRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDto<>(404, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto<>(500, "Internal Server Error", null));
        }
    }

}
