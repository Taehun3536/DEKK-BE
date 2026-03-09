package com.dekk.card.domain.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TargetGender {
    WOMEN("여성"),
    MEN("남성"),
    OTHER("남녀공용"),
    UNDEFINED("미정");

    private final String description;

    public static TargetGender musinsaParse(String value) {
        if (value == null || value.isBlank()) {
            return UNDEFINED;
        }

        return switch (value.trim().toUpperCase()) {
            case "WOMEN" -> TargetGender.WOMEN;
            case "MEN" -> TargetGender.MEN;
            case "ALL" -> TargetGender.OTHER;
            default -> TargetGender.UNDEFINED;
        };
    }
}
