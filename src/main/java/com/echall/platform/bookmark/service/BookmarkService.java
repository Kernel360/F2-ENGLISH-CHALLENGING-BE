package com.echall.platform.bookmark.service;

import java.util.List;

import com.echall.platform.bookmark.domain.dto.BookmarkRequestDto;
import com.echall.platform.bookmark.domain.dto.BookmarkResponseDto;

public interface BookmarkService {
	List<BookmarkResponseDto.BookmarkMyListResponse> getBookmarks(String name, Long contentId);
	BookmarkResponseDto.BookmarkUpdateResponse updateBookmark(String name, BookmarkRequestDto.BookmarkUpdateRequest bookmarkRequestDto);
}
