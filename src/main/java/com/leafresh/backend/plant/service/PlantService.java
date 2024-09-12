package com.leafresh.backend.plant.service;


import com.leafresh.backend.plant.model.PlantDTO;
import com.leafresh.backend.plant.model.PlantEntity;
import com.leafresh.backend.plant.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PlantService {

    @Autowired
    private PlantRepository plantRepository;

    public PlantDTO savePlant(PlantDTO plantDTO) throws IOException {
        PlantEntity plantEntity = new PlantEntity();
        plantEntity.setPlantName(plantDTO.getPlantName());
        plantEntity.setPlantType(plantDTO.getPlantType());
        plantEntity.setRegistrationDate(plantDTO.getRegistrationDate());
        plantEntity.setPlantDescription(plantDTO.getPlantDescription());
        plantEntity.setImageUrl(plantDTO.getImageUrl());

        PlantEntity savedPlant = plantRepository.save(plantEntity);
        return convertToDTO(savedPlant);
    }

    public List<PlantDTO> getAllPlants() {
        return plantRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public Optional<PlantDTO> getPlantById(Long id) {
        return plantRepository.findById(id).map(this::convertToDTO);
    }

    public void deletePlant(Long id) {
        plantRepository.deleteById(id);
    }

    public PlantDTO updatePlant(Long id, PlantDTO plantDTO) throws IOException {
        PlantEntity plantEntity = plantRepository.findById(id).orElseThrow();
        plantEntity.setPlantName(plantDTO.getPlantName());
        plantEntity.setPlantType(plantDTO.getPlantType());
        plantEntity.setRegistrationDate(plantDTO.getRegistrationDate());
        plantEntity.setPlantDescription(plantDTO.getPlantDescription());
        plantEntity.setImageUrl(plantDTO.getImageUrl());

        PlantEntity updatedPlant = plantRepository.save(plantEntity);
        return convertToDTO(updatedPlant);
    }

    private PlantDTO convertToDTO(PlantEntity plantEntity) {
        PlantDTO dto = new PlantDTO();
        dto.setId(plantEntity.getId());
        dto.setPlantName(plantEntity.getPlantName());
        dto.setPlantType(plantEntity.getPlantType());
        dto.setRegistrationDate(plantEntity.getRegistrationDate());
        dto.setPlantDescription(plantEntity.getPlantDescription());
        dto.setImageUrl(plantEntity.getImageUrl());
        return dto;
    }
}
