package com.leafresh.backend.feed.model.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "feed")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)  // toBuilder 옵션 추가
@Getter
public class FeedEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "feed_id")
	@Positive
	private Integer feedId;

	@Column(name = "feed_content")
	@NotNull(message = "피드내용는 null이 될 수 없습니다.")
	@Size(min = 20, max = 1000, message = "피드의 내용은 20자 이상, 1000자 이하여야 합니다.")
	private String feedContent;

	@Column(name = "feed_image")
	@NotNull(message = "피드이미지는 null이 될 수 없습니다.")
	private String feedImage;

	@Column(name = "feed_create_at")
	@CreationTimestamp // 생성시 시간을 자동으로 찍어줌
	private LocalDateTime feedCreateAt;

	@Column(name = "feed_update_at")
	@UpdateTimestamp // put으로 jpa가 작동될시 자동으로 시간을 찍어줌
	private LocalDateTime feedUpdateAt;

	@UpdateTimestamp
	@Column(name = "feed_delete_at")
	private LocalDateTime feedDeleteAt;

	@Enumerated(EnumType.STRING)
	@Column(name = "feed_status")
	private FeedStatus feedStatus = FeedStatus.FEED_ACTIVE;

	@Column(name = "user_id")
	@Positive
	private Integer userId;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "user_nickname")
	private String userNickname; // 추가된 필드: user_nickname

	@Column(name = "user_profile_img")
	private String userProfileImg;
}

