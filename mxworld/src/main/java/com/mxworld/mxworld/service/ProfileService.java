package com.mxworld.mxworld.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.mxworld.mxworld.interfaces.AttachmmentInterface;
import com.mxworld.mxworld.interfaces.ProfileInterface;
import com.mxworld.mxworld.model.Attachment;
import com.mxworld.mxworld.model.Profile;
import com.mxworld.mxworld.model.User;
import com.mxworld.mxworld.repository.AttachmentRepository;
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
    private final AttachmmentInterface attachmentInterface;

    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository, Jwt jwtUtil,
            AttachmmentInterface attachmentInterface) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.attachmentInterface = attachmentInterface;
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

        String id = profile.getProfileImageUrl();

        String url = attachmentInterface.getUri(id);

        ProfileResponse profileResponse = new ProfileResponse(
                user.getUsername(),
                user.getEmail(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getPhoneNumber(),
                url,
                profile.getDescription());

        return new ApiResponseDto<>(200, "Profile fetched successfully", profileResponse);

    }

    @Override
    public ApiResponseDto<?> updateProfile(String token, com.mxworld.mxworld.syntax.Profile.Profile profileRequest) {
        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        Profile existingProfile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        if (profileRequest.getImgBase64() != null) {
            ApiResponseDto<Map<String, String>> response = attachmentInterface
                    .uploadFile(profileRequest.getImgBase64());
            Map<String, String> data = (Map<String, String>) response.getData();

            String attachmentId = data.get("id");
            System.out.println(attachmentId);
            existingProfile.setProfileImageUrl(attachmentId);
        }

        user.setUsername(profileRequest.getUserName());
        existingProfile.setFirstName(profileRequest.getFirstName());
        existingProfile.setLastName(profileRequest.getLastName());
        existingProfile.setPhoneNumber(profileRequest.getPhoneNumber());
        existingProfile.setDescription(profileRequest.getDesription());

        userRepository.save(user);
        profileRepository.save(existingProfile);

        return new ApiResponseDto<Object>(200, "Profile Details Updated Successfully", null);
    }

}
