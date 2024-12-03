package com.stackoverflow.service;

import com.stackoverflow.model.Profile;

public interface ProfileService {
    void saveProfile(Profile profile);
    Profile getProfileById(long id);
}
