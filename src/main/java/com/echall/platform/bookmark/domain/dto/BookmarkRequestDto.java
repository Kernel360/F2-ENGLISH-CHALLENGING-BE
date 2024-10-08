package com.echall.platform.bookmark.domain.dto;

import jakarta.validation.constraints.NotBlank;

public class BookmarkRequestDto {
	public record BookmarkCreateRequest(
		Long sentenceIndex,
		Long wordIndex
	) {
	}

	public record BookmarkUpdateRequest(
		Long bookmarkId,
		@NotBlank
		String description
	) {
	}
}
