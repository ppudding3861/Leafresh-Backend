package com.leafresh.backend.oauth.payload;

public class AuthResponse {
    private String accessToken;
    private String refreshToken; // 리프레시 토큰 추가
    private String tokenType = "Bearer";

    // 수정된 생성자
    public AuthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken; // 리프레시 토큰 설정
    }

    // Getter 메서드
    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken; // 리프레시 토큰 Getter
    }

    public String getTokenType() {
        return tokenType;
    }

    // Setter 메서드
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken; // 리프레시 토큰 Setter
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
