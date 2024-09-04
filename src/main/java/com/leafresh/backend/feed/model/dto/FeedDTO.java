package com.leafresh.backend.feed.model.dto;

import java.time.LocalDateTime;

import com.leafresh.backend.feed.model.entity.FeedStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class FeedDTO {

	@NotNull(message = "피드id는 null이 될 수 없습니다.")
	Integer feedId;

	@NotNull(message = "피드내용는 null이 될 수 없습니다.")
	@Size(max = 100, message = "피드의 내용은 100자 이상이어야 합니다.")
	String feedContent;

	@NotNull(message = "피드이미지는 null이 될 수 없습니다.")
	String feedImage;

	@NotNull(message = "피드생성시간은 null이 될 수 없습니다.")
	LocalDateTime feedCreateAt;

	LocalDateTime feedUpdateAt;

	LocalDateTime feedDeleteAt;

	@NotNull(message = "피드상태는 null이 될 수 없습니다.")
	FeedStatus feedStatus;

	@NotNull(message = "유저id는 null이 될 수 없습니다.")
	Integer userId;
}
