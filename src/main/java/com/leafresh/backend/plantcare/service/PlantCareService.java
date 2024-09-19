package com.leafresh.backend.plantcare.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.leafresh.backend.plantcare.model.PlantCareDTO;
import com.leafresh.backend.plantcare.model.PlantCareEntity;
import com.leafresh.backend.plantcare.repository.PlantCareRepository;

@Service
public class PlantCareService {

	private PlantCareRepository plantCareRepository;

	@Autowired
	public PlantCareService(PlantCareRepository plantCareRepository) {
		this.plantCareRepository = plantCareRepository;
	}

	// 데이터 저장
	// 아이디랑 선택 날짜가 같으면 update, 아님 새롭게 저장!
	public PlantCareEntity saveOrUpdatePlantCare(PlantCareDTO plantCareDTO) {

		// 날짜를 입력하지 않으면 저장 안되게해야행
		if (plantCareDTO.getSelectedDate() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "날짜를 입력하세요!");
		}


		Optional<PlantCareEntity> existingRecord = plantCareRepository.findBySelectedDateAndUserId(
			plantCareDTO.getSelectedDate(), plantCareDTO.getUserId()
		);

		PlantCareEntity plantCareEntity;
		if (existingRecord.isPresent()) {
			plantCareEntity = existingRecord.get();
			plantCareEntity.setWater(plantCareDTO.getWater());
			plantCareEntity.setSunlight(plantCareDTO.getSunlight());
			plantCareEntity.setVentilation(plantCareDTO.isVentilation());
			plantCareEntity.setCover(plantCareDTO.isCover());
			plantCareEntity.setNutrients(plantCareDTO.isNutrients());

		}else {
			plantCareEntity = new PlantCareEntity();
			plantCareEntity.setWater(plantCareDTO.getWater());
			plantCareEntity.setSunlight(plantCareDTO.getSunlight());
			plantCareEntity.setVentilation(plantCareDTO.isVentilation());
			plantCareEntity.setCover(plantCareDTO.isCover());
			plantCareEntity.setNutrients(plantCareDTO.isNutrients());
			plantCareEntity.setUserId(plantCareDTO.getUserId());
			plantCareEntity.setSelectedDate(plantCareDTO.getSelectedDate());


		}

		return plantCareRepository.save(plantCareEntity);
	}

	public List<PlantCareEntity> getEventsByUserId(Integer userId) {

		return plantCareRepository.findAllByUserId(userId);
	}

	public List<PlantCareEntity> getAllEvents() {
		return plantCareRepository.findAll(); // 모든 PlantCareEntity 가져오기
	}

	public boolean deleteEventByUserIdAndDate(Integer userId, String eventDate) {
		// String 타입의 eventDate를 LocalDate로 변환
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(eventDate, formatter);  // String을 LocalDate로 변환

		// LocalDate 타입으로 검색
		List<PlantCareEntity> events = plantCareRepository.findByUserIdAndSelectedDate(userId, date);

		if (!events.isEmpty()) {
			plantCareRepository.deleteAll(events);
			return true;
		}
		return false;

	}
}

