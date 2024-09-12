package com.leafresh.backend.feed.service;

import com.leafresh.backend.feed.model.dto.FeedDTO;
import com.leafresh.backend.feed.model.entity.FeedEntity;
import com.leafresh.backend.feed.model.entity.FeedStatus;
import com.leafresh.backend.feed.repository.FeedRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FeedService {

	private final FeedRepository feedRepository;

	public FeedService(FeedRepository feedRepository) {
		this.feedRepository = feedRepository;
	}

	// 피드생성
	@Transactional
	public FeedDTO createFeed(FeedDTO feedDTO) {
		FeedEntity feedEntity = convertToEntity(feedDTO);
		FeedEntity savedEntity = feedRepository.save(feedEntity);
		return convertToDTO(savedEntity);
	}

	// 피드상세조회 - DELETE_FEED 상태 제외
	@Transactional
	public Optional<FeedDTO> getFeedById(Integer feedId) {
		Optional<FeedEntity> feedEntity = feedRepository.findByFeedIdAndFeedStatusNot(feedId, FeedStatus.FEED_DELETE);
		return feedEntity.map(this::convertToDTO);
	}

	// 사용자 ID 기반 피드 조회 - DELETE_FEED 상태 제외
	@Transactional
	public List<FeedDTO> getFeedsByUserId(Integer userId) {
		List<FeedEntity> feedEntities = feedRepository.findByUserIdAndFeedStatusNot(userId, FeedStatus.FEED_DELETE);
		return feedEntities.stream()
			.map(this::convertToDTO)
			.collect(Collectors.toList());
	}

	// 사용자 닉네임 기반 피드 조회 - DELETE_FEED 상태 제외
	@Transactional
	public List<FeedDTO> getFeedsByUsername(String userNickname) {
		List<FeedEntity> feedEntities = feedRepository.findByUserNicknameAndFeedStatusNot(userNickname, FeedStatus.FEED_DELETE);
		return feedEntities.stream()
			.map(this::convertToDTO)
			.collect(Collectors.toList());
	}

	// 피드전체조회 - DELETE_FEED 상태 제외
	@Transactional
	public List<FeedDTO> getAllFeeds() {
		List<FeedEntity> feedEntities = feedRepository.findByFeedStatusNot(FeedStatus.FEED_DELETE);
		return feedEntities.stream()
			.map(this::convertToDTO)
			.collect(Collectors.toList());
	}

	// 삭제된피드조회
	@Transactional
	public List<FeedDTO> getDeletedFeeds() {
		List<FeedEntity> feedEntities = feedRepository.findByFeedStatus(FeedStatus.FEED_DELETE);
		return feedEntities.stream()
			.map(this::convertToDTO)
			.collect(Collectors.toList());
	}

	// 피드 수정 메서드
	public FeedDTO updateFeed(FeedDTO feedDTO) {
		// 기존 피드 엔티티를 DB에서 조회
		Optional<FeedEntity> existingFeedOpt = feedRepository.findById(feedDTO.getFeedId());

		if (existingFeedOpt.isPresent()) {
			FeedEntity existingFeed = existingFeedOpt.get();

			// 기존 엔티티를 빌더 패턴으로 사용하여 필요한 필드만 수정
			FeedEntity updatedEntity = existingFeed.toBuilder()
				.feedContent(feedDTO.getFeedContent())
				.feedImage(feedDTO.getFeedImage())
				.build();

			// 엔티티 저장
			FeedEntity savedEntity = feedRepository.save(updatedEntity);
			return convertToDTO(savedEntity);
		} else {
			throw new EntityNotFoundException("피드를 찾을 수 없습니다.");
		}
	}


	// 논리적피드삭제
	@Transactional
	public void deleteFeed(Integer feedId) {
		Optional<FeedEntity> feedEntityOptional = feedRepository.findById(feedId);

		feedEntityOptional.ifPresent(feedEntity -> {
			FeedEntity updatedFeed = feedEntity.toBuilder()
				.feedStatus(FeedStatus.FEED_DELETE)  // 논리적 삭제 상태로 변경
				.build();

			feedRepository.save(updatedFeed);  // 새로운 객체 저장, Hibernate가 feedDeleteAt을 자동으로 설정
		});
	}

	// DTO -> Entity 변환 메서드
	private FeedEntity convertToEntity(FeedDTO feedDTO) {
		return FeedEntity.builder()
			.feedId(feedDTO.getFeedId())
			.feedContent(feedDTO.getFeedContent())
			.feedImage(feedDTO.getFeedImage())
			.feedCreateAt(feedDTO.getFeedCreateAt())
			.feedUpdateAt(feedDTO.getFeedUpdateAt())
			.feedDeleteAt(feedDTO.getFeedDeleteAt())
			.feedStatus(feedDTO.getFeedStatus())
			.userId(feedDTO.getUserId())
			.userName(feedDTO.getUserName())
			.userNickname(feedDTO.getUserNickname())
			.userProfileImg(feedDTO.getUserProfileImg())
			.build();
	}

	// Entity -> DTO 변환 메서드
	private FeedDTO convertToDTO(FeedEntity feedEntity) {
		return FeedDTO.builder()
			.feedId(feedEntity.getFeedId())
			.feedContent(feedEntity.getFeedContent())
			.feedImage(feedEntity.getFeedImage())
			.feedCreateAt(feedEntity.getFeedCreateAt())
			.feedUpdateAt(feedEntity.getFeedUpdateAt())
			.feedDeleteAt(feedEntity.getFeedDeleteAt())
			.feedStatus(feedEntity.getFeedStatus())
			.userId(feedEntity.getUserId())
			.userName(feedEntity.getUserName())
			.userNickname(feedEntity.getUserNickname())
			.userProfileImg(feedEntity.getUserProfileImg())
			.build();
	}
}
