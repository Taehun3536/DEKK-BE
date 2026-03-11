package com.dekk.activelog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.dekk.activelog.domain.model.SwipeType;
import com.dekk.activelog.domain.repository.ActiveLogRepository;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ActiveLogQueryServiceTest {

    @Mock
    private ActiveLogRepository activeLogRepository;

    @InjectMocks
    private ActiveLogQueryService activeLogQueryService;

    @Test
    @DisplayName("전체 스와이프 목록 조회 시 중복이 제거된 Set 구조로 반환되어야 한다")
    void getAllSwipedCardIds_ReturnSet() {

        Long userId = 1L;
        List<Long> mockList = List.of(10L, 20L, 10L);
        given(activeLogRepository.findCardIdsByUserIdAndSwipeTypes(eq(userId), anyList()))
            .willReturn(mockList);

        Set<Long> result = activeLogQueryService.getAllSwipedCardIds(userId);

        assertThat(result).isInstanceOf(Set.class);
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(10L, 20L);
        verify(activeLogRepository).findCardIdsByUserIdAndSwipeTypes(eq(userId), anyList());
    }

    @Test
    @DisplayName("특정 타입 조회 시 내부적으로 IN 절 메서드를 호출하여 리스트를 반환한다")
    void getSwipedCardIds_ReturnList() {

        Long userId = 1L;
        SwipeType type = SwipeType.LIKE;
        List<Long> mockList = List.of(10L, 20L);

        given(activeLogRepository.findCardIdsByUserIdAndSwipeTypes(userId, List.of(type)))
            .willReturn(mockList);

        List<Long> result = activeLogQueryService.getSwipedCardIds(userId, type);

        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(mockList);

        verify(activeLogRepository).findCardIdsByUserIdAndSwipeTypes(userId, List.of(type));
    }
}
