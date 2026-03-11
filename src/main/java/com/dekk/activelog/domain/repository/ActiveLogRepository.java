package com.dekk.activelog.domain.repository;

import com.dekk.activelog.domain.model.ActiveLog;
import com.dekk.activelog.domain.model.SwipeType;
import java.util.List;
import java.util.Optional;

public interface ActiveLogRepository {
    ActiveLog save(ActiveLog activeLog);

    boolean existsByUserIdAndCardId(Long userId, Long cardId);

    Optional<ActiveLog> findByUserIdAndCardId(Long userId, Long cardId);

    void delete(ActiveLog activeLog);

    List<Long> findCardIdsByUserIdAndSwipeTypes(Long userId, List<SwipeType> swipeTypes);
}
