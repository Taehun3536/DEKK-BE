package com.dekk.deck.presentation.request;

import com.dekk.deck.application.dto.command.CustomDeckCreateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomDeckCreateRequest(
    @Schema(description = "생성할 커스텀 보관함 이름", example = "여름 코디 모음")
    @NotBlank(message = "보관함 이름은 필수입니다.")
    @Size(min = 1, max = 15, message = "보관함 이름은 1자 이상 15자 이내여야 합니다.")
    String name
) {
    public CustomDeckCreateCommand toCommand() {
        return new CustomDeckCreateCommand(name);
    }
}
