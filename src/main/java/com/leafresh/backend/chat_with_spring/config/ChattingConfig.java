package com.leafresh.backend.chat_with_spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class ChattingConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * STOMP 엔드포인트를 등록합니다.
     * 클라이언트가 이 엔드포인트를 통해 WebSocket 연결을 설정합니다.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // 연결을 위한 주소 웹 소켓의 연결 형식 ws://localhost:8080/ws
                .setAllowedOrigins("*");
    }


    /**
     * 메시지 브로커를 구성합니다.
     * 메시지 라우팅 및 브로드캐스트를 위한 설정을 추가할 수 있습니다.
     * 브로드캐스팅이란 한 송신자가 여러 수신자에게 동시에 메세지를 전송하는 방식이다.
     * 채팅 서버와 같은 실시간 애플리케이션에서 주로 사용되며, 특정 메시지를 여러 클라이언트에게 전달할 때 사용된다.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메세지를 구독(수신)하는 요청 엔드포인트
        registry.enableSimpleBroker("/sub");

        // 메세지를 발행(수신)하는 엔드포인트
        registry.setApplicationDestinationPrefixes("/pub");
    }


    /**
     * WebSocket 전송 설정을 구성합니다.
     * 전송에 대한 설정을 변경할 수 있습니다.
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        WebSocketMessageBrokerConfigurer.super.configureWebSocketTransport(registry);
    }

    /**
     * 클라이언트의 인바운드 채널을 구성합니다.
     * 메시지 필터링 및 핸들러를 추가할 수 있습니다.
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        WebSocketMessageBrokerConfigurer.super.configureClientInboundChannel(registration);
    }

    /**
     * 클라이언트의 아웃바운드 채널을 구성합니다.
     * 클라이언트에 보내는 메시지를 처리하는 핸들러를 추가할 수 있습니다.
     */
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        WebSocketMessageBrokerConfigurer.super.configureClientOutboundChannel(registration);
    }

    /**
     * 메시지 핸들러 메서드의 인수 해석기를 추가합니다.
     * 메시지 처리 시 인수로 전달할 객체를 해석하는 로직을 추가할 수 있습니다.
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        WebSocketMessageBrokerConfigurer.super.addArgumentResolvers(argumentResolvers);
    }

    /**
     * 메시지 핸들러 메서드의 반환 값 핸들러를 추가합니다.
     * 메시지 처리 후 반환 값을 처리하는 로직을 추가할 수 있습니다.
     */
    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        WebSocketMessageBrokerConfigurer.super.addReturnValueHandlers(returnValueHandlers);
    }

    /**
     * 메시지 변환기를 구성합니다.
     * 클라이언트와 서버 간의 메시지 변환 로직을 설정할 수 있습니다.
     */
    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        return WebSocketMessageBrokerConfigurer.super.configureMessageConverters(messageConverters);
    }

    /**
     * WebSocket의 설정 우선 순위를 반환합니다.
     * 여러 설정이 있을 때 우선 순위를 정할 수 있습니다.
     */
    @Override
    public Integer getPhase() {
        return WebSocketMessageBrokerConfigurer.super.getPhase();
    }
}
