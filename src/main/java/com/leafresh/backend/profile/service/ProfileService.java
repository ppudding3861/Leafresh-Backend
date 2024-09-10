package com.leafresh.backend.profile.service;

import com.leafresh.backend.oauth.model.User;
import com.leafresh.backend.oauth.repository.UserRepository;
import com.leafresh.backend.profile.model.dto.ProfileDTO;
import com.leafresh.backend.profile.model.entity.ProfileEntity;
import com.leafresh.backend.profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    public void createProfile(Integer userId, ProfileDTO profileDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setUser(user);
        profileEntity.setProfileTitle(profileDTO.getProfileTitle());
        profileEntity.setProfileDescription(profileDTO.getProfileDescription());

        profileRepository.save(profileEntity);
    }

    public boolean checkProfileExists(Integer userId) {
        return profileRepository.existsByUserUserId(userId); // 수정된 메서드 이름 사용
    }

}
