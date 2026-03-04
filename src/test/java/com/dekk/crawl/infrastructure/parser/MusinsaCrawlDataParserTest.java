package com.dekk.crawl.infrastructure.parser;

import com.dekk.card.application.command.CardCreateCommand;
import com.dekk.card.application.command.ProductCreateCommand;
import com.dekk.card.domain.model.enums.Platform;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MusinsaCrawlDataParserTest {

    private MusinsaCrawlDataParser parser;

    @BeforeEach
    void setUp() {
        parser = new MusinsaCrawlDataParser(new ObjectMapper());
    }

    @Test
    @DisplayName("supportedPlatform은 MUSINSA를 반환한다")
    void supportedPlatform() {
        assertThat(parser.supportedPlatform()).isEqualTo(Platform.MUSINSA);
    }

    @Nested
    @DisplayName("실제 무신사 데이터 파싱")
    class ParseRealData {

        private static final String RAW_DATA = """
                [
                  {
                    "id": "1475883068452566428",
                    "createdBy": { "id": "1229453110790000259" },
                    "contentType": "USER_SNAP",
                    "formatType": "POST",
                    "detail": {
                      "title": "",
                      "content": "#광고 #무신사 #어반디타입 #오늘의스냅",
                      "formatType": "POST"
                    },
                    "model": {
                      "gender": "WOMEN",
                      "age": null,
                      "height": 168,
                      "weight": 44,
                      "skinTone": "NONE"
                    },
                    "goods": [
                      {
                        "id": 1475883068452566500,
                        "isMatched": true,
                        "goodsPlatform": "MUSINSA",
                        "goodsNo": "5916242",
                        "options": [
                          { "id": 1475883068452566500, "depth": 1, "optionName": "M" }
                        ]
                      }
                    ],
                    "tags": [
                      {"name": "개강룩"}, {"name": "개강코디"}, {"name": "광고"},
                      {"name": "꾸안꾸"}, {"name": "무신사"}, {"name": "어반디타입"},
                      {"name": "오늘의스냅"}, {"name": "출근룩"}
                    ],
                    "medias": [
                      {
                        "id": 1475883068452566500,
                        "type": "IMAGE",
                        "path": "https://image.msscdn.net/thumbnails/snap/images/2026/02/25/73ffa7fff50d45d28375464b1d801dab.jpg",
                        "videoId": null
                      },
                      {
                        "id": 1475883068452566500,
                        "type": "IMAGE",
                        "path": "https://image.msscdn.net/thumbnails/snap/images/2026/02/25/b465276a555a4351b81f8131f4ea57fd.jpg",
                        "videoId": null
                      }
                    ],
                    "status": {
                      "snapDisplayStatus": "DISPLAY",
                      "snapAdminCheckStatus": "CHECKED"
                    },
                    "goods_detail_list": [
                      {
                        "goodsNo": "5916242",
                        "platform": "MUSINSA",
                        "goodsName": "파리스 스트라이프 롱슬리브 폴로 티셔츠_버건디",
                        "price": 32990,
                        "normalPrice": 56000,
                        "discountRate": 41,
                        "brandName": "어반디타입",
                        "imageUrl": "https://image.msscdn.net/thumbnails/images/goods_img/20260120/5916242/5916242_17701917627149_500.jpg",
                        "linkUrl": "https://www.musinsa.com/products/5916242",
                        "saleStat": "SALE"
                      }
                    ]
                  }
                ]
                """;

        @Test
        @DisplayName("snap 1건을 CardCreateCommand 1건으로 파싱한다")
        void parseSingleSnap() throws JsonProcessingException {
            List<CardCreateCommand> result = parser.parse(RAW_DATA);

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("originId를 올바르게 파싱한다")
        void parseOriginId() throws JsonProcessingException {
            CardCreateCommand command = parser.parse(RAW_DATA).get(0);

            assertThat(command.originId()).isEqualTo("1475883068452566428");
        }

        @Test
        @DisplayName("platform은 MUSINSA로 고정된다")
        void parsePlatform() throws JsonProcessingException {
            CardCreateCommand command = parser.parse(RAW_DATA).get(0);

            assertThat(command.platform()).isEqualTo(Platform.MUSINSA);
        }

        @Test
        @DisplayName("model의 height, weight를 파싱한다")
        void parseModelInfo() throws JsonProcessingException {
            CardCreateCommand command = parser.parse(RAW_DATA).get(0);

            assertThat(command.height()).isEqualTo(168);
            assertThat(command.weight()).isEqualTo(44);
        }

        @Test
        @DisplayName("tags를 쉼표로 구분하여 파싱한다")
        void parseTags() throws JsonProcessingException {
            CardCreateCommand command = parser.parse(RAW_DATA).get(0);

            assertThat(command.tags()).isEqualTo("개강룩,개강코디,광고,꾸안꾸,무신사,어반디타입,오늘의스냅,출근룩");
        }

        @Test
        @DisplayName("goods_detail_list에서 상품 정보를 파싱한다")
        void parseProduct() throws JsonProcessingException {
            CardCreateCommand command = parser.parse(RAW_DATA).get(0);
            List<ProductCreateCommand> products = command.productCreateCommands();

            assertThat(products).hasSize(1);

            ProductCreateCommand product = products.get(0);
            assertThat(product.originId()).isEqualTo("5916242");
            assertThat(product.brand()).isEqualTo("어반디타입");
            assertThat(product.name()).isEqualTo("파리스 스트라이프 롱슬리브 폴로 티셔츠_버건디");
            assertThat(product.price()).isEqualTo(32990);
            assertThat(product.productUrl()).isEqualTo("https://www.musinsa.com/products/5916242");
        }

        @Test
        @DisplayName("goods의 options에서 goodsNo 매칭으로 옵션을 파싱한다")
        void parseProductOption() throws JsonProcessingException {
            CardCreateCommand command = parser.parse(RAW_DATA).get(0);
            ProductCreateCommand product = command.productCreateCommands().get(0);

            assertThat(product.option()).isEqualTo("M");
        }

        @Test
        @DisplayName("goods의 isMatched가 true이면 isSimilar는 false이다")
        void parseIsSimilar() throws JsonProcessingException {
            CardCreateCommand command = parser.parse(RAW_DATA).get(0);
            ProductCreateCommand product = command.productCreateCommands().get(0);

            assertThat(product.isSimilar()).isFalse();
        }
    }

    @Nested
    @DisplayName("엣지 케이스")
    class EdgeCases {

        @Test
        @DisplayName("id가 없는 snap은 건너뛴다")
        void skipSnapWithoutId() throws JsonProcessingException {
            String rawData = """
                    [{"model": {}, "goods": [], "tags": [], "medias": [],
                      "status": {"snapDisplayStatus": "DISPLAY"}, "goods_detail_list": []}]
                    """;

            List<CardCreateCommand> result = parser.parse(rawData);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("tags가 빈 배열이면 null을 반환한다")
        void emptyTags() throws JsonProcessingException {
            String rawData = """
                    [{"id": "123", "model": {}, "goods": [], "tags": [], "medias": [],
                      "status": {"snapDisplayStatus": "DISPLAY"}, "goods_detail_list": []}]
                    """;

            CardCreateCommand command = parser.parse(rawData).get(0);

            assertThat(command.tags()).isNull();
        }

        @Test
        @DisplayName("medias가 비어있으면 카드 이미지 originUrl은 null이다")
        void emptyMedias() throws JsonProcessingException {
            String rawData = """
                    [{"id": "123", "model": {}, "goods": [], "tags": [], "medias": [],
                      "status": {"snapDisplayStatus": "DISPLAY"}, "goods_detail_list": []}]
                    """;

            CardCreateCommand command = parser.parse(rawData).get(0);

            assertThat(command.cardImage().originUrl()).isNull();
        }

        @Test
        @DisplayName("model 정보가 없으면 height, weight는 null이다")
        void emptyModel() throws JsonProcessingException {
            String rawData = """
                    [{"id": "123", "model": {}, "goods": [], "tags": [], "medias": [],
                      "status": {"snapDisplayStatus": "DISPLAY"}, "goods_detail_list": []}]
                    """;

            CardCreateCommand command = parser.parse(rawData).get(0);

            assertThat(command.height()).isNull();
            assertThat(command.weight()).isNull();
        }

        @Test
        @DisplayName("잘못된 JSON이면 CrawlBusinessException을 던진다")
        void invalidJson() {
            assertThatThrownBy(() -> parser.parse("invalid json"))
                    .isInstanceOf(JsonProcessingException.class);
        }

        @Test
        @DisplayName("goods에서 isMatched가 false이면 isSimilar는 true이다")
        void unmatchedGoodsIsSimilar() throws JsonProcessingException {
            String rawData = """
                    [{"id": "123", "model": {}, "tags": [], "medias": [],
                      "status": {"snapDisplayStatus": "DISPLAY"},
                      "goods": [{"goodsNo": "100", "isMatched": false, "options": []}],
                      "goods_detail_list": [{"goodsNo": "100", "goodsName": "테스트", "price": 1000,
                        "brandName": "브랜드", "imageUrl": "https://img.com/1.jpg", "linkUrl": "https://link.com"}]
                    }]
                    """;

            CardCreateCommand command = parser.parse(rawData).get(0);
            ProductCreateCommand product = command.productCreateCommands().get(0);

            assertThat(product.isSimilar()).isTrue();
        }

        @Test
        @DisplayName("키가 0이면 Null로 파싱한다.")
        void heightZeroParsedAsNull() throws JsonProcessingException {
            String rawData = """
                    [{"id": "123", "model": {"height": 0}, "goods": [], "tags": [], "medias": [],
                       "status": {"snapDisplayStatus": "DISPLAY"}, "goods_detail_list": []}]
                    """;

            CardCreateCommand command = parser.parse(rawData).get(0);
            assertThat(command.height()).isNull();
        }

        @Test
        @DisplayName("몸무게가 0이면 Null로 파싱한다.")
        void weightZeroParsedAsNull() throws JsonProcessingException {
            String rawData = """
                    [{"id": "123", "model": {"weight": 0}, "goods": [], "tags": [], "medias": [],
                       "status": {"snapDisplayStatus": "DISPLAY"}, "goods_detail_list": []}]
                    """;

            CardCreateCommand command = parser.parse(rawData).get(0);
            assertThat(command.weight()).isNull();
        }

        @Test
        @DisplayName("키가 공백이면 Null로 파싱한다.")
        void heightEmptyParsedAsNull() throws JsonProcessingException {
            String rawData = """
                    [{"id": "123", "model": {"height": " "}, "goods": [], "tags": [], "medias": [],
                       "status": {"snapDisplayStatus": "DISPLAY"}, "goods_detail_list": []}]
                    """;

            CardCreateCommand command = parser.parse(rawData).get(0);
            assertThat(command.height()).isNull();
        }

        @Test
        @DisplayName("몸무게가 공백이면 Null로 파싱한다.")
        void weightEmptyParsedAsNull() throws JsonProcessingException {
            String rawData = """
                    [{"id": "123", "model": {"weight": " "}, "goods": [], "tags": [], "medias": [],
                       "status": {"snapDisplayStatus": "DISPLAY"}, "goods_detail_list": []}]
                    """;

            CardCreateCommand command = parser.parse(rawData).get(0);
            assertThat(command.weight()).isNull();
        }

        @Test
        @DisplayName("상품 리스트와 상세 정보의 goodsNo가 일치하지 않으면 상품 정보를 제외한다")
        void goodsNoMismatchTest() throws JsonProcessingException {
            String rawData = """
                    [
                      {
                        "id": "123",
                        "status": { "snapDisplayStatus": "DISPLAY" },
                        "goods": [{ "goodsNo": "5916242" }], 
                        "goods_detail_list": [
                          { "goodsNo": "9999999", "goodsName": "매칭 안될 상품" }
                        ]
                      }
                    ]
                    """;

            CardCreateCommand command = parser.parse(rawData).get(0);

            assertThat(command.productCreateCommands()).isEmpty();
        }
    }

}
