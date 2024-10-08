package com.echall.platform.bookmark.service;

import java.util.List;

import com.echall.platform.bookmark.domain.dto.BookmarkRequestDto;
import com.echall.platform.bookmark.domain.dto.BookmarkResponseDto;

public interface BookmarkService {
	List<BookmarkResponseDto.BookmarkMyListResponse> getBookmarks(String email, Long contentId);
	BookmarkResponseDto.BookmarkMyListResponse updateBookmark(
		String email, BookmarkRequestDto.BookmarkUpdateRequest bookmarkRequestDto, Long contentId
	);
	BookmarkResponseDto.BookmarkCreateResponse createBookmark(
		String email, BookmarkRequestDto.BookmarkCreateRequest bookmarkRequestDto, Long contentId
	);
}
