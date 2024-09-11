package com.leafresh.backend.plant.controller;

import com.leafresh.backend.plant.model.GardenDiary;
import com.leafresh.backend.plant.model.GardenDiaryDTO;
import com.leafresh.backend.plant.service.GardenDiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/garden-diary")
public class GardenDiaryController {

    @Autowired
    private GardenDiaryService gardenDiaryService;

    private static final String IMAGE_DIRECTORY = "uploaded_images";

    // 식물 등록
    @PostMapping("/add")
    public ResponseEntity<GardenDiary> createGardenDiary(
            @RequestParam("plantName") String plantName,
            @RequestParam("plantType") String plantType,
            @RequestParam("registrationDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate registrationDate,
            @RequestParam("plantDescription") String plantDescription,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        GardenDiaryDTO gardenDiaryDTO = new GardenDiaryDTO();
        gardenDiaryDTO.setPlantName(plantName);
        gardenDiaryDTO.setPlantType(plantType);
        gardenDiaryDTO.setRegistrationDate(registrationDate);
        gardenDiaryDTO.setPlantDescription(plantDescription);

        if (image != null && !image.isEmpty()) {
            try {
                // 이미지 저장 로직 추가
                String fileName = StringUtils.cleanPath(image.getOriginalFilename());
                Path path = Paths.get(IMAGE_DIRECTORY).resolve(fileName);
                Files.copy(image.getInputStream(), path);
                gardenDiaryDTO.setImageUrl(path.toString());
            } catch (IOException e) {
                e.printStackTrace(); // 로그 추가
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        GardenDiary createdDiary = gardenDiaryService.createGardenDiary(gardenDiaryDTO);
        return ResponseEntity.ok(createdDiary);
    }



    // 모든 식물 조회
    @GetMapping
    public ResponseEntity<List<GardenDiary>> getAllDiaries() {
        List<GardenDiary> diaries = gardenDiaryService.getAllDiaries();
        return ResponseEntity.ok(diaries);
    }

    // 특정 식물 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<GardenDiary> getDiaryById(@PathVariable Long id) {
        return gardenDiaryService.getDiaryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 식물 정보 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<GardenDiary> updateDiary(@PathVariable Long id, @RequestBody GardenDiaryDTO gardenDiaryDTO) {
        GardenDiary updatedDiary = gardenDiaryService.updateGardenDiary(id, gardenDiaryDTO);
        return ResponseEntity.ok(updatedDiary);
    }

    // 식물 정보 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiary(@PathVariable Long id) {
        gardenDiaryService.deleteGardenDiary(id);
        return ResponseEntity.noContent().build();
    }
}
