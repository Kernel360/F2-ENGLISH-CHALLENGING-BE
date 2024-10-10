package com.echall.platform.bookmark.repository.custom;

import static com.echall.platform.bookmark.domain.entity.QBookmarkEntity.*;
import static com.echall.platform.message.error.code.BookmarkErrorCode.*;
import static com.echall.platform.user.domain.entity.QUserEntity.*;

import java.util.Objects;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import com.echall.platform.bookmark.domain.entity.BookmarkEntity;

public class BookmarkRepositoryImpl extends QuerydslRepositorySupport implements BookmarkRepositoryCustom {

	public BookmarkRepositoryImpl() {
		super(BookmarkEntity.class);
	}

	@Override
	@Transactional(readOnly = true)
	public BookmarkEntity findBookmark(Long userId, Long bookmarkId) {
		return Objects.requireNonNull(from(userEntity)
			.join(userEntity.bookmarks, bookmarkEntity)
			.select(bookmarkEntity)
			.where(userEntity.id.eq(userId))
			.where(bookmarkEntity.id.eq(bookmarkId))
			.fetchOne(), BOOKMARK_NOT_FOUND.getMessage());
	}
}
