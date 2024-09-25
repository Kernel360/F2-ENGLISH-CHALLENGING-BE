package com.echall.platform.bookmark.domain.dto;

public class BookmarkRequestDto {
	public record BookmarkUpdateRequest(
		Long scriptIndex,
		Long sentenceIndex,
		Long wordIndex
	) {
	}
}
