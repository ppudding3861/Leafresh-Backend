package com.leafresh.backend.market.service;

import com.leafresh.backend.market.model.dto.MarketDTO;
import com.leafresh.backend.market.model.entity.MarketEntity;
import com.leafresh.backend.market.model.entity.VisibleScope;
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
        Integer userId = userPrincipal.getUserId(); // 인증된 유저의 고유한 id 값
        Optional<User> user = userRepository.findById(userId); // id값을 기준으로 user 객체를 꺼내옴

        if (user != null) { // 유저가 존재하면
            String userNickname = user.get().getUserNickname(); // 유저의 닉네임을 가져옴

            MarketEntity entity = new MarketEntity.Builder()
                    .marketCategory(marketDTO.getMarketCategory())
                    .marketTitle(marketDTO.getMarketTitle())
                    .marketContent(marketDTO.getMarketContent())
                    .marketImage(marketDTO.getMarketImage())
                    .marketStatus(true) // 등록되었을때는 무조건 분양중인 초기상태로 시작
                    .marketVisibleScope(VisibleScope.MARKET_PUBLIC)
                    .userNickname(userNickname)
                    .build();
            marketRepository.save(entity);
            System.out.println(entity); // 잘 저장되었나 출력

            if (entity != null) {
                MarketDTO savedDTO = new MarketDTO();
                savedDTO.setMarketId(entity.getMarketId());
                savedDTO.setMarketCategory(entity.getMarketCategory());
                savedDTO.setMarketTitle(entity.getMarketTitle());
                savedDTO.setMarketContent(entity.getMarketContent());
                savedDTO.setMarketImage(entity.getMarketImage());
                savedDTO.setMarketCreatedAt(entity.getMarketCreatedAt());
                savedDTO.setMarketStatus(entity.isMarketStatus());
                savedDTO.setMarketVisibleScope(entity.getMarketVisibleScope());
                savedDTO.setUserNickname(entity.getUserNickname());

                return savedDTO;
            } else {
                return null;
            }
        } else {
            System.out.println("로그인 한 사용자가 없습니다.");
            return null;
        }
    }

    public MarketDTO detailPost(Integer id) {
        Optional<MarketEntity> findEntity = marketRepository.findById(id);
        MarketDTO detailDTO = new MarketDTO();

        if (findEntity.isPresent()) { // 조회된 값이 있으면
            detailDTO.setMarketId(findEntity.get().getMarketId());
            detailDTO.setMarketCategory(findEntity.get().getMarketCategory());
            detailDTO.setMarketTitle(findEntity.get().getMarketTitle());
            detailDTO.setMarketContent(findEntity.get().getMarketContent());
            detailDTO.setMarketImage(findEntity.get().getMarketImage());
            detailDTO.setMarketStatus(findEntity.isPresent());
            detailDTO.setMarketVisibleScope(findEntity.get().getMarketVisibleScope());
            detailDTO.setMarketCreatedAt(findEntity.get().getMarketCreatedAt());
            detailDTO.setUserNickname(findEntity.get().getUserNickname());
        }
        return detailDTO;
    }
}
