package com.dekk.card.application;

import com.dekk.card.domain.exception.CardBusinessException;
import com.dekk.card.domain.exception.CardErrorCode;
import com.dekk.card.domain.model.Card;
import com.dekk.card.domain.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CardCommandService {

    private final CardRepository cardRepository;

    public void approveCard(Long cardId) {
        Card card = getCard(cardId);
        card.approve();
    }

    public void rejectCard(Long cardId) {
        Card card = getCard(cardId);
        card.reject();
    }

    private Card getCard(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new CardBusinessException(CardErrorCode.CARD_NOT_FOUND));
    }
}
