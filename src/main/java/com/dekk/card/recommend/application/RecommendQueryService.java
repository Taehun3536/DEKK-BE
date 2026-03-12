package com.dekk.card.recommend.application;

import com.dekk.activelog.application.ActiveLogQueryService;
import com.dekk.card.application.CardQueryService;
import com.dekk.card.application.dto.query.RecommendCandidateQuery;
import com.dekk.card.application.dto.result.MemberCardResult;
import com.dekk.card.domain.model.enums.TargetGender;
import com.dekk.user.application.UserQueryService;
import com.dekk.user.application.dto.result.UserInfoResult;
import com.dekk.user.domain.model.enums.Gender;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecommendQueryService {

    private final CardQueryService cardQueryService;
    private final UserQueryService userQueryService;
    private final ActiveLogQueryService activeLogQueryService;

    public List<MemberCardResult> getRecommendCandidates(Long userId) {
        UserInfoResult userInfo = userQueryService.getMyInfo(userId);
        List<MemberCardResult> candidates = fetchCandidates(userInfo);
        return excludeSwiped(candidates, userId);
    }

    private List<MemberCardResult> fetchCandidates(UserInfoResult userInfo) {
        List<TargetGender> genders = resolveTargetGenders(userInfo.gender());
        return cardQueryService
                .getRecommendCandidates(RecommendCandidateQuery.of(genders, userInfo.height(), userInfo.weight()))
                .stream()
                .map(MemberCardResult::from)
                .toList();
    }

    private List<MemberCardResult> excludeSwiped(List<MemberCardResult> candidates, Long userId) {
        Set<Long> swipedCardIds = activeLogQueryService.getAllSwipedCardIds(userId);
        return candidates.stream()
                .filter(card -> !swipedCardIds.contains(card.cardId()))
                .toList();
    }

    private List<TargetGender> resolveTargetGenders(Gender gender) {
        if (gender == null) {
            return List.of(TargetGender.values());
        }
        return switch (gender) {
            case MALE -> List.of(TargetGender.MEN, TargetGender.OTHER);
            case FEMALE -> List.of(TargetGender.WOMEN, TargetGender.OTHER);
            case OTHER -> List.of(TargetGender.values());
        };
    }
}
