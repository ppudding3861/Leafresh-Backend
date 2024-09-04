package com.leafresh.backend.market.model.dto;

import com.leafresh.backend.market.model.entity.VisibleScope;

import java.time.LocalDateTime;

public class MarketDTO {
    private int marketId;
    private String marketCategory;
    private String marketTitle;
    private String marketContent;
    private String marketImage;
    private boolean marketStatus;
    private VisibleScope marketVisibleScope; // 게시글 공개 범위
    private LocalDateTime marketCreatedAt;
    private String userNickname;

    public MarketDTO() {
    }

    public MarketDTO(int marketId, String marketCategory, String marketTitle, String marketContent, String marketImage, boolean marketStatus, VisibleScope marketVisibleScope, LocalDateTime marketCreatedAt, String userNickname) {
        this.marketId = marketId;
        this.marketCategory = marketCategory;
        this.marketTitle = marketTitle;
        this.marketContent = marketContent;
        this.marketImage = marketImage;
        this.marketStatus = marketStatus;
        this.marketVisibleScope = marketVisibleScope;
        this.marketCreatedAt = marketCreatedAt;
        this.userNickname = userNickname;
    }

    public int getMarketId() {
        return marketId;
    }

    public void setMarketId(int marketId) {
        this.marketId = marketId;
    }

    public String getMarketCategory() {
        return marketCategory;
    }

    public void setMarketCategory(String marketCategory) {
        this.marketCategory = marketCategory;
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

    public boolean isMarketStatus() {
        return marketStatus;
    }

    public void setMarketStatus(boolean marketStatus) {
        this.marketStatus = marketStatus;
    }

    public VisibleScope getMarketVisibleScope() {
        return marketVisibleScope;
    }

    public void setMarketVisibleScope(VisibleScope marketVisibleScope) {
        this.marketVisibleScope = marketVisibleScope;
    }

    public LocalDateTime getMarketCreatedAt() {
        return marketCreatedAt;
    }

    public void setMarketCreatedAt(LocalDateTime marketCreatedAt) {
        this.marketCreatedAt = marketCreatedAt;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    @Override
    public String toString() {
        return "MarketDTO{" +
                "marketId=" + marketId +
                ", marketCategory='" + marketCategory + '\'' +
                ", marketTitle='" + marketTitle + '\'' +
                ", marketContent='" + marketContent + '\'' +
                ", marketImage='" + marketImage + '\'' +
                ", marketStatus=" + marketStatus +
                ", marketVisibleScope=" + marketVisibleScope +
                ", marketCreatedAt=" + marketCreatedAt +
                ", userNickname='" + userNickname + '\'' +
                '}';
    }
}
