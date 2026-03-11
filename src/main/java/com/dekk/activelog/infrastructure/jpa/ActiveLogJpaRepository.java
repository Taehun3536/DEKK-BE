package com.dekk.activelog.infrastructure.jpa;

import com.dekk.activelog.domain.model.ActiveLog;
import com.dekk.activelog.domain.model.SwipeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ActiveLogJpaRepository extends JpaRepository<ActiveLog, Long> {
    boolean existsByUserIdAndCardId(Long userId, Long cardId);

    Optional<ActiveLog> findByUserIdAndCardId(Long userId, Long cardId);

    @Query("SELECT a.cardId FROM ActiveLog a WHERE a.userId = :userId AND a.swipeType IN :swipeTypes")
    List<Long> findCardIdsByUserIdAndSwipeTypes(
            @Param("userId") Long userId, @Param("swipeTypes") List<SwipeType> swipeTypes);
}
