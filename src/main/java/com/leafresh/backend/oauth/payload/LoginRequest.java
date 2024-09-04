package com.leafresh.backend.oauth.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Created by rajeevkumarsingh on 02/08/17.
 */
public class LoginRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    // Getter 메서드
    public String getEmail() {
        return email;
    }

    // Setter 메서드
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter 메서드
    public String getPassword() {
        return password;
    }

    // Setter 메서드
    public void setPassword(String password) {
        this.password = password;
    }
}
