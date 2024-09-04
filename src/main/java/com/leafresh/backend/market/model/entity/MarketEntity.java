package com.leafresh.backend.market.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "market")
public class MarketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "market_id")
    private Integer marketId;

    @Column(name = "maket_category")
    private String marketCategory;

    @Column(name = "market_title")
    private String marketTitle;

    @Column(name = "market_content")
    private String marketContent;

    @Column(name = "market_image")
    private String marketImage;

    @Column(name = "market_created_at")
    private LocalDateTime marketCreatedAt; // 분양글 등록일자

    @Column(name = "market_updated_at")
    private LocalDateTime marketUpdatedAt; // 분양글 수정일자

    @Column(name = "market_complated_at")
    private LocalDateTime marketComplatedAt; // 분양 완료일자

    @Column(name = "market_status")
    private boolean marketStatus; // 글이 삭제되었을 시 false

    @Column(name = "user_nickname")
    private String userNickname; // 작성자 ID

    public MarketEntity() {
    }

    public String getMarketCategory() {
        return marketCategory;
    }

    public void setMarketCategory(String marketCategory) {
        this.marketCategory = marketCategory;
    }

    public Integer getMarketId() {
        return marketId;
    }

    public void setMarketId(Integer marketId) {
        this.marketId = marketId;
    }

    public String getMarketTitle() {
        return marketTitle;
    }

    public void setMarketTitle(String marketTitle) {
        this.marketTitle = marketTitle;
    }

    public String getMarketContent() {
        return marketContent;
    }

    public void setMarketContent(String marketContent) {
        this.marketContent = marketContent;
    }

    public String getMarketImage() {
        return marketImage;
    }

    public void setMarketImage(String marketImage) {
        this.marketImage = marketImage;
    }

    public LocalDateTime getMarketCreatedAt() {
        return marketCreatedAt;
    }

    public void setMarketCreatedAt(LocalDateTime marketCreatedAt) {
        this.marketCreatedAt = marketCreatedAt;
    }

    public LocalDateTime getMarketUpdatedAt() {
        return marketUpdatedAt;
    }

    public void setMarketUpdatedAt(LocalDateTime marketUpdatedAt) {
        this.marketUpdatedAt = marketUpdatedAt;
    }

    public LocalDateTime getMarketComplatedAt() {
        return marketComplatedAt;
    }

    public void setMarketComplatedAt(LocalDateTime marketComplatedAt) {
        this.marketComplatedAt = marketComplatedAt;
    }

    public boolean isMarketStatus() {
        return marketStatus;
    }

    public void setMarketStatus(boolean marketStatus) {
        this.marketStatus = marketStatus;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    @Override
    public String toString() {
        return "MarketEntity{" +
                "marketId=" + marketId +
                ", marketCategory='" + marketCategory + '\'' +
                ", marketTitle='" + marketTitle + '\'' +
                ", marketContent='" + marketContent + '\'' +
                ", marketImage='" + marketImage + '\'' +
                ", marketCreatedAt=" + marketCreatedAt +
                ", marketUpdatedAt=" + marketUpdatedAt +
                ", marketComplatedAt=" + marketComplatedAt +
                ", marketStatus=" + marketStatus +
                ", userNickname='" + userNickname + '\'' +
                '}';
    }
}
