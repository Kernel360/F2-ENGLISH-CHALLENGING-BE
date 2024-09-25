package com.echall.platform.bookmark.domain.dto;

import com.echall.platform.bookmark.domain.entity.BookmarkEntity;

public class BookmarkResponseDto {
	public record BookmarkMyListResponse(
		Long scriptIndex,
		Long sentenceIndex,
		Long wordIndex
	) {
		public static BookmarkMyListResponse of(BookmarkEntity bookmark){
			return new BookmarkMyListResponse(
				bookmark.getScriptIndex(),
				bookmark.getSentenceIndex(),
				bookmark.getWordIndex()
			);
		}
	}

	public record BookmarkUpdateResponse(
		Long contentId,
		Long bookmarkId
	) {
	}
}
