package com.leafresh.backend.feed.repository;

import com.leafresh.backend.feed.model.entity.FeedEntity;
import com.leafresh.backend.feed.model.entity.FeedStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedRepository extends JpaRepository<FeedEntity, Integer> {

	// 상태가 DELETE_FEED가 아닌 피드들만 조회
	List<FeedEntity> findByFeedStatusNot(FeedStatus feedStatus);

	// 특정 ID로 상태가 DELETE_FEED가 아닌 피드 조회
	Optional<FeedEntity> findByFeedIdAndFeedStatusNot(Integer feedId, FeedStatus feedStatus);

	// 상태가 DELETE_FEED인 피드들만 조회
	List<FeedEntity> findByFeedStatus(FeedStatus feedStatus);
}
