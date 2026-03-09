package com.dekk.deck.application;

import com.dekk.deck.domain.exception.DeckBusinessException;
import com.dekk.deck.domain.exception.DeckErrorCode;
import com.dekk.deck.domain.model.Deck;
import com.dekk.deck.domain.model.DeckCard;
import com.dekk.deck.domain.repository.DeckCardRepository;
import com.dekk.deck.domain.repository.DeckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeckCardCommandService {

    private static final int MAX_CUSTOM_DECK_CARD_COUNT = 50;

    private final DeckRepository deckRepository;
    private final DeckCardRepository deckCardRepository;

    public void saveToDefaultDeck(Long userId, Long cardId) {
        Deck defaultDeck = getDefaultDeckByUserId(userId);

        if (isCardAlreadyInDeck(defaultDeck.getId(), cardId)) {
            return;
        }

        deckCardRepository.save(DeckCard.create(defaultDeck.getId(), cardId));
    }

    public void removeFromDefaultDeck(Long userId, Long cardId) {
        Deck defaultDeck = getDefaultDeckByUserId(userId);
        DeckCard deckCard = getDeckCardByDeckIdAndCardId(defaultDeck.getId(), cardId);

        deckCardRepository.delete(deckCard);
    }

    public void saveToCustomDeck(Long userId, Long customDeckId, Long cardId) {
        Deck customDeck = getCustomDeckByUserId(customDeckId, userId);

        if (isCardAlreadyInDeck(customDeck.getId(), cardId)) {
            return;
        }

        validateCustomDeckCardLimit(customDeck.getId());

        deckCardRepository.save(DeckCard.create(customDeck.getId(), cardId));
    }

    public void removeFromCustomDeck(Long userId, Long customDeckId, Long cardId) {
        Deck customDeck = getCustomDeckByUserId(customDeckId, userId);
        DeckCard deckCard = getDeckCardByDeckIdAndCardId(customDeck.getId(), cardId);

        deckCardRepository.delete(deckCard);
    }

    private boolean isCardAlreadyInDeck(Long deckId, Long cardId) {
        return deckCardRepository.existsByDeckIdAndCardId(deckId, cardId);
    }

    private Deck getDefaultDeckByUserId(Long userId) {
        return deckRepository.findByUserIdAndIsDefaultTrue(userId)
            .orElseThrow(() -> new DeckBusinessException(DeckErrorCode.DEFAULT_DECK_NOT_FOUND));
    }

    private Deck getCustomDeckByUserId(Long deckId, Long userId) {
        return deckRepository.findByIdAndUserId(deckId, userId)
            .orElseThrow(() -> new DeckBusinessException(DeckErrorCode.CUSTOM_DECK_NOT_FOUND));
    }

    private DeckCard getDeckCardByDeckIdAndCardId(Long deckId, Long cardId) {
        return deckCardRepository.findByDeckIdAndCardId(deckId, cardId)
            .orElseThrow(() -> new DeckBusinessException(DeckErrorCode.CARD_NOT_FOUND_IN_DECK));
    }

    private void validateCustomDeckCardLimit(Long deckId) {
        long currentCardCount = deckCardRepository.countByDeckId(deckId);
        if (currentCardCount >= MAX_CUSTOM_DECK_CARD_COUNT) {
            throw new DeckBusinessException(DeckErrorCode.CUSTOM_DECK_CARD_LIMIT_EXCEEDED);
        }
    }
}
