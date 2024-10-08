package com.echall.platform.bookmark.domain.dto;

public class BookmarkRequestDto {
	public record BookmarkCreateRequest(
		Long sentenceIndex,
		Long wordIndex
	) {
	}

	public record BookmarkUpdateRequest(
		Long bookmarkId,
		String description
	) {
	}
}
