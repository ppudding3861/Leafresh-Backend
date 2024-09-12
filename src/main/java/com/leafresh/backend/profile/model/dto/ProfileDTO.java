package com.leafresh.backend.profile.model.dto;

public class ProfileDTO {
    private String profileTitle;
    private String profileDescription;

    public ProfileDTO() {
    }

    public ProfileDTO(String profileTitle, String profileDescription) {
        this.profileTitle = profileTitle;  // 필드 값을 초기화
        this.profileDescription = profileDescription;  // 필드 값을 초기화
    }

    // getters and setters
    public String getProfileTitle() {
        return profileTitle;
    }

    public void setProfileTitle(String profileTitle) {
        this.profileTitle = profileTitle;
    }

    public String getProfileDescription() {
        return profileDescription;
    }

    public void setProfileDescription(String profileDescription) {
        this.profileDescription = profileDescription;
    }
}
