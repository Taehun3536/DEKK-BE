package com.dekk.user.domain.repository;

import com.dekk.user.domain.model.Profile;

public interface ProfileRepository {
    Profile save(Profile profile);

    boolean existsByNickname(String nickname);
}
