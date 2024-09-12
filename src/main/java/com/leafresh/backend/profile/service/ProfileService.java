package com.leafresh.backend.profile.service;

import com.leafresh.backend.oauth.model.User;
import com.leafresh.backend.oauth.repository.UserRepository;
import com.leafresh.backend.profile.model.dto.ProfileDTO;
import com.leafresh.backend.profile.model.entity.ProfileEntity;
import com.leafresh.backend.profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    public ProfileDTO createProfile(Integer userId, ProfileDTO profileDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setUser(user);
        profileEntity.setProfileTitle(profileDTO.getProfileTitle());
        profileEntity.setProfileDescription(profileDTO.getProfileDescription());

        ProfileEntity savedProfile = profileRepository.save(profileEntity);

        ProfileDTO createdProfileDTO = new ProfileDTO();
        createdProfileDTO.setProfileTitle(savedProfile.getProfileTitle());
        createdProfileDTO.setProfileDescription(savedProfile.getProfileDescription());

        return createdProfileDTO;
    }

    public ProfileDTO getProfileInfo(Integer userId) {
        ProfileEntity profileEntity = profileRepository.findByUserUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("프로필이 존재하지 않습니다."));

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setProfileTitle(profileEntity.getProfileTitle());
        profileDTO.setProfileDescription(profileEntity.getProfileDescription());
        return profileDTO;
    }

    public ProfileDTO modifyProfile(Integer userId, ProfileDTO profileDTO) {
        ProfileEntity profileEntity = profileRepository.findByUserUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("프로필이 존재하지 않습니다."));

        profileEntity.setProfileTitle(profileDTO.getProfileTitle());
        profileEntity.setProfileDescription(profileDTO.getProfileDescription());

        ProfileEntity updatedProfile = profileRepository.save(profileEntity);

        ProfileDTO updatedProfileDTO = new ProfileDTO();
        updatedProfileDTO.setProfileTitle(updatedProfile.getProfileTitle());
        updatedProfileDTO.setProfileDescription(updatedProfile.getProfileDescription());

        return updatedProfileDTO;
    }

        public Optional<ProfileDTO> findProfileByUserId(Integer userId) {
            return profileRepository.findByUserUserId(userId)
                    .map(profile -> new ProfileDTO(profile.getProfileTitle(), profile.getProfileDescription()));
        }
    }


