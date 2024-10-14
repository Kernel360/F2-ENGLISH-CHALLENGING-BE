package com.echall.platform.bookmark.service;

import java.util.List;

import com.echall.platform.bookmark.domain.dto.BookmarkRequestDto;
import com.echall.platform.bookmark.domain.dto.BookmarkResponseDto;

public interface BookmarkService {
	List<BookmarkResponseDto.BookmarkListResponseDto> getBookmarks(String email, Long contentId);

	List<BookmarkResponseDto.BookmarkMyListResponseDto> getAllBookmarks(Long userId);

	BookmarkResponseDto.BookmarkListResponseDto updateBookmark(
		BookmarkRequestDto.BookmarkUpdateRequest bookmarkRequestDto, Long contentId
	);

	BookmarkResponseDto.BookmarkCreateResponse createBookmark(
		String email, BookmarkRequestDto.BookmarkCreateRequest bookmarkRequestDto, Long contentId
	);

	void deleteBookmark(Long userId, Long bookmarkId);
}
