package com.dekk.card.application.dto.query;

import com.dekk.card.domain.model.enums.TargetGender;
import java.util.List;

public record RecommendCandidateQuery(
        List<TargetGender> genders, int minHeight, int maxHeight, int minWeight, int maxWeight) {}
