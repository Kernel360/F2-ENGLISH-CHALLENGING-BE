package com.echall.platform.bookmark.repository.custom;

import com.echall.platform.bookmark.domain.dto.BookmarkRequestDto;
import com.echall.platform.bookmark.domain.entity.BookmarkEntity;

import java.util.List;

public interface BookmarkRepositoryCustom {
	Long deleteBookmark(Long userId, Long bookmarkId);
	List<BookmarkEntity> getAllBookmarks(Long userId);
	boolean isBookmarkAlreadyPresent(Long scriptIndex, BookmarkRequestDto.BookmarkCreateRequest request, Long userId);
}
