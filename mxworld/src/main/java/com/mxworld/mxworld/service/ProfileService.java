package com.mxworld.mxworld.service;

import com.mxworld.mxworld.interfaces.ProfileInterface;
import com.mxworld.mxworld.model.Profile;
import com.mxworld.mxworld.repository.ProfileRepository;

public class ProfileService implements ProfileInterface {

    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Profile getProfileById (Long id) {
           return null;
    }
}
