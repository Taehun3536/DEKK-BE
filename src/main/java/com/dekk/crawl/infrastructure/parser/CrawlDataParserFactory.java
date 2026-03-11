package com.dekk.crawl.infrastructure.parser;

import com.dekk.card.domain.model.enums.Platform;
import com.dekk.crawl.domain.exception.CrawlBusinessException;
import com.dekk.crawl.domain.exception.CrawlErrorCode;
import com.dekk.crawl.domain.parser.CrawlDataParser;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CrawlDataParserFactory {

    private final Map<Platform, CrawlDataParser> parsers;

    public CrawlDataParserFactory(List<CrawlDataParser> parserList) {
        this.parsers =
                parserList.stream().collect(Collectors.toMap(CrawlDataParser::supportedPlatform, Function.identity()));
    }

    public CrawlDataParser getParser(Platform platform) {
        CrawlDataParser parser = parsers.get(platform);
        if (parser == null) {
            throw new CrawlBusinessException(CrawlErrorCode.UNSUPPORTED_PLATFORM);
        }
        return parser;
    }
}
