package com.leafresh.backend.oauth.payload;

public class AuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";

    // 생성자
    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    // Getter 메서드
    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    // Setter 메서드
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
