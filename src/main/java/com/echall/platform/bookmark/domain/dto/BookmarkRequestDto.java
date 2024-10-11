package com.echall.platform.bookmark.domain.dto;

import static com.echall.platform.message.error.code.BookmarkErrorCode.*;

import com.echall.platform.message.error.exception.CommonException;

import jakarta.validation.constraints.NotNull;

public class BookmarkRequestDto {
	public record BookmarkCreateRequest(
		@NotNull Long sentenceIndex,
		Long wordIndex,
		String description
	) {
		public void validate() {
			if (wordIndex == null) {
				throw new CommonException(BOOKMARK_NEED_ANY_INDEX);
			}
		}
	}

	public record BookmarkUpdateRequest(
		@NotNull Long bookmarkId,
		String description
	) {
	}

}
