package com.echall.platform.bookmark.repository.custom;

import java.util.List;

import com.echall.platform.bookmark.domain.entity.BookmarkEntity;

public interface BookmarkRepositoryCustom {
	BookmarkEntity findBookmark(Long userId, Long bookmarkId);
}
