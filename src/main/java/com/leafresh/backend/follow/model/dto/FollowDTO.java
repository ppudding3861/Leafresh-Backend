package com.leafresh.backend.follow.model.dto;

public class FollowDTO {
    private String followingNickname;
    private boolean isFollowing;

    public FollowDTO() {}

    public FollowDTO(boolean isFollowing) {
        this.isFollowing = isFollowing;
    }

    public String getFollowingNickname() {
        return followingNickname;
    }

    public void setFollowingNickname(String followingNickname) {
        this.followingNickname = followingNickname;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }
}
