package com.echall.platform.content.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.content.domain.entity.Script;
import com.echall.platform.content.domain.enums.ContentType;

public class ContentResponseDto {

	// ContentRepositoryImpl의 search 메서드에서 사용하는 Dto
	public record ContentViewResponseDto(
		Long contentId,
		String scriptId,
		String title,
		String enScripts,
		String koScripts,
		ContentType contentType,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {

	}

	public record ContentCreateResponseDto(
		String scriptId,
		Long contentId
	) {

	}

	public record ContentUpdateResponseDto(
		Long contentId
	) {

	}

	public record ContentDetailResponseDto(
		Long contentId,
		ContentType contentType,
		String category,
		String title,
		String thumbnailUrl,
		List<Script> scriptList
	) {

	}

	public record ContentPreviewResponseDto(
		Long contentId,
		String title,
		String thumbnailUrl,
		ContentType contentType,
		String preScripts,
		String category,
		int hits
	) {
		public static ContentPreviewResponseDto from(ContentEntity content) {
			return new ContentPreviewResponseDto(
				content.getId(),
				content.getTitle(),
				content.getThumbnailUrl(),
				content.getContentType(),
				content.getPreScripts(),
				content.getCategory().getName(),
				content.getHits()
			);
		}
	}
}
