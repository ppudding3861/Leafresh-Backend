package com.leafresh.backend.plant.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leafresh.backend.plant.model.PlantDTO;
import com.leafresh.backend.plant.service.PlantService;

@RestController
@RequestMapping("/garden-diary/plant-care")
public class PlantInfoController {

	private PlantService plantService;

	@Autowired
	public PlantInfoController(PlantService plantService) {
		this.plantService = plantService;
	}

	@GetMapping("/info/{id}")
	public ResponseEntity<PlantDTO> getPlantById(@PathVariable Long id) {
		Optional<PlantDTO> plant = plantService.getPlantById(id);
		return plant.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}



}
