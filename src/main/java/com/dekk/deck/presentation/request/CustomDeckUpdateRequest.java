package com.dekk.deck.presentation.request;

import com.dekk.deck.application.dto.command.CustomDeckUpdateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomDeckUpdateRequest(
        @Schema(description = "수정할 커스텀 보관함 이름", example = "가을 코디 모음")
        @NotBlank(message = "보관함 이름은 필수입니다.")
        @Size(min = 1, max = 15, message = "보관함 이름은 1자 이상 15자 이내여야 합니다.")
        String name) {
    public CustomDeckUpdateCommand toCommand() {
        return new CustomDeckUpdateCommand(name);
    }
}
