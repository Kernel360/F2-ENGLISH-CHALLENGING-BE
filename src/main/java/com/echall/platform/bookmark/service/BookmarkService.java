package com.echall.platform.bookmark.service;

import java.util.List;

import com.echall.platform.bookmark.domain.dto.BookmarkRequestDto;
import com.echall.platform.bookmark.domain.dto.BookmarkResponseDto;

public interface BookmarkService {
	List<BookmarkResponseDto.BookmarkMyListResponse> getBookmarks(String email, Long contentId);

	List<BookmarkResponseDto.BookmarkMyListResponse> getAllBookmarks(Long userId);

	BookmarkResponseDto.BookmarkMyListResponse updateBookmark(
		BookmarkRequestDto.BookmarkUpdateRequest bookmarkRequestDto, Long contentId
	);

	BookmarkResponseDto.BookmarkCreateResponse createBookmark(
		String email, BookmarkRequestDto.BookmarkCreateRequest bookmarkRequestDto, Long contentId
	);

	BookmarkResponseDto.BookmarkDeleteResponse deleteBookmark(Long userId, Long bookmarkId);
}
