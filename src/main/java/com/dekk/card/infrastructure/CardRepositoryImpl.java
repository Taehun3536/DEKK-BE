package com.dekk.card.infrastructure;

import com.dekk.card.application.dto.query.RecommendCandidateQuery;
import com.dekk.card.domain.model.Card;
import com.dekk.card.domain.model.enums.CardStatus;
import com.dekk.card.domain.model.enums.Platform;
import com.dekk.card.domain.repository.CardRepository;
import com.dekk.card.infrastructure.jpa.CardJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepository {
    private final CardJpaRepository cardJpaRepository;

    @Override
    public Card save(Card card) {
        return cardJpaRepository.save(card);
    }

    @Override
    public List<Card> saveAll(List<Card> cards) {
        return cardJpaRepository.saveAll(cards);
    }

    @Override
    public boolean existsByPlatformAndOriginId(Platform platform, String originId) {
        return cardJpaRepository.existsByPlatformAndOriginId(platform, originId);
    }

    @Override
    public Page<Card> findCardsWithImageByStatus(CardStatus status, Pageable pageable) {
        return cardJpaRepository.findCardsWithImageByStatus(status, pageable);
    }

    @Override
    public Page<Card> findCardsWithProductsByStatus(CardStatus status, Pageable pageable) {
        Page<Long> idPage = cardJpaRepository.findCardIdsByStatus(status, pageable);

        List<Long> cardIds = idPage.getContent();
        if (cardIds.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, idPage.getTotalElements());
        }

        List<Card> cardsWithProducts = cardJpaRepository.findAllByIdInWithProducts(cardIds);

        return new PageImpl<>(cardsWithProducts, pageable, idPage.getTotalElements());
    }

    @Override
    public List<Card> findAllByIdInWithProducts(List<Long> ids) {
        return cardJpaRepository.findAllByIdInWithProducts(ids);
    }

    @Override
    public List<Card> findRecommendCandidates(RecommendCandidateQuery query) {
        return cardJpaRepository.findRecommendCandidates(
                query.genders(), query.minHeight(), query.maxHeight(), query.minWeight(), query.maxWeight());
    }

    @Override
    public Optional<Card> findById(Long id) {
        return cardJpaRepository.findById(id);
    }
}
