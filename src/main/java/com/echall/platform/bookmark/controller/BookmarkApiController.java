package com.echall.platform.bookmark.controller;

import static com.echall.platform.message.response.BookmarkResponseCode.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.echall.platform.bookmark.domain.dto.BookmarkRequestDto;
import com.echall.platform.bookmark.domain.dto.BookmarkResponseDto;
import com.echall.platform.bookmark.service.BookmarkService;
import com.echall.platform.message.ApiCustomResponse;
import com.echall.platform.message.ResponseEntityFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/b1/bookmark")
@RequiredArgsConstructor
@RestController
@Tag(name = "Bookmark - private API", description = "북마크 회원전용 API")
public class BookmarkApiController {
	private final BookmarkService bookmarkService;

	@GetMapping("/view/{id}")
	@Operation(summary = "게시글 북마크 조회", description = "게시글에 대해 회원이 등록해둔 북마크 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content),
		@ApiResponse(responseCode = "204", description = "컨텐츠가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	public ResponseEntity<ApiCustomResponse<List<BookmarkResponseDto.BookmarkMyListResponse>>> getBookmarks(
		Authentication authentication,
		@PathVariable Long id
	) {
		return ResponseEntityFactory.toResponseEntity(
			BOOKMARK_VIEW_SUCCESS, bookmarkService.getBookmarks(authentication.getName(), id)
		);
	}

	@PostMapping("/update")
	@Operation(summary = "북마크 수정", description = "회원이 북마크를 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content),
		@ApiResponse(responseCode = "204", description = "컨텐츠가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	public ResponseEntity<ApiCustomResponse<BookmarkResponseDto.BookmarkUpdateResponse>> updateBookmark(
		Authentication authentication, @RequestBody BookmarkRequestDto.BookmarkUpdateRequest bookmarkRequestDto
	) {

		return ResponseEntityFactory.toResponseEntity(
			BOOKMARK_UPDATE_SUCCESS, bookmarkService.updateBookmark(authentication.getName(), bookmarkRequestDto)
		);

	}

}
