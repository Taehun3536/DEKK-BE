package com.dekk.security.oauth2;

import com.dekk.user.domain.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomUserDetails implements UserDetails, OAuth2User {

    @Getter
    private final Long id;
    private final String email;
    private final String role;

    @Getter
    private final String status;

    private final Map<String, Object> attributes;

    public CustomUserDetails(User user, Map<String, Object> attributes) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.role = user.getRole().getKey();
        this.status = user.getStatus().name();
        this.attributes = attributes;
    }
    public CustomUserDetails(Long id, String email, String role, String status) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.status = status;
        this.attributes = Collections.emptyMap();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    //Todo : 추후에 관리자 로그인 및 자체 로그인이 추가 된다면 소셜 로그인 회원을 구분하여 계정 상태 관리 필요
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
