package com.leafresh.backend.plant.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "plants")
public class PlantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plant_name")
    private String plantName;

    @Column(name = "plant_type")
    private String plantType;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "plant_description")
    private String plantDescription;

    @Column(name = "image_url")
    private String imageUrl;

    public PlantEntity() {
    }

    public PlantEntity(Long id, String plantName, String plantType, LocalDate registrationDate, String plantDescription, String imageUrl) {
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
        return "PlantEntity{" +
                "id=" + id +
                ", plantName='" + plantName + '\'' +
                ", plantType='" + plantType + '\'' +
                ", registrationDate=" + registrationDate +
                ", plantDescription='" + plantDescription + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}