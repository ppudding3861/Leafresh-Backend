package com.leafresh.backend.oauth.config;

import com.leafresh.backend.oauth.security.CustomUserDetailsService;
import com.leafresh.backend.oauth.security.RestAuthenticationEntryPoint;
import com.leafresh.backend.oauth.security.TokenAuthenticationFilter;
import com.leafresh.backend.oauth.security.oauth2.CustomOAuth2UserService;
import com.leafresh.backend.oauth.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.leafresh.backend.oauth.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.leafresh.backend.oauth.security.oauth2.OAuth2AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Autowired
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Autowired
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.and()) // 기본 CORS 설정
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션을 무상태로 설정
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .formLogin(formLogin -> formLogin.disable()) // 폼 로그인 비활성화
                .httpBasic(httpBasic -> httpBasic.disable()) // HTTP Basic 인증 비활성화
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(new RestAuthenticationEntryPoint())) // 예외 처리 설정
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/", "/error").permitAll() // 특정 경로 허용
                                .requestMatchers("/auth/**", "/oauth2/**").permitAll() // 인증 관련 경로 허용
                                .anyRequest().authenticated()) // 나머지 모든 요청은 인증 필요
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .authorizationEndpoint(authorizationEndpoint ->
                                        authorizationEndpoint
                                                .baseUri("/oauth2/authorization")
                                                .authorizationRequestRepository(cookieAuthorizationRequestRepository())) // OAuth2 인증 요청 설정
                                .redirectionEndpoint(redirectionEndpoint ->
                                        redirectionEndpoint
                                                .baseUri("/oauth2/callback/**")) // 리디렉션 엔드포인트 설정 - 와일드카드를 하나로 사용
                                .userInfoEndpoint(userInfoEndpoint ->
                                        userInfoEndpoint
                                                .userService(customOAuth2UserService)) // 사용자 정보 서비스 설정
                                .successHandler(oAuth2AuthenticationSuccessHandler) // 성공 핸들러 설정
                                .failureHandler(oAuth2AuthenticationFailureHandler)); // 실패 핸들러 설정

        // Add our custom Token based authentication filter
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
