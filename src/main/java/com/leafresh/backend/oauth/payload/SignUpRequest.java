package com.leafresh.backend.oauth.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SignUpRequest {

    @NotBlank
    @Size(max = 6)  // 이름의 최대 길이 제한
    private String name;

    @NotBlank
    @Size(max = 40)
    @Email  // 유효한 이메일 주소 검증
    private String email;

    @NotBlank
    @Size(min = 6, max = 20)  // 비밀번호의 길이 제한
    private String password;

    @NotBlank
    @Size(max = 15)  // 닉네임의 최대 길이 제한
    private String nickname;  // 필드명 수정

    @NotBlank
    @Pattern(regexp = "^[0-9]{10,11}$", message = "유효한 전화번호를 입력하세요.")  // 전화번호 유효성 검사
    private String phoneNumber;  // 전화번호를 String 타입으로 유지

    private String imageUrl;  // 이미지 URL 필드, 길이 제한 없음

    private boolean isTermsAgreement;

    // Getter 메서드
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isTermsAgreement() {
        return isTermsAgreement;
    }

    // Setter 메서드

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTermsAgreement(boolean termsAgreement) {
        isTermsAgreement = termsAgreement;
    }
}
