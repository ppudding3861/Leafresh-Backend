package com.leafresh.backend.oauth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leafresh.backend.follow.model.entity.FollowEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
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

    @Column(name = "terms_agreement", nullable = false)
    private boolean termsAgreement; // 약관 동의 여부

    @OneToMany(mappedBy = "follower")
    @JsonIgnore // 순환 참조 방지
    private List<FollowEntity> followers;

    @OneToMany(mappedBy = "following")
    @JsonIgnore // 순환 참조 방지
    private List<FollowEntity> followings;

    // 기본 생성자
    public User() {
    }

    // 모든 필드를 포함하는 생성자
    public User(Integer userId, String userName, String userNickname, String userPhoneNumber, String userMailAdress, String userPassword, Date userJoinDate, Date userExitDate, int userReportCount, String userExitReason, UserStatus userStatus, Role role, String imageUrl, boolean termsAgreement) {
        this.userId = userId;
        this.userName = userName;
        this.userNickname = userNickname;
        this.userPhoneNumber = userPhoneNumber;
        this.userMailAdress = userMailAdress;
        this.userPassword = userPassword;
        this.userJoinDate = userJoinDate;
        this.userExitDate = userExitDate;
        this.userReportCount = userReportCount;
        this.userExitReason = userExitReason;
        this.userStatus = userStatus;
        this.role = role;
        this.imageUrl = imageUrl;
        this.termsAgreement = termsAgreement;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserMailAdress() {
        return userMailAdress;
    }

    public void setUserMailAdress(String userMailAdress) {
        this.userMailAdress = userMailAdress;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Date getUserJoinDate() {
        return userJoinDate;
    }

    public void setUserJoinDate(Date userJoinDate) {
        this.userJoinDate = userJoinDate;
    }

    public Date getUserExitDate() {
        return userExitDate;
    }

    public void setUserExitDate(Date userExitDate) {
        this.userExitDate = userExitDate;
    }

    public int getUserReportCount() {
        return userReportCount;
    }

    public void setUserReportCount(int userReportCount) {
        this.userReportCount = userReportCount;
    }

    public String getUserExitReason() {
        return userExitReason;
    }

    public void setUserExitReason(String userExitReason) {
        this.userExitReason = userExitReason;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isTermsAgreement() {
        return termsAgreement;
    }

    public void setTermsAgreement(boolean termsAgreement) {
        this.termsAgreement = termsAgreement;
    }

    public List<FollowEntity> getFollowers() {
        return followers;
    }

    public void setFollowers(List<FollowEntity> followers) {
        this.followers = followers;
    }

    public List<FollowEntity> getFollowings() {
        return followings;
    }

    public void setFollowings(List<FollowEntity> followings) {
        this.followings = followings;
    }
}
