package com.leafresh.backend.market.repository;

import com.leafresh.backend.market.model.entity.MarketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketRepository extends JpaRepository<MarketEntity, Integer> {
}
