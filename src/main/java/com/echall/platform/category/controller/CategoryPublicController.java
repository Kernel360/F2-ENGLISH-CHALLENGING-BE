package com.echall.platform.category.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
 * 카테고리 공통 API
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category - public API", description = "카테고리 공통 API")
public class CategoryPublicController {

	private final CategoryService categoryService;

	@GetMapping("/all")
	@Operation(summary = "전체 카테고리 조회", description = "전체 카테고리를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponseDto.class))
		}),
		@ApiResponse(responseCode = "204", description = "카테고리가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	public ResponseEntity<List<CategoryResponseDto>> findAllCategory() {
		List<CategoryResponseDto> categories = categoryService.findAllCategory();
		if (categories.isEmpty()) {
			return ResponseEntity.noContent().build(); //204 : no content
		}
		return ResponseEntity.status(HttpStatus.OK).body(categories);
	}

	@GetMapping("/{id}")
	@Operation(summary = "카테고리 id로 조회", description = "단일 카테고리를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponseDto.class))
		}),
		@ApiResponse(responseCode = "204", description = "카테고리가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	public ResponseEntity<CategoryResponseDto> findCategoryById(
		@PathVariable("id") Long id
	) {
		CategoryResponseDto category = categoryService.findCategoryById(id);
		if (category == null) {
			return ResponseEntity.noContent().build(); //204 : no content (해당하는 카테고리가 없을 때)
		}
		return ResponseEntity.status(HttpStatus.OK).body(category);
	}

}
