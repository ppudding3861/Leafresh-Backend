package com.leafresh.backend.plant.controller;

import com.leafresh.backend.plant.model.PlantDTO;
import com.leafresh.backend.plant.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PlantController {

    @Autowired
    private PlantService plantService;

    @PostMapping("/add")
    public ResponseEntity<PlantDTO> addPlant(@ModelAttribute PlantDTO plantDTO) {
        try {
            PlantDTO createdPlant = plantService.savePlant(plantDTO);
            return new ResponseEntity<>(createdPlant, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/plants")
    public ResponseEntity<List<PlantDTO>> getAllPlants() {
        List<PlantDTO> plants = plantService.getAllPlants();
        return new ResponseEntity<>(plants, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlantDTO> getPlantById(@PathVariable Long id) {
        Optional<PlantDTO> plant = plantService.getPlantById(id);
        return plant.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlantDTO> updatePlant(@PathVariable Long id, @RequestBody PlantDTO plantDTO) {
        try {
            PlantDTO updatedPlant = plantService.updatePlant(id, plantDTO);
            return new ResponseEntity<>(updatedPlant, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlant(@PathVariable Long id) {
        plantService.deletePlant(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
