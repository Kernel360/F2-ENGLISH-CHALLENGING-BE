package com.echall.platform.scrap.controller;

import static com.echall.platform.message.response.ScrapResponseCode.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.echall.platform.message.ApiCustomResponse;
import com.echall.platform.message.ResponseEntityFactory;
import com.echall.platform.oauth2.domain.info.OAuth2UserPrincipal;
import com.echall.platform.scrap.domain.dto.ScrapRequestDto;
import com.echall.platform.scrap.domain.dto.ScrapResponseDto;
import com.echall.platform.scrap.service.ScrapService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/scrap")
@Tag(name = "Scrap - private API", description = "스크랩 회원전용 API")
public class ScrapApiController {
	private final ScrapService scrapService;

	@GetMapping("/view")
	@Operation(summary = "스크랩 조회", description = "전체 스크랩 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content),
		@ApiResponse(responseCode = "204", description = "요청한 스크랩이 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	public ResponseEntity<ApiCustomResponse<Map<String, List<ScrapResponseDto.ScrapViewResponseDto>>>> getScraps(
		@AuthenticationPrincipal OAuth2UserPrincipal oAuth2UserPrincipal
	) {
		Map<String, List<ScrapResponseDto.ScrapViewResponseDto>> data = new HashMap<>();
		data.put("scrapList", scrapService.getAllScraps(oAuth2UserPrincipal.getId()));

		return ResponseEntityFactory.toResponseEntity(SCRAP_VIEW_SUCCESS, data);
	}

	@PostMapping("/create")
	@Operation(summary = "스크랩 생성", description = "새로운 스크랩을 생성합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content),
		@ApiResponse(responseCode = "204", description = "요청한 스크랩이 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	public ResponseEntity<ApiCustomResponse<ScrapResponseDto.ScrapCreateResponseDto>> createScrap(
		@AuthenticationPrincipal OAuth2UserPrincipal oAuth2UserPrincipal,
		@RequestBody ScrapRequestDto.ScrapCreateRequestDto requestDto
	) {
		return ResponseEntityFactory.toResponseEntity(
			SCRAP_CREATE_SUCCESS, scrapService.createScrap(oAuth2UserPrincipal.getEmail(), requestDto)
		);
	}

	@DeleteMapping("/delete")
	@Operation(summary = "스크랩 삭제", description = "스크랩을 삭제합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content),
		@ApiResponse(responseCode = "204", description = "요청한 스크랩이 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	public ResponseEntity<ApiCustomResponse<Object>> deleteScrap(
		@AuthenticationPrincipal OAuth2UserPrincipal oAuth2UserPrincipal,
		@RequestBody ScrapRequestDto.ScrapDeleteRequestDto requestDto
	) {
		scrapService.deleteScrap(oAuth2UserPrincipal.getId(), requestDto);
		return ResponseEntityFactory.toResponseEntity(SCRAP_DELETE_SUCCESS, null);
	}

}
