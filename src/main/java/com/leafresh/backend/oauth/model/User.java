package com.leafresh.backend.oauth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter @Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer userId; // user_id를 Integer로 변경

    @Column(name = "user_name", nullable = false, length = 255)
    private String userName; // VARCHAR(255)

    @Column(name = "user_nickname", nullable = false, length = 255)
    private String userNickname; // 추가된 필드: user_nickname

    @Column(name = "user_phone_number", nullable = false, length = 20)
    private String userPhoneNumber; // 전화번호를 String으로 변경

    @Email
    @Column(name = "user_mail_adress", nullable = false, length = 255)
    private String userMailAdress; // VARCHAR(255)

    @JsonIgnore
    @Column(name = "user_password", nullable = false, length = 255)
    private String userPassword; // VARCHAR(255)

    @Column(name = "user_join_date", nullable = false)
    private Date userJoinDate; // TIMESTAMP

    @Column(name = "user_exit_date")
    private Date userExitDate; // TIMESTAMP

    @Column(name = "user_report_count", nullable = false)
    private int userReportCount; // int형 경고 횟수

    @Column(name = "user_exit_reason", length = 255)
    private String userExitReason; // VARCHAR(255)

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false)
    private UserStatus userStatus; // ENUM 타입, Enum 클래스는 아래에 정의됨

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role; // 추가된 필드: role, 사용자 권한을 나타내는 ENUM 타입

    @Column(name = "image_url", columnDefinition = "TEXT") // URL 길이 제한 제거
    private String imageUrl; // 프로필 이미지 URL
}
