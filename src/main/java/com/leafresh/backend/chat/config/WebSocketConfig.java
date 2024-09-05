package com.leafresh.backend.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker  // STOMP 프로토콜을 통해 WebSocket 메시지 브로커를 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 메시지 브로커 구성 설정
     * 메시지 브로커: 메시지 발행자와 구독자 간의 메시지 라우팅을 처리
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // "/queue"와 "/topic" 접두사로 시작하는 경로에 대해 내장된 메시지 브로커를 사용하여 메시지 전달
        // "/topic": 다수의 클라이언트가 구독하는 모델 (예: 실시간 뉴스, 주식 시세 정보)
        // "/queue": 일대일 개인 메시징에 사용 (예: 1:1 채팅)
        config.enableSimpleBroker("/queue", "/topic");

        // 클라이언트가 서버로 메시지를 전송할 때 사용하는 접두사를 "/app"으로 설정
        // 예: 클라이언트가 "/app/hello"로 메시지를 전송하면, 서버의 @MessageMapping("/hello") 메서드로 라우팅
        config.setApplicationDestinationPrefixes("/app");

        // 개인 메시지를 처리할 때 사용하는 사용자 접두사 설정
        // 개인 메시지는 "/user/queue" 경로를 통해 전달됨
        config.setUserDestinationPrefix("/user");
    }

    /**
     * STOMP 엔드포인트를 설정하여 클라이언트가 WebSocket에 연결할 수 있도록 구성
     * SockJS를 통해 WebSocket을 지원하지 않는 브라우저에서도 폴백 옵션으로 사용할 수 있음
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 웹소켓 연결을 시작하는 경로로 "/ws" 설정
        // SockJS를 통해 WebSocket을 지원하지 않는 환경에서도 호환성 유지
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:8000")  // 허용할 출처를 설정
                .withSockJS();  // SockJS 활성화: WebSocket을 지원하지 않는 브라우저에 폴백 메커니즘 제공
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        // 예외 메시지와 상태 코드 반환
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
    }
}
