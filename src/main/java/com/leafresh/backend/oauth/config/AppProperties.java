package com.leafresh.backend.oauth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Auth auth = new Auth();

    public static class Auth {
        private String tokenSecret;
        private long tokenExpirationMsec;
        private long refreshTokenExpirationMsec;  // 리프레시 토큰 만료 시간 필드 추가

        public String getTokenSecret() {
            return tokenSecret;
        }

        public void setTokenSecret(String tokenSecret) {
            this.tokenSecret = tokenSecret;
        }

        public long getTokenExpirationMsec() {
            return tokenExpirationMsec;
        }

        public void setTokenExpirationMsec(long tokenExpirationMsec) {
            this.tokenExpirationMsec = tokenExpirationMsec;
        }

        // 리프레시 토큰 만료 시간 Getter
        public long getRefreshTokenExpirationMsec() {
            return refreshTokenExpirationMsec;
        }

        // 리프레시 토큰 만료 시간 Setter
        public void setRefreshTokenExpirationMsec(long refreshTokenExpirationMsec) {
            this.refreshTokenExpirationMsec = refreshTokenExpirationMsec;
        }
    }

    public Auth getAuth() {
        return auth;
    }
}
