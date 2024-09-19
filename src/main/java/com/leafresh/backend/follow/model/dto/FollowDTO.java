package com.leafresh.backend.follow.model.dto;

public class FollowDTO {
    private String userNickname; // 유저 닉네임 필드
    private String imageUrl; // 이미지 URL 필드
    private boolean isFollowing; // 팔로잉 여부 필드

    public FollowDTO() {}

    public FollowDTO(String userNickname, String imageUrl, boolean isFollowing) {
        this.userNickname = userNickname;
        this.imageUrl = imageUrl;
        this.isFollowing = isFollowing;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean isFollowing) {
        this.isFollowing = isFollowing;
    }
}

