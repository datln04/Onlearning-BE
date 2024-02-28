package fpt.fall23.onlearn.service.impl;

import fpt.fall23.onlearn.entity.Profile;
import fpt.fall23.onlearn.repository.ProfileRepository;
import fpt.fall23.onlearn.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    ProfileRepository profileRepository;


    @Override
    public Profile saveProfile(Profile profile) {
        return profileRepository.save(profile);
    }


    @Override
    public Profile getProfileByEmail(String email) {
        return profileRepository.getProfileByEmailIgnoreCase(email);
    }
}
