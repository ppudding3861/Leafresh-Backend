package com.leafresh.backend.feed.controller;

import com.leafresh.backend.feed.model.dto.FeedDTO;
import com.leafresh.backend.feed.service.FeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/feeds")
@Validated
public class FeedController {

	private final FeedService feedService;

	public FeedController(FeedService feedService) {
		this.feedService = feedService;
	}

	// 피드생성
	@PostMapping
	public ResponseEntity<FeedDTO> createFeed(@Valid @RequestBody FeedDTO feedDTO) {
		FeedDTO createdFeed = feedService.createFeed(feedDTO);
		return ResponseEntity.ok(createdFeed);
	}

	// 피드상세조회
	@GetMapping("/{id}")
	public ResponseEntity<FeedDTO> getFeedById(@PathVariable Integer id) {
		Optional<FeedDTO> feedDTO = feedService.getFeedById(id);
		return feedDTO.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	// 유저의 아이디 기반으로 조회
	@GetMapping("/id/{userId}")
	public ResponseEntity<List<FeedDTO>> getFeedsByUserId(@PathVariable Integer userId) {
		List<FeedDTO> feeds = feedService.getFeedsByUserId(userId);
		return ResponseEntity.ok(feeds);
	}

	// 유저 닉네임을 기반으로 피드 조회
	@GetMapping("/nickname/{userNickname}")
	public ResponseEntity<List<FeedDTO>> getFeedsByUsername(@PathVariable String userNickname) {
		List<FeedDTO> feeds = feedService.getFeedsByUsername(userNickname);
		if (feeds.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(feeds);
	}

	// 피드전체조회
	@GetMapping
	public ResponseEntity<List<FeedDTO>> getAllFeeds() {
		List<FeedDTO> feeds = feedService.getAllFeeds();
		return ResponseEntity.ok(feeds);
	}

	// 삭제된피드조회
	@GetMapping("/deleted")
	public ResponseEntity<List<FeedDTO>> getDeletedFeeds() {
		List<FeedDTO> feeds = feedService.getDeletedFeeds();
		return ResponseEntity.ok(feeds);
	}

	// 피드수정
	@PutMapping("/{id}")
	public ResponseEntity<FeedDTO> updateFeed(@PathVariable Integer id, @Valid @RequestBody FeedDTO feedDTO) {
		Optional<FeedDTO> existingFeed = feedService.getFeedById(id);
		if (existingFeed.isPresent()) {
			feedDTO = feedDTO.toBuilder().feedId(id).build();  // ID 설정
			FeedDTO updatedFeed = feedService.updateFeed(feedDTO);
			return ResponseEntity.ok(updatedFeed);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// 논리적피드삭제
	@PutMapping("/{id}/delete")
	public ResponseEntity<Void> deleteFeed(@PathVariable Integer id) {
		feedService.deleteFeed(id);  // 논리적 삭제
		return ResponseEntity.ok().build();
	}
}
