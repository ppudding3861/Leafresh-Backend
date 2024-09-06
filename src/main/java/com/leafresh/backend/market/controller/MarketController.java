package com.leafresh.backend.market.controller;

import com.leafresh.backend.market.model.dto.MarketDTO;
import com.leafresh.backend.market.service.MarketService;
import com.leafresh.backend.oauth.security.CurrentUser;
import com.leafresh.backend.oauth.security.UserPrincipal;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
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

        MarketDTO marketDTO = new MarketDTO();
        marketDTO.setMarketCategory(category);
        marketDTO.setMarketTitle(title);
        marketDTO.setMarketContent(content);
        marketDTO.setMarketImage(image);

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
}
