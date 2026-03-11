package com.dekk.user.application;

import com.dekk.user.application.dto.result.UserInfoResult;
import com.dekk.user.domain.exception.UserBusinessException;
import com.dekk.user.domain.exception.UserErrorCode;
import com.dekk.user.domain.model.User;
import com.dekk.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository userRepository;

    public UserInfoResult getMyInfo(Long userId) {
        User user = userRepository
                .findWithProfileById(userId)
                .orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));

        return UserInfoResult.from(user);
    }
}
