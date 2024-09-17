package com.echall.platform.content.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.echall.platform.content.domain.dto.ContentPageResponse;
import com.echall.platform.content.domain.dto.ContentResponseDto;
import com.echall.platform.content.service.ContentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/p1/contents")
@Tag(name = "Content API", description = "컨텐츠 공통 API")
public class ContentPublicController {

	private final ContentService contentService;

	@Autowired
	public ContentPublicController(ContentService contentService) {
		this.contentService = contentService;
	}

	/**
	 * 컨텐츠 조회
	 * (pageable)
	 */
	@GetMapping("/view")
	@Operation(summary = "컨텐츠 조회", description = "페이지네이션을 적용하여 전체 컨텐츠 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = ContentPageResponse.class))
		}),
		@ApiResponse(responseCode = "204", description = "컨텐츠가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	@Parameters({
		@Parameter(name = "page", description = "페이지 번호 (0부터 시작)", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")),
		@Parameter(name = "size", description = "페이지당 데이터 수", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "10")),
		@Parameter(name = "sort", description = "정렬 기준 (예: createdAt,desc)", in = ParameterIn.QUERY, schema = @Schema(type = "string"))
	})
	public ResponseEntity<ContentPageResponse> get(
		@Parameter(hidden = true) @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable)
	{
		Page<ContentResponseDto> pageContentList = contentService.get(pageable);
		ContentPageResponse response = new ContentPageResponse();
		response.setPageNumber(pageContentList.getNumber());
		response.setPageSize(pageContentList.getSize());
		response.setTotalPages(pageContentList.getTotalPages());
		response.setTotalElements(pageContentList.getTotalElements());
		response.setContent(pageContentList.getContent());

		return ResponseEntity.ok().body(response);
	}

	/**
	 * 컨텐츠 상세조회
	 */

	/**
	 * 컨첸츠 검색
	 */
}
