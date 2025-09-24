package com.mxworld.mxworld.service;

import org.springframework.stereotype.Service;

import com.mxworld.mxworld.interfaces.ProfileInterface;
import com.mxworld.mxworld.model.Profile;
import com.mxworld.mxworld.model.User;
import com.mxworld.mxworld.repository.ProfileRepository;
import com.mxworld.mxworld.repository.UserRepository;
import com.mxworld.mxworld.syntax.ApiResponseDto;
import com.mxworld.mxworld.syntax.Profile.ProfileResponse;
import com.mxworld.mxworld.utility.Jwt;


@Service
public class ProfileService implements ProfileInterface {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final Jwt jwtUtil;

    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository, Jwt jwtUtil) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public ApiResponseDto<ProfileResponse> getProfileById(String token) {
        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        Profile profile = user.getProfile();
        if (profile == null) {
            throw new RuntimeException("Profile not found for user " + email);
        }

        ProfileResponse profileResponse = new ProfileResponse(
                user.getUsername(),
                user.getEmail(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getPhoneNumber(),
                profile.getProfileImageUrl(),
                profile.getDescription()
        );

        return new ApiResponseDto<>(200, "Profile fetched successfully" , profileResponse);

    }
}
