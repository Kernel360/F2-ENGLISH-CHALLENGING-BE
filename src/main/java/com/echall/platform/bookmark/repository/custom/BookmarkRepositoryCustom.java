package com.echall.platform.bookmark.repository.custom;

import java.util.List;

import com.echall.platform.bookmark.domain.entity.BookmarkEntity;

public interface BookmarkRepositoryCustom {
	Long deleteBookmark(Long userId, Long bookmarkId);
	List<BookmarkEntity> getAllBookmarks(Long userId);
}
