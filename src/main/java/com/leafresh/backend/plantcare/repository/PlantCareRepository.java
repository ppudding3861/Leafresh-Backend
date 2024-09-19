package com.leafresh.backend.plantcare.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leafresh.backend.plantcare.model.PlantCareEntity;

@Repository
public interface PlantCareRepository extends JpaRepository<PlantCareEntity, Integer> {

	Optional<PlantCareEntity> findBySelectedDateAndUserId(Date selectedDate, Integer userId);

	//userId 로 plantCareEntity 를 찾는다.
	// 그리고 그 결과를 List<PlantCareEntity 로 반환!
	List<PlantCareEntity> findAllByUserId(Integer userId);


	List<PlantCareEntity> findByUserIdAndSelectedDate(Integer userId, LocalDate selectedDate);



}
