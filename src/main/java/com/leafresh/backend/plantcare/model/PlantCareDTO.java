package com.leafresh.backend.plantcare.model;

import java.util.Date;

import jakarta.validation.constraints.NotNull;

public class PlantCareDTO {

	private Integer plantCareid;
	private int water;
	private int sunlight;
	private boolean ventilation;
	private boolean cover;
	private boolean nutrients;
	private Integer userId;
	@NotNull(message = "날짜를 입력하세요!")
	private Date selectedDate;

	public PlantCareDTO() {
	}

	public PlantCareDTO(Integer plantCareid, int water, int sunlight, boolean ventilation, boolean cover,
		boolean nutrients,
		Integer userId, Date selectedDate) {
		this.plantCareid = plantCareid;
		this.water = water;
		this.sunlight = sunlight;
		this.ventilation = ventilation;
		this.cover = cover;
		this.nutrients = nutrients;
		this.userId = userId;
		this.selectedDate = selectedDate;
	}

	public Integer getPlantCareid() {
		return plantCareid;
	}

	public void setPlantCareid(Integer plantCareid) {
		this.plantCareid = plantCareid;
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

	public Date getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}

	@Override
	public String toString() {
		return "PlantCareDTO{" +
			"plantCareid=" + plantCareid +
			", water=" + water +
			", sunlight=" + sunlight +
			", ventilation=" + ventilation +
			", cover=" + cover +
			", nutrients=" + nutrients +
			", userId=" + userId +
			", selectedDate=" + selectedDate +
			'}';
	}
}
