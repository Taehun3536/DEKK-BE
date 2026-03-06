package com.dekk.activelog.application;

import com.dekk.activelog.application.dto.command.SwipeCommand;
import com.dekk.activelog.domain.model.ActiveLog;
import com.dekk.activelog.domain.model.SwipeType;
import com.dekk.activelog.domain.repository.ActiveLogRepository;
import com.dekk.deck.application.DeckCardCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ActiveLogCommandService {

    private final ActiveLogRepository activeLogRepository;
    private final DeckCardCommandService deckCardCommandService;

    public void saveSwipeAction(SwipeCommand command) {
        if (activeLogRepository.existsByUserIdAndCardId(command.userId(), command.cardId())) {
            return;
        }

        ActiveLog activeLog = ActiveLog.create(command.userId(), command.cardId(), command.swipeType());
        activeLogRepository.save(activeLog);

        if(command.swipeType() == SwipeType.LIKE){
            deckCardCommandService.saveToDefaultDeck(command.userId(), command.cardId());
        }
    }
}
