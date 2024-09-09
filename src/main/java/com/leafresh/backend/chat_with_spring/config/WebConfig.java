package com.leafresh.backend.chat_with_spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    /*
    * CROS(교차 출처 리소스 공유, Cross-Origin Resource Sharing)
    * 출처(Origin): 출처는 프로토콜(예: http, https), 도메인(예: www.example.com), 포트(예: 80, 443)로 구성됩니다. 예를 들어, https://www.example.com과 http://www.example.com은 다른 출처입니다.
    * Preflight 요청: CORS 요청이 복잡한 경우(예: HTTP 메서드가 GET, POST 외에 다른 경우, 사용자 지정 헤더가 포함된 경우) 브라우저는 실제 요청을 보내기 전에 OPTIONS 메서드를 사용하여 서버에 "preflight" 요청을 보냅니다. 서버가 이 요청을 허용하면 실제 요청이 전송됩니다.
    * Access-Control-Allow-Origin 헤더: 서버가 어떤 출처의 요청을 허용할지를 정의하는 HTTP 응답 헤더입니다. 예를 들어, 특정 도메인만 허용하거나 모든 도메인(*)을 허용할 수 있습니다.
    * */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
