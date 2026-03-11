package com.dekk.activelog.presentation.request;

import com.dekk.activelog.domain.model.SwipeType;
import jakarta.validation.constraints.NotNull;

public record SwipeRequest(
        @NotNull(message = "스와이프 타입(LIKE/DISLIKE)은 필수입니다.") SwipeType swipeType) {}
