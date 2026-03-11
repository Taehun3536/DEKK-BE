package com.dekk.activelog.application.dto.command;

import com.dekk.activelog.domain.model.SwipeType;

public record SwipeCommand(Long userId, Long cardId, SwipeType swipeType) {}
