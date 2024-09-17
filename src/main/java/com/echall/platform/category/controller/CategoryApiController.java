package com.echall.platform.category.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.echall.platform.category.domain.dto.CategoryRequestDto;
import com.echall.platform.category.domain.dto.CategoryResponseDto;
import com.echall.platform.category.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * admin, 회원 전용 API
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/t1/categories")
@Tag(name = "Category - private API", description = "카테고리 회원전용 API")
public class CategoryApiController {

	private final CategoryService categoryService;

	@PostMapping("/new")
	@Operation(summary = "어드민 - 새로운 카테고리 추가", description = "어드민 회원이 새로운 카테고리를 추가합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "카테고리가 성공적으로 추가되었습니다.", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponseDto.class))
		}),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "403", description = "접근 권한이 없습니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content(mediaType = "application/json"))
	})
	public CategoryResponseDto save(@RequestBody CategoryRequestDto requestDto) {
		return categoryService.createCategory(requestDto);
	}


	@PatchMapping("/modify/{id}")
	@Operation(summary = "어드민 - 카테고리 수정", description = "어드민 회원이 카테고리를 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "카테고리가 성공적으로 추가되었습니다.", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponseDto.class))
		}),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "403", description = "접근 권한이 없습니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<CategoryResponseDto> update(
		@PathVariable Long id,
		@RequestBody CategoryRequestDto requestDto
	) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(categoryService.updateCategory(id, requestDto));
	}
}
