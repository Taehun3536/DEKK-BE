package com.dekk.security.oauth2;

import com.dekk.security.oauth2.dto.GoogleOAuth2UserInfo;
import com.dekk.security.oauth2.dto.KakaoOAuth2UserInfo;
import com.dekk.security.oauth2.dto.OAuth2UserInfo;
import com.dekk.user.domain.model.enums.Provider;
import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo of(Provider provider, Map<String, Object> attribute) {
        return switch (provider) {
            case GOOGLE -> new GoogleOAuth2UserInfo(attribute);
            case KAKAO -> new KakaoOAuth2UserInfo(attribute);
        };
    }
}
