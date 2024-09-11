package com.leafresh.backend.plant.service;

import com.leafresh.backend.plant.model.GardenDiary;
import com.leafresh.backend.plant.model.GardenDiaryDTO;
import com.leafresh.backend.plant.repository.GardenDiaryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class GardenDiaryService {

    @Autowired
    private GardenDiaryRepository gardenDiaryRepository;

    // 식물 등록
    public GardenDiary createGardenDiary(GardenDiaryDTO gardenDiaryDTO) {
        GardenDiary gardenDiary = new GardenDiary();
        gardenDiary.setPlantName(gardenDiaryDTO.getPlantName());
        gardenDiary.setPlantType(gardenDiaryDTO.getPlantType());
        gardenDiary.setRegistrationDate(gardenDiaryDTO.getRegistrationDate());
        gardenDiary.setPlantDescription(gardenDiaryDTO.getPlantDescription());

        // 로그 추가
        System.out.println("Saving GardenDiary: " + gardenDiary);

        return gardenDiaryRepository.save(gardenDiary);
    }

    // 모든 식물 조회
    public List<GardenDiary> getAllDiaries() {
        return gardenDiaryRepository.findAll();
    }

    // 특정 ID로 식물 조회
    public Optional<GardenDiary> getDiaryById(Long id) {
        return gardenDiaryRepository.findById(id);
    }

    // 식물 정보 업데이트
    public GardenDiary updateGardenDiary(Long id, GardenDiaryDTO gardenDiaryDTO) {
        Optional<GardenDiary> optionalDiary = gardenDiaryRepository.findById(id);
        if (optionalDiary.isPresent()) {
            GardenDiary diaryToUpdate = optionalDiary.get();
            diaryToUpdate.setPlantName(gardenDiaryDTO.getPlantName());
            diaryToUpdate.setPlantType(gardenDiaryDTO.getPlantType());
            diaryToUpdate.setPlantDescription(gardenDiaryDTO.getPlantDescription());

            return gardenDiaryRepository.save(diaryToUpdate);
        }
        throw new IllegalArgumentException("해당 식물을 찾을 수 없습니다.");
    }

    // 식물 정보 삭제
    public void deleteGardenDiary(Long id) {
        gardenDiaryRepository.deleteById(id);
    }
}
