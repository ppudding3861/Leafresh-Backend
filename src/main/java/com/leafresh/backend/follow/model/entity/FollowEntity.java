package com.leafresh.backend.follow.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.leafresh.backend.oauth.model.User;
import jakarta.persistence.*;

@Entity
@Table(name = "follow")
public class FollowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    @JsonBackReference // 순환 참조 방지
    private User follower;

    @ManyToOne
    @JoinColumn(name = "following_id")
    @JsonManagedReference // 순환 참조 방지
    private User following;

    private FollowEntity(Builder builder) {
        this.follower = builder.follower;
        this.following = builder.following;
    }

    public FollowEntity() {}

    // Getter 메서드
    public Integer getId() {
        return id;
    }

    public User getFollower() {
        return follower;
    }

    public User getFollowing() {
        return following;
    }

    // Builder 클래스
    public static class Builder {
        private User follower;
        private User following;

        public Builder follower(User follower) {
            this.follower = follower;
            return this;
        }

        public Builder following(User following) {
            this.following = following;
            return this;
        }

        public FollowEntity build() {
            return new FollowEntity(this);
        }
    }
}
