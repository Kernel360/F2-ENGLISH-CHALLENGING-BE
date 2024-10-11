package com.echall.platform.bookmark.domain.dto;

import jakarta.validation.constraints.NotNull;

public class BookmarkRequestDto {
	public record BookmarkCreateRequest(
		Long sentenceIndex,
		Long wordIndex
	) {
	}

	public record BookmarkUpdateRequest(
		@NotNull
		Long bookmarkId,
		String description
	) {
	}

}
