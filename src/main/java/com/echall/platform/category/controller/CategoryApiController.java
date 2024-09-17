package com.echall.platform.category.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.echall.platform.category.domain.dto.CategoryRequestDto;
import com.echall.platform.category.domain.dto.CategoryResponseDto;
import com.echall.platform.category.service.CategoryService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * admin, 회원 전용 API
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Tag(name = "Category - private API", description = "카테고리 회원전용 API")
public class CategoryApiController {
	/**
	 *
	 * 	private final CategoryService categoryService;
	 *
	 * 	    @GetMapping("/all")
	 *    @Operation(summary = "전체 카테고리 조회", description = "전체 카테고리를 조회합니다.")
	 *    @ApiResponses(value = {
	 *        @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = {
	 *            @Content(mediaType = "application/json", schema = @Schema(implementation = ContentPageResponse.class))
	 *        }),
	 *        @ApiResponse(responseCode = "204", description = "카테고리가 없습니다.", content = @Content),
	 *        @ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	 *    })
	 * 	public List<CategoryResponseDto> findAllCategory() {
	 * 		return categoryService.findAllCategory();
	 *    }
	 */

	private final CategoryService categoryService;

	@PostMapping("/new")
	public CategoryResponseDto save(@RequestBody CategoryRequestDto requestDto) {
		return categoryService.createCategory(requestDto);
	}
}
