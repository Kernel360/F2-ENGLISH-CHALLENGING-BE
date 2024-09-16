package com.echall.platform.content.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.echall.platform.content.domain.dto.ContentRequestDto;
import com.echall.platform.content.domain.dto.ContentResponseDto;
import com.echall.platform.content.domain.dto.ContentUpdateRequestDto;
import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.content.service.ContentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * admin, 회원 전용 컨텐츠 controller
 */
@RestController
@RequestMapping("/api/a1/contents") //TODO: URL 수정 필요
@Tag(name = "Content - private API", description = "컨텐츠 회원전용 API")
public class ContentApiController {


	private final ContentService contentService;

	@Autowired
	public ContentApiController(ContentService contentService) {
		this.contentService = contentService;
	}

	/**
	 * 컨텐츠 등록
	 */
	//TODO : ADMIN 회원만 가능하도록 권한 관리가 필요합니다.
	@PostMapping("/new")
	@Operation(summary = "어드민 - 컨텐츠 등록", description = "어드민 회원이 컨텐츠를 새로 등록합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "컨텐츠가 성공적으로 생성되었습니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "403", description = "접근 권한이 없습니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<ContentResponseDto> newContent(@RequestBody ContentRequestDto contentRequest) {
		ContentEntity contentEntity = contentRequest.toEntity();
		ContentEntity createdContent = contentService.createContent(contentEntity);
		ContentResponseDto responseDto = ContentResponseDto.of(createdContent);

		return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
	}

	/**
	 * 컨텐츠 수정
	 */
	@PatchMapping("/modify")
	@Operation(summary = "어드민 - 컨텐츠 수정", description = "어드민 회원이 컨텐츠를 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "컨텐츠가 성공적으로 수정되었습니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "403", description = "접근 권한이 없습니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "404", description = "컨텐츠를 찾을 수 없습니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<ContentResponseDto> modifyContent(@RequestBody ContentUpdateRequestDto contentUpdateRequest) {
		// DTO를 엔티티로 변환
		ContentEntity updatedContent = contentUpdateRequest.toEntity();

		// 서비스 레이어에서 수정 로직 수행
		ContentEntity modifiedContent = contentService.updateContent(updatedContent);

		// 수정된 엔티티를 응답용 DTO로 변환
		ContentResponseDto responseDto = ContentResponseDto.of(modifiedContent);

		return ResponseEntity.ok(responseDto);
	}

	/**
	 * 컨텐츠 삭제 (비활성화)
	 */
	@DeleteMapping("/{id}")
	@Operation(summary = "어드민 - 컨텐츠 비활성화", description = "어드민 회원이 컨텐츠를 비활성화합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "컨텐츠가 성공적으로 비활성화되었습니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "404", description = "컨텐츠를 찾을 수 없습니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Void> deactivateContent(@PathVariable String id) {
		contentService.deactivateContent(id);
		return ResponseEntity.ok().build();
	}
}
