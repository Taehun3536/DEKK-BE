package com.dekk.crawl.domain.parser;

import com.dekk.card.application.command.CardCreateCommand;
import com.dekk.card.domain.model.enums.Platform;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;

public interface CrawlDataParser {

    Platform supportedPlatform();

    List<CardCreateCommand> parse(String rawData) throws JsonProcessingException;
}
