package com.dekk.user.application.dto.result;

import com.dekk.user.domain.model.User;
import com.dekk.user.domain.model.enums.Gender;

public record UserInfoResult(
        Long id,
        String email,
        String nickname,
        Integer height,
        Integer weight,
        Gender gender,
        String status,
        String role) {
    public static UserInfoResult from(User user) {
        if (user.getProfile() == null) {
            return new UserInfoResult(
                    user.getId(),
                    user.getEmail(),
                    null,
                    null,
                    null,
                    null,
                    user.getStatus().name(),
                    user.getRole().name());
        }

        return new UserInfoResult(
                user.getId(),
                user.getEmail(),
                user.getProfile().getNickname(),
                user.getProfile().getHeight(),
                user.getProfile().getWeight(),
                user.getProfile().getGender(),
                user.getStatus().name(),
                user.getRole().name());
    }
}
