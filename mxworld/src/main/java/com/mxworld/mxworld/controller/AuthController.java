package com.mxworld.mxworld.controller;

import com.mxworld.mxworld.model.RefreshToken;
import com.mxworld.mxworld.model.User;
import com.mxworld.mxworld.repository.OtpRepository;
import com.mxworld.mxworld.service.OtpService;
import com.mxworld.mxworld.service.RefreshTokenService;
import com.mxworld.mxworld.service.UserService;
import com.mxworld.mxworld.syntax.Otp.Otp;
import com.mxworld.mxworld.syntax.Otp.OtpResponse;
import com.mxworld.mxworld.syntax.Otp.OtpValidation;
import com.mxworld.mxworld.syntax.Otp.OtpValidationResponse;
import com.mxworld.mxworld.syntax.refreshToken.Token;
import com.mxworld.mxworld.syntax.users.Login;
import com.mxworld.mxworld.syntax.users.LoginResponse;
import com.mxworld.mxworld.syntax.users.SignUpResponse;
import com.mxworld.mxworld.syntax.users.Signup;
import com.mxworld.mxworld.utility.Jwt;
import com.mxworld.mxworld.utility.UserFuncion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.Map;

import java.util.Optional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication API", description = "Signup and Login endpoints")
@SecurityRequirement(name = "bearerAuth")
public class AuthController {

    private final OtpRepository otpRepository;

    private final Jwt jwtUtil;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final OtpService otpService;

    public AuthController(Jwt jwtUtil, UserService userService, RefreshTokenService refreshTokenService,
            OtpRepository otpRepository, OtpService otpService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.otpRepository = otpRepository;
        this.otpService = otpService;
    }

    @Operation(summary = "Register a new user")
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signup(@RequestBody Signup signupRequest) {
        try {
            String password = signupRequest.getPassword();
            String emString = signupRequest.getEmail();
            if (emString == null || password == null) {
                SignUpResponse response = new SignUpResponse(400,
                        "Invalid Payload");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (!UserFuncion.isValidEmail(emString)) {
                SignUpResponse response = new SignUpResponse(400,
                        "Please Enter Valid Email");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (!UserFuncion.isValidPassword(password)) {
                SignUpResponse response = new SignUpResponse(400,
                        "Password must be at least 8 characters long, contain one uppercase letter, one number, and one special character.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            // Convert DTO to entity
            User user = new User();
            user.setEmail(signupRequest.getEmail());
            user.setPassword(signupRequest.getPassword());

            // Register user
            userService.register(user);

            // Success response
            SignUpResponse response = new SignUpResponse(200, "User Created Successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            // For example, email already exists
            SignUpResponse response = new SignUpResponse(400, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            // Any other server error
            SignUpResponse response = new SignUpResponse(500, "Internal Server Error" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "Login a user")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody Login loginRequest) {
        try {
            String emString = loginRequest.getEmail();
            String password = loginRequest.getPassword();
            if (emString == null || password == null) {
                LoginResponse response = new LoginResponse(400,
                        "Invalid Payload", null, null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (!UserFuncion.isValidEmail(emString)) {
                LoginResponse response = new LoginResponse(400,
                        "Please Enter Valid Email", null, null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (!UserFuncion.isValidPassword(password)) {
                LoginResponse response = new LoginResponse(400,
                        "Password must be at least 8 characters long, contain one uppercase letter, one number, and one special character.",
                        null, null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            Optional<User> loggedInUser = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

            if (loggedInUser.isEmpty()) {
                LoginResponse response = new LoginResponse(404, "User Doesn't Exist", null, null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            User user = loggedInUser.get();

            if (!userService.checkPassword(user, loginRequest.getPassword())) {
                LoginResponse response = new LoginResponse(400, "Invalid Credentials", null, null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            String jwt = jwtUtil.generateToken(user.getEmail());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

            LoginResponse response = new LoginResponse(200, "Logged In Successfully", jwt, refreshToken.getToken());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

        } catch (Exception e) {
            LoginResponse response = new LoginResponse(500, "Internal Server Error" + e, null, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody Token tokenRequest, HttpServletRequest request) {
        String refreshToken = tokenRequest.getRefreshToken();

        if (refreshToken == null) {
            LoginResponse response = new LoginResponse(400, "Please Provide Token", null, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration) // returns RefreshToken or throws if expired
                .map(RefreshToken::getUser) // returns User
                .map(user -> {
                    // Generate new JWT and refresh token
                    String newJwt = jwtUtil.generateToken(user.getEmail());
                    RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

                    // Prepare response
                    LoginResponse response = new LoginResponse(200, "Token refreshed successfully", newJwt,
                            newRefreshToken.getToken());

                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
                }).orElseGet(() -> {
                    LoginResponse response = new LoginResponse(400, "Invalid refresh token", null, null);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                });

    }

    @PostMapping("/sendOtp")
    public ResponseEntity<OtpResponse> sendOtp(@RequestBody Otp otpRequest) {
        if (otpRequest.getEmail() == null) {
            OtpResponse response = new OtpResponse(400, "Invalid Payload", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        String emString = otpRequest.getEmail();
        if (!UserFuncion.isValidEmail(emString)) {
            OtpResponse response = new OtpResponse(400, "Please Enter Valid Email", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        try {
            String token = otpService.generateAndSend(otpRequest.getEmail());
            return ResponseEntity
                    .ok(new OtpResponse(200, "OTP has been sent to your email", token));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new OtpResponse(500, "Failed to send OTP: " + e.getMessage(), null));
        }
    }

    @PostMapping("/validateOtp")
    public ResponseEntity<OtpValidationResponse> validateOtp(@RequestBody OtpValidation otpValidation) {
        if (otpValidation.getOtp() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new OtpValidationResponse(400, "Invalid Payload"));
        }
        try {
            boolean value = otpService.validateOtp(otpValidation.getToken(), otpValidation.getOtp());
            System.out.println("Vlaue of validating :" + value);
            if (value) {
                return ResponseEntity
                        .status(HttpStatus.ACCEPTED).body(new OtpValidationResponse(200, "OTP has been verified"));
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_ACCEPTABLE).body(new OtpValidationResponse(400, "OTP has not been verified"));
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new OtpValidationResponse(500, "Failed to send OTP: " + e.getMessage()));
        }

    }

}
