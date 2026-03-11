package com.dekk.security.oauth2.dto;

import com.dekk.security.oauth2.exception.CustomOAuth2Exception;
import com.dekk.security.oauth2.exception.OAuth2ErrorCode;
import com.dekk.user.domain.model.enums.Provider;
import java.util.Map;

public class KakaoOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;
    private final Map<String, Object> kakaoAccount;
    private final Map<String, Object> profile;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;

        Object kakaoAccountObj = attributes.get("kakao_account");
        if (!(kakaoAccountObj instanceof Map)) {
            throw new CustomOAuth2Exception(OAuth2ErrorCode.MISSING_USER_INFO);
        }
        this.kakaoAccount = (Map<String, Object>) kakaoAccountObj;

        Object profileObj = this.kakaoAccount.get("profile");
        if (!(profileObj instanceof Map)) {
            throw new CustomOAuth2Exception(OAuth2ErrorCode.MISSING_USER_INFO);
        }
        this.profile = (Map<String, Object>) profileObj;
    }

    @Override
    public Provider getProvider() {
        return Provider.KAKAO;
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getEmail() {
        return (String) kakaoAccount.getOrDefault("email", null);
    }

    @Override
    public String getName() {
        return (String) profile.get("nickname");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
