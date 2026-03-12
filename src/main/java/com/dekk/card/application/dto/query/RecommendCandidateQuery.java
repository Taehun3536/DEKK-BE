package com.dekk.card.application.dto.query;

import com.dekk.card.domain.model.enums.TargetGender;
import java.util.List;

public record RecommendCandidateQuery(
        List<TargetGender> genders, int minHeight, int maxHeight, int minWeight, int maxWeight) {
    private static final int HEIGHT_RANGE = 5;
    private static final int WEIGHT_RANGE = 7;
    private static final int DEFAULT_MIN = 0;
    private static final int DEFAULT_MAX = 999;

    public static RecommendCandidateQuery of(List<TargetGender> genders, Integer height, Integer weight) {
        return new RecommendCandidateQuery(
                genders,
                height != null ? height - HEIGHT_RANGE : DEFAULT_MIN,
                height != null ? height + HEIGHT_RANGE : DEFAULT_MAX,
                weight != null ? weight - WEIGHT_RANGE : DEFAULT_MIN,
                weight != null ? weight + WEIGHT_RANGE : DEFAULT_MAX);
    }
}
