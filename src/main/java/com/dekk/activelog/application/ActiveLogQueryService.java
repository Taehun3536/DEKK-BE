package com.dekk.activelog.application;

import com.dekk.activelog.domain.model.SwipeType;
import com.dekk.activelog.domain.repository.ActiveLogRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActiveLogQueryService {

    private final ActiveLogRepository activeLogRepository;

    public Set<Long> getAllSwipedCardIds(Long userId) {
        List<SwipeType> targetTypes = List.of(SwipeType.LIKE, SwipeType.DISLIKE);
        List<Long> cardIds = activeLogRepository.findCardIdsByUserIdAndSwipeTypes(userId, targetTypes);

        return new HashSet<>(cardIds);
    }

    public List<Long> getSwipedCardIds(Long userId, SwipeType swipeType) {
        return activeLogRepository.findCardIdsByUserIdAndSwipeTypes(userId, List.of(swipeType));
    }
}
