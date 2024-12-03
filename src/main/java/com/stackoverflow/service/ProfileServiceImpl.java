package com.stackoverflow.service;

import com.stackoverflow.model.Profile;
import com.stackoverflow.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService{

    @Autowired
    ProfileRepository profileRepository;

    @Override
    public void saveProfile(Profile profile) {
         profileRepository.save(profile);
    }

    @Override
    public Profile getProfileById(long id) {
        return profileRepository.findById(id).get();
    }
}
