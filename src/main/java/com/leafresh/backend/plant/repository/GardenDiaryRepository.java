package com.leafresh.backend.plant.repository;

import com.leafresh.backend.plant.model.GardenDiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GardenDiaryRepository extends JpaRepository<GardenDiary, Long> {
    // 필요한 경우, 커스텀 메서드를 추가할 수 있음
}
