package com.leafresh.backend.plantcare.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class PlantCareEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer plantCareId;

	private int water;
	private int sunlight;
	private boolean ventilation;  // 통풍 체크: true/false
	private boolean cover;        // 가리기 체크: true/false
	private boolean nutrients;
	private Integer userId;

	// 생성날짜
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "plantCare_created_at", updatable = false)
	private Date plantCareCreatedAt = new Date();

	// 선택한 날짜
	@Temporal(TemporalType.DATE)
	@Column(name = "selected_date")
	private Date selectedDate;

	// 메모
	@Column(name = "memo",length = 300)
	private String memo;



	public PlantCareEntity() {
	}

	public PlantCareEntity(Integer plantCareId, int water, int sunlight, boolean ventilation, boolean cover,
		boolean nutrients, Integer userId, Date plantCareCreatedAt, Date selectedDate, String memo) {
		this.plantCareId = plantCareId;
		this.water = water;
		this.sunlight = sunlight;
		this.ventilation = ventilation;
		this.cover = cover;
		this.nutrients = nutrients;
		this.userId = userId;
		this.plantCareCreatedAt = plantCareCreatedAt;
		this.selectedDate = selectedDate;
		this.memo = memo;
	}

	public Integer getPlantCareId() {
		return plantCareId;
	}

	public void setPlantCareId(Integer plantCareId) {
		this.plantCareId = plantCareId;
	}

	public int getWater() {
		return water;
	}

	public void setWater(int water) {
		this.water = water;
	}

	public int getSunlight() {
		return sunlight;
	}

	public void setSunlight(int sunlight) {
		this.sunlight = sunlight;
	}

	public boolean isVentilation() {
		return ventilation;
	}

	public void setVentilation(boolean ventilation) {
		this.ventilation = ventilation;
	}

	public boolean isCover() {
		return cover;
	}

	public void setCover(boolean cover) {
		this.cover = cover;
	}

	public boolean isNutrients() {
		return nutrients;
	}

	public void setNutrients(boolean nutrients) {
		this.nutrients = nutrients;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getPlantCareCreatedAt() {
		return plantCareCreatedAt;
	}

	public void setPlantCareCreatedAt(Date plantCareCreatedAt) {
		this.plantCareCreatedAt = plantCareCreatedAt;
	}

	public Date getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}
