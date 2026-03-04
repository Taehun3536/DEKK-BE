package com.dekk.crawl.presentation.controller;

import com.dekk.common.error.ErrorResponse;
import com.dekk.common.response.ApiResponse;
import com.dekk.crawl.presentation.dto.request.CrawlBatchCreateRequest;
import com.dekk.crawl.presentation.dto.response.CrawlBatchCreateResponse;
import com.dekk.crawl.presentation.dto.request.CrawlRawDataCreateRequest;
import com.dekk.crawl.presentation.dto.response.CrawlRawDataCreateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "크롤링 배치 API", description = "크롤링 배치 API")
public interface CrawlApi {

    @Operation(summary = "크롤링 배치 생성", description = "새로운 크롤링 배치를 생성하고 batchId를 발급합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "배치가 생성되었습니다",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CrawlBatchCreateResponse.class),
                            examples = @ExampleObject(value = """
                                    {"code": "SCR20101", "message": "배치가 생성되었습니다", "data": {"batchId": 1, "platform": "MUSINSA", "status": "COLLECTING"}}
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "플랫폼은 필수 값입니다",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {"code": "ECR40004", "message": "플랫폼은 필수 값입니다"}
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponse<CrawlBatchCreateResponse>> createBatch(CrawlBatchCreateRequest request);

    @Operation(summary = "원본 데이터 추가", description = "배치에 크롤링 원본 데이터를 추가합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "원본 데이터가 수신되었습니다",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CrawlRawDataCreateResponse.class),
                            examples = @ExampleObject(value = """
                                    {"code": "SCR20102", "message": "원본 데이터가 수신되었습니다", "data": {"rawDataId": 1, "status": "PENDING"}}
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "수집 중인 배치가 아닙니다",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {"code": "ECR40001", "message": "수집 중인 배치가 아닙니다"}
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "배치를 찾을 수 없습니다",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {"code": "ECR40401", "message": "배치를 찾을 수 없습니다"}
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponse<CrawlRawDataCreateResponse>> addRawData(
            @Parameter(description = "배치 ID", in = ParameterIn.PATH) @PathVariable Long batchId,
            CrawlRawDataCreateRequest request
    );

    @Operation(summary = "배치 수집 완료", description = "배치의 데이터 수집을 완료 처리합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "수집이 완료되었습니다",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = """
                                    {"code": "SCR20001", "message": "수집이 완료되었습니다"}
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "수집 중인 배치가 아닙니다",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {"code": "ECR40001", "message": "수집 중인 배치가 아닙니다"}
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "배치를 찾을 수 없습니다",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {"code": "ECR40401", "message": "배치를 찾을 수 없습니다"}
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponse<Void>> completeBatch(
            @Parameter(description = "배치 ID", in = ParameterIn.PATH) @PathVariable Long batchId
    );
}
