package com.dekk.user.application.command;

import com.dekk.user.domain.model.enums.Gender;

public record UserProfileUpdateCommand(String nickname, Integer height, Integer weight, Gender gender) {}
