package com.dekk.card.application.command;

import com.dekk.card.domain.model.enums.Platform;

import com.dekk.card.domain.model.enums.TargetGender;
import java.util.List;

public record CardCreateCommand(
    CardImageCreateCommand cardImage,
    List<ProductCreateCommand> productCreateCommands,
    String tags,
    String originId,
    Platform platform,
    TargetGender targetGender,
    Integer height,
    Integer weight
) {
}
