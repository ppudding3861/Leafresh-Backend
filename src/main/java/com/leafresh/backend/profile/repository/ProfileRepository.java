package com.leafresh.backend.profile.repository;

import com.leafresh.backend.profile.model.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
    boolean existsByUserUserId(Integer userId); // 수정된 부분: existsByUserUserId
}
