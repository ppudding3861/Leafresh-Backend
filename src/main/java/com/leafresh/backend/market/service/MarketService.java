package com.leafresh.backend.market.service;

import com.leafresh.backend.market.model.dto.MarketDTO;
import com.leafresh.backend.market.model.entity.MarketEntity;
import com.leafresh.backend.market.repository.MarketRepository;
import com.leafresh.backend.oauth.model.User;
import com.leafresh.backend.oauth.repository.UserRepository;
import com.leafresh.backend.oauth.security.CurrentUser;
import com.leafresh.backend.oauth.security.UserPrincipal;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MarketService {
    private MarketRepository marketRepository;
    private UserRepository userRepository;

    @Autowired
    public MarketService(MarketRepository marketRepository, UserRepository userRepository) {
        this.marketRepository = marketRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<MarketDTO> allMarketList() {
        List<MarketEntity> entities = marketRepository.findAll();
        List<MarketDTO> marketList = new ArrayList<>();
        for (MarketEntity entity : entities) {
            MarketDTO dto = new MarketDTO();
            dto.setMarketId(entity.getMarketId());
            dto.setMarketCategory(entity.getMarketCategory());
            dto.setMarketTitle(entity.getMarketTitle());
            dto.setMarketContent(entity.getMarketContent());
            dto.setMarketImage(entity.getMarketImage());
            dto.setMarketStatus(entity.isMarketStatus());
            dto.setMarketCreatedAt(entity.getMarketCreatedAt());
            marketList.add(dto);
        }
        return marketList;
    }

    @Transactional
    public MarketDTO createPost(MarketDTO marketDTO, @CurrentUser UserPrincipal userPrincipal) {
        MarketEntity entity = new MarketEntity();
        Integer userId = userPrincipal.getUserId(); // 인증된 유저의 고유한 id 값
        Optional<User> user = userRepository.findById(userId); // id값을 기준으로 user 객체를 꺼내옴

        if (user != null) { // 유저가 존재하면
            String userNickname = user.get().getUserNickname(); // 유저의 닉네임을 가져옴

            entity.setMarketCategory(marketDTO.getMarketCategory());
            entity.setMarketTitle(marketDTO.getMarketTitle());
            entity.setMarketContent(marketDTO.getMarketContent());
            entity.setMarketImage(marketDTO.getMarketImage());
            entity.setMarketCreatedAt(marketDTO.getMarketCreatedAt());
            entity.setMarketStatus(true); // 글이 삭제되었을시에 상태는 false
            entity.setUserNickname(userNickname); // oauth의 User 테이블에서 userNickname을 가져와서 넣어줘야함.
            marketRepository.save(entity);
            System.out.println(entity); // 잘 저장되었나 출력

            MarketDTO savedDTO = new MarketDTO();
            savedDTO.setMarketId(entity.getMarketId());
            savedDTO.setMarketCategory(entity.getMarketCategory());
            savedDTO.setMarketTitle(entity.getMarketTitle());
            savedDTO.setMarketContent(entity.getMarketContent());
            savedDTO.setMarketImage(entity.getMarketImage());
            savedDTO.setMarketCreatedAt(entity.getMarketCreatedAt());
            savedDTO.setMarketStatus(entity.isMarketStatus());
            savedDTO.setUserNickname(entity.getUserNickname());

            return savedDTO;
        } else {
            System.out.println("로그인 한 사용자가 없습니다.");
            return null;
        }
    }
}
