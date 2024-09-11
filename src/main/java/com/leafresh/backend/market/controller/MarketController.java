package com.leafresh.backend.market.controller;

import com.leafresh.backend.market.model.dto.MarketDTO;
import com.leafresh.backend.market.model.entity.VisibleScope;
import com.leafresh.backend.market.service.MarketService;
import com.leafresh.backend.oauth.security.CurrentUser;
import com.leafresh.backend.oauth.security.UserPrincipal;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/market")
public class MarketController {
    private MarketService marketService;

    @Autowired
    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping
    public List<MarketDTO> getAllMarkets() {
        return marketService.allMarketList();
    }

    @PostMapping("/addpost")
    public ResponseEntity<?> create(
            @RequestParam("category") String category,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("image") String image,
            @RequestParam("visibleScope") VisibleScope visibleScope,
            @CurrentUser UserPrincipal userPrincipal){

        if (category == null || category.isEmpty()){
            return ResponseEntity.status(415).body("카테고리를 선택해주세요.");
        }
        if (title == null || title.isEmpty()){
            return ResponseEntity.status(415).body("제목을 입력해주세요.");
        }
        if (content == null || content.isEmpty()){
            return ResponseEntity.status(415).body("내용을 입력해주세요.");
        }
        if (image == null || image.isEmpty()){
            return ResponseEntity.status(415).body("사진을 등록해주세요.");
        }
        if (visibleScope == null || image.isEmpty()){
            return ResponseEntity.status(415).body("공개범위를 선택해주세요.");
        }

        MarketDTO marketDTO = new MarketDTO();
        marketDTO.setMarketCategory(category);
        marketDTO.setMarketTitle(title);
        marketDTO.setMarketContent(content);
        marketDTO.setMarketImage(image);
        marketDTO.setMarketVisibleScope(visibleScope);

        MarketDTO createdDTO = marketService.createPost(marketDTO, userPrincipal);

        if (createdDTO != null) { // 게시글이 잘 저장되었으면
            return ResponseEntity.ok(createdDTO);
        } else {
            return ResponseEntity.status(500).body("다시 시도해주세요");
        }
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();

        if(id <= 0 || id == null) {
            response.put("error", "다시 시도해주세요.");
            return ResponseEntity.status(400).body(response);
        }

        MarketDTO findDTO = marketService.detailPost(id);

        if (findDTO != null) {
            response.put("post", findDTO);
            return ResponseEntity.ok(response);
        }else {
            response.put("error", "게시글 조회 실패. 다시 시도해주세요.");
            return ResponseEntity.status(400).body(response);
        }
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<?> modify (
            @RequestParam("category") String category,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("image") String image,
            @RequestParam("visibleScope") VisibleScope visibleScope,
            @RequestParam("status") Boolean status,
            @PathVariable Integer id,
            @CurrentUser UserPrincipal userPrincipal){

        if (category == null || category.isEmpty()){
            return ResponseEntity.status(415).body("카테고리를 선택해주세요.");
        }
        if (title == null || title.isEmpty()){
            return ResponseEntity.status(415).body("제목을 입력해주세요.");
        }
        if (content == null || content.isEmpty()){
            return ResponseEntity.status(415).body("내용을 입력해주세요.");
        }
        if (image == null || image.isEmpty()){
            return ResponseEntity.status(415).body("사진을 등록해주세요.");
        }
        if (visibleScope == null || image.isEmpty()){
            return ResponseEntity.status(415).body("공개범위를 선택해주세요.");
        }

        MarketDTO marketDTO = new MarketDTO();
        marketDTO.setMarketCategory(category);
        marketDTO.setMarketTitle(title);
        marketDTO.setMarketContent(content);
        marketDTO.setMarketImage(image);
        marketDTO.setMarketVisibleScope(visibleScope);
        marketDTO.setMarketStatus(status);

        MarketDTO modifyDTO = marketService.modifyPost(marketDTO, userPrincipal, id);

        if (modifyDTO != null) { // 게시글이 잘 저장되었으면
            return ResponseEntity.ok(modifyDTO);
        } else {
            return ResponseEntity.status(500).body("다시 시도해주세요");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@CurrentUser UserPrincipal userPrincipal, @PathVariable Integer id){
        int result = marketService.deletePost(userPrincipal, id);

        if (result == 0) { // 데이터가 잘 삭제되었으면
            return ResponseEntity.status(200).body("게시글 삭제가 완료되었습니다.");
        } else {
            return ResponseEntity.status(500).body("다시 시도해주세요");
        }
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<?> updateStatus (@RequestBody Map<String, Boolean> requestBody, @PathVariable Integer id) {
        Boolean marketStatus = requestBody.get("status");

        try {
            marketService.updateMarketStatus(id, marketStatus);
            Map<String, String> response = new HashMap<>();
            response.put("message", "상태가 업데이트되었습니다.");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "게시글을 찾을 수 없습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "서버 오류가 발생했습니다."));
        }
    }
}
