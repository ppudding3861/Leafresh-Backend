package com.leafresh.backend.plant.model;

import java.time.LocalDate;
import java.util.Arrays;

public class PlantDTO {

    private Long id;
    private String plantName;
    private String plantType;
    private LocalDate registrationDate;
    private String plantDescription;
    private String imageUrl;

    public PlantDTO() {
    }

    public PlantDTO(Long id, String plantName, String plantType, LocalDate registrationDate, String plantDescription, String imageUrl) {
        this.id = id;
        this.plantName = plantName;
        this.plantType = plantType;
        this.registrationDate = registrationDate;
        this.plantDescription = plantDescription;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getPlantType() {
        return plantType;
    }

    public void setPlantType(String plantType) {
        this.plantType = plantType;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getPlantDescription() {
        return plantDescription;
    }

    public void setPlantDescription(String plantDescription) {
        this.plantDescription = plantDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "PlantDTO{" +
                "id=" + id +
                ", plantName='" + plantName + '\'' +
                ", plantType='" + plantType + '\'' +
                ", registrationDate=" + registrationDate +
                ", plantDescription='" + plantDescription + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}