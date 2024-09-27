package com.echall.platform.crawling.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.echall.platform.crawling.domain.dto.CrawlingRequestDto;
import com.echall.platform.crawling.domain.dto.CrawlingResponseDto;
import com.echall.platform.crawling.service.CrawlingServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cr1/crawling")
@Tag(name = "Crawling - private API", description = "크롤링 관리자 전용 API")
public class CrawlingController {
	private final CrawlingServiceImpl crawlingService;

	@GetMapping("/youtube")
	@Operation(summary = "유튜브 크롤링", description = "유튜브 크롤링을 통해 필요한 정보를 가져옵니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content),
		@ApiResponse(responseCode = "204", description = "컨텐츠가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	public ResponseEntity<CrawlingResponseDto.YoutubeResponseDto> crawlingYoutube(
		Authentication authentication, CrawlingRequestDto.YoutubeRequestDto youtubeRequestDto
	) throws Exception {
		// TODO: 비동기 처리 필요
		// Get YouTube Info - YouTube API
		// Get YouTube Script - Selenium
		return ResponseEntity.status(HttpStatus.OK)
			.body(
				crawlingService.getYoutubeInfo(youtubeRequestDto.youtubeUrl(), (String)authentication.getCredentials())
			);
	}

	@GetMapping("/article")
	@Operation(summary = "CNN 기사 크롤링", description = "CNN 기사 크롤링을 통해 필요한 정보를 가져옵니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content),
		@ApiResponse(responseCode = "204", description = "컨텐츠가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	public ResponseEntity<CrawlingResponseDto.CNNResponseDto> crawlingArticle(
		Authentication authentication, CrawlingRequestDto.CNNRequestDto cnnRequestDto
	) {

		return ResponseEntity.status(HttpStatus.OK)
			.body(crawlingService.getCNNInfo(cnnRequestDto.cnnUrl(), (String)authentication.getCredentials()));
	}

}
