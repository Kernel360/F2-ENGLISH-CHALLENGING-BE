package com.echall.platform.bookmark.repository.custom;

import static com.echall.platform.bookmark.domain.entity.QBookmarkEntity.*;
import static com.echall.platform.message.error.code.BookmarkErrorCode.*;
import static com.echall.platform.user.domain.entity.QUserEntity.*;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.echall.platform.bookmark.domain.entity.BookmarkEntity;
import com.echall.platform.message.error.exception.CommonException;

public class BookmarkRepositoryImpl extends QuerydslRepositorySupport implements BookmarkRepositoryCustom {

	public BookmarkRepositoryImpl() {
		super(BookmarkEntity.class);
	}

	@Override
	public Long deleteBookmark(Long userId, Long bookmarkId) {
		BookmarkEntity bookmark = from(userEntity)
			.join(userEntity.bookmarks, bookmarkEntity)
			.select(bookmarkEntity)
			.where(userEntity.id.eq(userId))
			.where(bookmarkEntity.id.eq(bookmarkId))
			.fetchOne();

		if (bookmark == null) {
			throw new CommonException(BOOKMARK_NOT_FOUND);
		}

		delete(bookmarkEntity)
			.where(bookmarkEntity.id.eq(bookmarkId))
			.execute();

		return bookmarkId;
	}

}
