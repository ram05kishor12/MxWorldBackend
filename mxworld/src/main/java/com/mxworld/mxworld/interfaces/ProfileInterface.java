package com.mxworld.mxworld.interfaces;

import com.mxworld.mxworld.syntax.ApiResponseDto;
import com.mxworld.mxworld.syntax.Profile.ProfileResponse;

public interface ProfileInterface {
    ApiResponseDto<ProfileResponse> getProfileById(String token);

    ApiResponseDto<?> updateProfile(String token, com.mxworld.mxworld.syntax.Profile.Profile profileRequest);

    // String saveBase64AsFile(String base64, String fileName) throws IOException;
}
