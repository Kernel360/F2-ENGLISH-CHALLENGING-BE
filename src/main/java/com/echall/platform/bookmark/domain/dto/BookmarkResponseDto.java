package com.echall.platform.bookmark.domain.dto;

import com.echall.platform.bookmark.domain.entity.BookmarkEntity;

public class BookmarkResponseDto {
	public record BookmarkMyListResponse(
		Long bookmarkId,
		Long sentenceIndex,
		Long wordIndex,
		String description
	) {
		public static BookmarkMyListResponse of(BookmarkEntity bookmark) {
			return new BookmarkMyListResponse(
				bookmark.getId(),
				bookmark.getSentenceIndex(),
				bookmark.getWordIndex(),
				bookmark.getDescription()
			);
		}
	}

	public record BookmarkCreateResponse(
		Long bookmarkId,
		Long scriptIndex,
		Long sentenceIndex,
		Long wordIndex
	) {
		public static BookmarkCreateResponse of(BookmarkEntity bookmark) {
			return new BookmarkCreateResponse(
				bookmark.getId(),
				bookmark.getScriptIndex(),
				bookmark.getSentenceIndex(),
				bookmark.getWordIndex()
			);
		}
	}
	public record BookmarkDeleteResponse(
		Long bookmarkId
	){
	}

}
