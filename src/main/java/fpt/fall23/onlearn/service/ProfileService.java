package fpt.fall23.onlearn.service;

import fpt.fall23.onlearn.entity.Profile;

public interface ProfileService {
    Profile saveProfile(Profile profile);

    Profile getProfileByEmail(String email);

}
