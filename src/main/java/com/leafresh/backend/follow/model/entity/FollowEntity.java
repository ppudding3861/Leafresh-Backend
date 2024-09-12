package com.leafresh.backend.follow.model.entity;

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
    private User follower;

    @ManyToOne
    @JoinColumn(name = "following_id")
    private User following;

    // 생성자를 private으로 지정하여 외부에서 직접 접근하지 못하게 합니다.
    private FollowEntity(Builder builder) {
        this.follower = builder.follower;
        this.following = builder.following;
    }

    public FollowEntity() {

    }


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

        // build 메서드에서 FollowEntity 객체를 생성하여 반환
        public FollowEntity build() {
            return new FollowEntity(this);
        }
    }
}
