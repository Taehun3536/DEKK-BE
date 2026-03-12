package com.dekk.card.recommend.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.dekk.activelog.application.ActiveLogQueryService;
import com.dekk.card.application.CardQueryService;
import com.dekk.card.application.dto.result.MemberCardResult;
import com.dekk.card.domain.model.Card;
import com.dekk.card.domain.model.CardImage;
import com.dekk.user.application.UserQueryService;
import com.dekk.user.application.dto.result.UserInfoResult;
import com.dekk.user.domain.model.enums.Gender;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecommendQueryServiceTest {

    private static final Long USER_ID = 1L;

    @Mock private CardQueryService cardQueryService;
    @Mock private UserQueryService userQueryService;
    @Mock private ActiveLogQueryService activeLogQueryService;

    @InjectMocks
    private RecommendQueryService recommendQueryService;

    @Nested
    @DisplayName("스와이프 이력 제외")
    class ExcludeSwipedCards {

        @BeforeEach
        void setUp() {
            given(userQueryService.getMyInfo(USER_ID))
                    .willReturn(userInfo(Gender.MALE, 175, 70));
        }

        @Test
        @DisplayName("스와이프한 카드는 추천 결과에서 제외된다")
        void shouldExcludeSwipedCards_whenSwipedIdsExist() {
            Card card10 = mockCard(10L);
            Card card20 = mockCard(20L);
            Card card30 = mockCard(30L);

            given(cardQueryService.getRecommendCandidates(any()))
                    .willReturn(List.of(card10, card20, card30));
            given(activeLogQueryService.getAllSwipedCardIds(USER_ID))
                    .willReturn(Set.of(10L, 20L));

            List<MemberCardResult> result = recommendQueryService.getRecommendCandidates(USER_ID);

            assertThat(result).hasSize(1);
            assertThat(result.getFirst().cardId()).isEqualTo(30L);
        }

        @Test
        @DisplayName("스와이프 이력이 없으면 후보 카드 전체가 반환된다")
        void shouldReturnAllCandidates_whenNoSwipeHistory() {
            Card card1 = mockCard(1L);
            Card card2 = mockCard(2L);

            given(cardQueryService.getRecommendCandidates(any()))
                    .willReturn(List.of(card1, card2));
            given(activeLogQueryService.getAllSwipedCardIds(USER_ID))
                    .willReturn(Set.of());

            List<MemberCardResult> result = recommendQueryService.getRecommendCandidates(USER_ID);

            assertThat(result).hasSize(2);
        }

        @Test
        @DisplayName("모든 후보 카드를 스와이프했으면 빈 리스트가 반환된다")
        void shouldReturnEmpty_whenAllCandidatesSwiped() {
            Card card1 = mockCard(1L);
            Card card2 = mockCard(2L);

            given(cardQueryService.getRecommendCandidates(any()))
                    .willReturn(List.of(card1, card2));
            given(activeLogQueryService.getAllSwipedCardIds(USER_ID))
                    .willReturn(Set.of(1L, 2L));

            List<MemberCardResult> result = recommendQueryService.getRecommendCandidates(USER_ID);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("유저 프로파일 기반 필터링")
    class ProfileBasedFiltering {

        @BeforeEach
        void setUp() {
            given(cardQueryService.getRecommendCandidates(any())).willReturn(List.of());
            given(activeLogQueryService.getAllSwipedCardIds(USER_ID)).willReturn(Set.of());
        }

        @Test
        @DisplayName("성별 정보가 없으면 전체 성별 대상으로 카드가 조회된다")
        void shouldQueryAllGenders_whenGenderIsNull() {
            given(userQueryService.getMyInfo(USER_ID))
                    .willReturn(userInfo(null, 170, 65));

            assertThat(recommendQueryService.getRecommendCandidates(USER_ID)).isEmpty();
        }

        @Test
        @DisplayName("체형 정보가 없으면 기본 범위로 카드가 조회된다")
        void shouldUseDefaultRange_whenBodyInfoIsNull() {
            Card card = mockCard(1L);

            given(userQueryService.getMyInfo(USER_ID))
                    .willReturn(userInfo(Gender.MALE, null, null));
            given(cardQueryService.getRecommendCandidates(any()))
                    .willReturn(List.of(card));

            assertThat(recommendQueryService.getRecommendCandidates(USER_ID)).hasSize(1);
        }
    }

    private UserInfoResult userInfo(Gender gender, Integer height, Integer weight) {
        return new UserInfoResult(USER_ID, "test@test.com", "닉네임", height, weight, gender, "ACTIVE", "USER");
    }

    private Card mockCard(Long cardId) {
        Card card = mock(Card.class);
        CardImage cardImage = mock(CardImage.class);

        given(card.getId()).willReturn(cardId);
        given(card.getCardImage()).willReturn(cardImage);
        given(cardImage.getImageUrl()).willReturn("http://image.url/" + cardId);
        given(card.getHeight()).willReturn(170);
        given(card.getWeight()).willReturn(65);
        given(card.getTags()).willReturn(null);
        given(card.getCardProducts()).willReturn(List.of());

        return card;
    }
}
