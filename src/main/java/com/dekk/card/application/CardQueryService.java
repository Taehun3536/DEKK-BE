package com.dekk.card.application;

import com.dekk.card.application.dto.query.RecommendCandidateQuery;
import com.dekk.card.application.dto.result.GuestCardResult;
import com.dekk.card.application.dto.result.MemberCardResult;
import com.dekk.card.domain.model.Card;
import com.dekk.card.domain.model.enums.CardStatus;
import com.dekk.card.domain.repository.CardRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardQueryService {

    private final CardRepository cardRepository;

    public Page<GuestCardResult> getCardsForGuest(Pageable pageable) {
        return cardRepository
                .findCardsWithImageByStatus(CardStatus.APPROVED, pageable)
                .map(GuestCardResult::from);
    }

    public Page<MemberCardResult> getCardsForMember(Pageable pageable) {
        return cardRepository
                .findCardsWithProductsByStatus(CardStatus.APPROVED, pageable)
                .map(MemberCardResult::from);
    }

    public List<MemberCardResult> getCardsByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        return cardRepository.findAllByIdInWithProducts(ids).stream()
                .map(MemberCardResult::from)
                .toList();
    }

    public List<Card> getRecommendCandidates(RecommendCandidateQuery query) {
        return cardRepository.findRecommendCandidates(query);
    }
}
