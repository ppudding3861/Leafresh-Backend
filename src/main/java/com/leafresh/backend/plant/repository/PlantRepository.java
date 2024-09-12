package com.leafresh.backend.plant.repository;


import com.leafresh.backend.plant.model.PlantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantRepository extends JpaRepository<PlantEntity, Long> {
}
