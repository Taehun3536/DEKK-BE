package com.dekk.activelog.infrastructure.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import com.dekk.activelog.domain.model.ActiveLog;
import com.dekk.activelog.domain.model.SwipeType;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class ActiveLogJpaRepositoryTest {

    @Autowired
    private ActiveLogJpaRepository activeLogJpaRepository;

    @Test
    @DisplayName("특정 유저의 여러 SwipeType(LIKE, DISLIKE)에 해당하는 카드 ID 목록을 한 번에 조회한다")
    void findCardIdsByUserIdAndSwipeTypes_Success() {

        Long userId = 1L;
        activeLogJpaRepository.save(ActiveLog.create(userId, 101L, SwipeType.LIKE));
        activeLogJpaRepository.save(ActiveLog.create(userId, 102L, SwipeType.DISLIKE));
        activeLogJpaRepository.save(ActiveLog.create(userId, 103L, SwipeType.LIKE));
        activeLogJpaRepository.save(ActiveLog.create(2L, 104L, SwipeType.LIKE));

        List<SwipeType> targetTypes = List.of(SwipeType.LIKE, SwipeType.DISLIKE);

        List<Long> result = activeLogJpaRepository.findCardIdsByUserIdAndSwipeTypes(userId, targetTypes);

        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyInAnyOrder(101L, 102L, 103L);
        assertThat(result).doesNotContain(104L);
    }

    @Test
    @DisplayName("단일 타입 조회 시 IN 절을 통해 요청한 타입의 카드 ID만 정확히 반환한다")
    void findCardIdsByUserIdAndSingleSwipeType_Success() {

        Long userId = 1L;
        activeLogJpaRepository.save(ActiveLog.create(userId, 101L, SwipeType.LIKE));
        activeLogJpaRepository.save(ActiveLog.create(userId, 102L, SwipeType.DISLIKE));

        List<Long> result = activeLogJpaRepository.findCardIdsByUserIdAndSwipeTypes(userId, List.of(SwipeType.LIKE));

        assertThat(result).hasSize(1);
        assertThat(result).containsOnly(101L);
    }
}
