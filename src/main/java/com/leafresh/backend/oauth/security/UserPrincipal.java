package com.leafresh.backend.oauth.security;

import com.leafresh.backend.oauth.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserPrincipal implements UserDetails {

    private Integer userId;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private User user;  // User 객체 추가

    public UserPrincipal(Integer userId, String email, String password, Collection<? extends GrantedAuthority> authorities, User user) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.user = user; // User 객체 초기화
    }

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new UserPrincipal(
                user.getUserId(),
                user.getUserMailAdress(),
                user.getUserPassword(),
                authorities,
                user // User 객체 전달
        );
    }

    public Integer getUserId() {
        return userId;
    }

    public User getUser() {
        return user; // User 객체 반환 메서드
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
