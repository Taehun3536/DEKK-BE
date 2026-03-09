package com.dekk.deck.domain.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeckType {
    DEFAULT("기본 보관함"),
    CUSTOM("커스텀 보관함");

    private final String description;
}
