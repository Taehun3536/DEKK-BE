package com.dekk.card.presentation.controller;

import com.dekk.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "관리자 카드 상태 관리 API", description = "관리자가 카드를 승인/반려하는 API")
public interface CardCommandApi {

    @Operation(summary = "카드 승인", description = "PENDING 또는 REJECTED 상태의 카드를 승인합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "카드 승인 성공",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        examples = @ExampleObject(value = """
                                    {
                                      "code": "SC200003",
                                      "message": "카드 승인 성공"
                                    }"""))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "삭제 요청된 카드 상태 변경 불가",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        examples = @ExampleObject(value = """
                                    {
                                      "code": "EC40009",
                                      "message": "삭제 요청된 카드의 상태는 변경할 수 없습니다"
                                    }"""))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "카드를 찾을 수 없음",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        examples = @ExampleObject(value = """
                                    {
                                      "code": "EC40401",
                                      "message": "카드를 찾을 수 없습니다"
                                    }""")))
            })
    ResponseEntity<ApiResponse<Void>> approveCard(
            @Parameter(description = "승인할 카드 ID", in = ParameterIn.PATH) Long cardId);

    @Operation(summary = "카드 반려", description = "PENDING 또는 APPROVED 상태의 카드를 반려합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "카드 반려 성공",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        examples = @ExampleObject(value = """
                                    {
                                      "code": "SC200004",
                                      "message": "카드 반려 성공"
                                    }"""))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "삭제 요청된 카드 상태 변경 불가",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        examples = @ExampleObject(value = """
                                    {
                                      "code": "EC40009",
                                      "message": "삭제 요청된 카드의 상태는 변경할 수 없습니다"
                                    }"""))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "카드를 찾을 수 없음",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        examples = @ExampleObject(value = """
                                    {
                                      "code": "EC40401",
                                      "message": "카드를 찾을 수 없습니다"
                                    }""")))
            })
    ResponseEntity<ApiResponse<Void>> rejectCard(
            @Parameter(description = "반려할 카드 ID", in = ParameterIn.PATH) Long cardId);
}
