package com.echall.platform.bookmark.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class BookmarkRequestDto {
	public record BookmarkUpdateRequest(
		@Schema(description = "컨텐츠 인덱스(ID)")
		Long scriptIndex,
		@Schema(description = "문장 인덱스")
		Long sentenceIndex,
		@Schema(description = "단어 인덱스")
		Long wordIndex
	) {
	}
}
