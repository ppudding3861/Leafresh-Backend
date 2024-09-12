package com.leafresh.backend.oauth.service;

import com.leafresh.backend.oauth.config.AppProperties;
import com.leafresh.backend.oauth.exception.ResourceNotFoundException;
import com.leafresh.backend.oauth.model.User;
import com.leafresh.backend.oauth.repository.UserRepository;
import com.leafresh.backend.oauth.security.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private final AppProperties appProperties;
    private final UserRepository userRepository;

    @Autowired
    public TokenProvider(AppProperties appProperties, UserRepository userRepository) {
        this.appProperties = appProperties;
        this.userRepository = userRepository;
    }

    // 액세스 토큰 생성
    public String createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

        String token = Jwts.builder()
                .setSubject(Integer.toString(userPrincipal.getUserId()))  // Long에서 Integer로 수정
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();

        logger.debug("Created token for userId: {}, token: {}", userPrincipal.getUserId(), token);
        return token;
    }

    // 사용자 세부 정보로 토큰 생성
    public String createTokenWithUserDetails(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

        String token = Jwts.builder()
                .setSubject(Integer.toString(user.getUserId()))  // Long으로 수정
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();

        logger.debug("Created token for userId: {}, token: {}", user.getUserId(), token);
        return token;
    }


    // 리프레시 토큰 생성
    public String createRefreshToken(Integer userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getRefreshTokenExpirationMsec());

        return Jwts.builder()
                .setSubject(Integer.toString(userId))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
    }

    public String refreshAccessToken(String refreshToken) {
        if (validateRefreshToken(refreshToken)) {
            Integer userId = getUserIdFromRefreshToken(refreshToken);
            // 새로운 액세스 토큰 발급
            return createTokenWithUserDetails(userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId)));
        }
        throw new IllegalArgumentException("Invalid refresh token");
    }

    // 토큰에서 사용자 ID 추출
    public Integer getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();

        return Integer.parseInt(claims.getSubject());  // Integer로 파싱
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            logger.error("JWT 토큰이 없거나 비어 있습니다.");
            return false;
        }

        try {
            logger.debug("Validating token: {}", token);
            Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Malformed JWT token: {}", token, e);
            return false;
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token: {}", token, e);
            return false;
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token: {}", token, e);
            return false;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", token, e);
            return false;
        } catch (JwtException e) {
            logger.error("Invalid JWT token", e);
            return false;
        }
    }

    // 리프레시 토큰에서 사용자 ID 추출
    public Integer getUserIdFromRefreshToken(String token) {
        return getUserIdFromToken(token); // 동일한 방식으로 추출
    }

    // 리프레시 토큰 유효성 검사
    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken); // 동일한 방식으로 유효성 검사
    }


}
