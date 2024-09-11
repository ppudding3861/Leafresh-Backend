package com.leafresh.backend.market.service;

import com.leafresh.backend.market.model.dto.MarketDTO;
import com.leafresh.backend.market.model.entity.MarketEntity;
import com.leafresh.backend.market.model.entity.VisibleScope;
import com.leafresh.backend.market.repository.MarketRepository;
import com.leafresh.backend.oauth.model.User;
import com.leafresh.backend.oauth.repository.UserRepository;
import com.leafresh.backend.oauth.security.CurrentUser;
import com.leafresh.backend.oauth.security.UserPrincipal;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

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
            dto.setMarketVisibleScope(entity.getMarketVisibleScope());
            dto.setUserEmail(entity.getUserEmail());
            marketList.add(dto);
        }
        return marketList;
    }

    @Transactional
    public MarketDTO createPost(MarketDTO marketDTO, @CurrentUser UserPrincipal userPrincipal) {
        Integer userId = userPrincipal.getUserId(); // 인증된 유저의 고유한 id 값
        Optional<User> user = userRepository.findById(userId); // id값을 기준으로 user 객체를 꺼내옴

        if (user != null) { // 유저가 존재하면
            String userEmailAddress = user.get().getUserMailAdress(); // 유저의 이메일을 가져옴(고유한 값)

            MarketEntity entity = new MarketEntity.Builder()
                    .marketCategory(marketDTO.getMarketCategory())
                    .marketTitle(marketDTO.getMarketTitle())
                    .marketContent(marketDTO.getMarketContent())
                    .marketImage(marketDTO.getMarketImage())
                    .marketStatus(true) // 등록되었을때는 무조건 분양중인 초기상태로 시작
                    .marketVisibleScope(marketDTO.getMarketVisibleScope())
                    .userEmail(userEmailAddress) // 이메일을 엔티티에 담아줌
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
                savedDTO.setUserEmail(entity.getUserEmail());

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
            detailDTO.setMarketStatus(findEntity.get().isMarketStatus());
            detailDTO.setMarketVisibleScope(findEntity.get().getMarketVisibleScope());
            detailDTO.setMarketCreatedAt(findEntity.get().getMarketCreatedAt());
            detailDTO.setUserEmail(findEntity.get().getUserEmail());
        }
        return detailDTO;
    }

    @Transactional
    public MarketDTO modifyPost(MarketDTO marketDTO, @CurrentUser UserPrincipal userPrincipal, Integer id) {
        Integer userId = userPrincipal.getUserId(); // 현재 인증된 유저의 아이디값을 가져옴
        Optional<User> user = userRepository.findById(userId); // user id값 기준으로 유저 엔터티 조회
        Optional<MarketEntity> marketEntity = marketRepository.findById(id); // url id값 기준으로 게시글 조회

        if (user != null) { // 유저가 존재하면
            if (marketEntity.isPresent()) { // 게시글이 존재하면
                MarketEntity market = marketEntity.get();
                String userEmailAddress = user.get().getUserMailAdress(); // 유저의 이메일을 가져옴

                market.setMarketCategory(marketDTO.getMarketCategory());
                market.setMarketTitle(marketDTO.getMarketTitle());
                market.setMarketContent(marketDTO.getMarketContent());
                market.setMarketImage(marketDTO.getMarketImage());
                market.setMarketStatus(marketDTO.isMarketStatus());
                market.setMarketVisibleScope(marketDTO.getMarketVisibleScope());
                market.setUserEmail(userEmailAddress);

                marketRepository.save(market);
                System.out.println("수정완료 : "+market); // 수정이 잘 되었는지 확인

                if (market != null) {
                    MarketDTO savedDTO = new MarketDTO();
                    savedDTO.setMarketId(market.getMarketId());
                    savedDTO.setMarketCategory(market.getMarketCategory());
                    savedDTO.setMarketTitle(market.getMarketTitle());
                    savedDTO.setMarketContent(market.getMarketContent());
                    savedDTO.setMarketImage(market.getMarketImage());
                    savedDTO.setMarketCreatedAt(market.getMarketCreatedAt());
                    savedDTO.setMarketStatus(market.isMarketStatus());
                    savedDTO.setMarketVisibleScope(market.getMarketVisibleScope());
                    savedDTO.setUserEmail(market.getUserEmail());

                    return savedDTO;
                } else {
                    return null;
                }
            } else {
                System.out.println("수정할 게시글이 존재하지 않습니다.");
                return null;
            }
        } else {
            System.out.println("로그인 한 사용자가 없습니다.");
            return null;
        }
    }


    @Transactional
    public int deletePost(@CurrentUser UserPrincipal userPrincipal, @PathVariable Integer id){
        Integer userId = userPrincipal.getUserId();
        Optional<User> user = userRepository.findById(userId); // 유저정보 가져옴
        Optional<MarketEntity> market = marketRepository.findById(id); // 게시글 데이터 가져옴

        int result = 0;

        if (user.isPresent() && market.isPresent()) { // 유저, 게시글이 존재하면
            if (market.isPresent()) { // 게시글이 존재하면
                MarketEntity marketEntity = market.get(); // 게시글 엔티티 가져오기
                marketEntity.setMarketVisibleScope(VisibleScope.MARKET_DELETE); // 공개범위를 MARKET_DELETE로 수정
                marketRepository.save(marketEntity);
                return result;
            } else {
                System.out.println("삭제하려는 게시글이 존재하지 않습니다.");
                result = 1;
                return result;
            }
        } else {
            System.out.println("로그인 한 사용자가 없습니다.");
            result = 1;
            return result;
        }
    }

    @Transactional
    public void updateMarketStatus(Integer id, Boolean status) {
        Optional<MarketEntity> marketEntity = marketRepository.findById(id);
        System.out.println(marketEntity);

        if (marketEntity.isPresent()) { // 게시글이 존재하면
            MarketEntity market = marketEntity.get();
            market.setMarketStatus(status);
            marketRepository.save(market);
        } else {
            System.out.println("게시글이 존재하지 않습니다.");
        }
    }
}
