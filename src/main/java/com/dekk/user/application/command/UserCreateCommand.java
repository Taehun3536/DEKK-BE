package com.dekk.user.application.command;

import com.dekk.user.domain.model.enums.Provider;

public record UserCreateCommand(String email, Provider provider, String providerId) {}
