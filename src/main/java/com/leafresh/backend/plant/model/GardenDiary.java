package com.leafresh.backend.plant.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "garden_diary")
public class GardenDiary {

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

    @Column(name = "image_url") // 새로운 필드 추가
    private String imageUrl;

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

    // getters and setters...

    @Override
    public String toString() {
        return "GardenDiary{" +
                "id=" + id +
                ", plantName='" + plantName + '\'' +
                ", plantType='" + plantType + '\'' +
                ", registrationDate=" + registrationDate +
                ", plantDescription='" + plantDescription + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
