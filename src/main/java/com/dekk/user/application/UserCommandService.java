package com.dekk.user.application;

import com.dekk.deck.application.DefaultDeckCommandService;
import com.dekk.user.application.command.UserOnboardingCommand;
import com.dekk.user.application.command.UserProfileUpdateCommand;
import com.dekk.user.domain.exception.UserBusinessException;
import com.dekk.user.domain.exception.UserErrorCode;
import com.dekk.user.domain.model.User;
import com.dekk.user.domain.repository.ProfileRepository;
import com.dekk.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final DefaultDeckCommandService deckCommandService;

    public void onboardUser(Long userId, UserOnboardingCommand command) {
        User user = getUser(userId);

        if (profileRepository.existsByNickname(command.nickname())) {
            throw new UserBusinessException(UserErrorCode.DUPLICATE_NICKNAME);
        }

        user.completeOnboarding(command);

        deckCommandService.createDefaultDeck(user.getId());
    }

    public void updateProfileInfo(Long userId, UserProfileUpdateCommand command) {
        User user = getUserWithProfile(userId);

        if (command.nickname() != null
                && !command.nickname().equals(user.getProfile().getNickname())) {
            if (profileRepository.existsByNickname(command.nickname())) {
                throw new UserBusinessException(UserErrorCode.DUPLICATE_NICKNAME);
            }
        }

        user.updateProfileInfo(command);
    }

    public void deleteUser(Long userId) {
        User user = getUser(userId);

        user.deleteUser();
    }

    private User getUser(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));
    }

    private User getUserWithProfile(Long userId) {
        return userRepository
                .findWithProfileById(userId)
                .orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));
    }
}
