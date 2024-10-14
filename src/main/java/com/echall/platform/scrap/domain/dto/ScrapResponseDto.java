package com.echall.platform.scrap.domain.dto;

import java.time.LocalDateTime;

import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.scrap.domain.entity.ScrapEntity;

public class ScrapResponseDto {

	public record ScrapViewResponseDto(
		Long scrapId,
		Long contentId,
		String title,
		LocalDateTime createdAt,
		String preScript,
		String thumbnailUrl
	) {
		public static ScrapViewResponseDto from(ScrapEntity scrap) {
			return new ScrapViewResponseDto(
				scrap.getId(),
				scrap.getContent().getId(),
				scrap.getContent().getTitle(),
				scrap.getCreatedAt(),
				scrap.getContent().getPreScripts(),
				scrap.getContent().getThumbnailUrl()
			);
		}
	}

	public record ScrapCreateResponseDto(
		Long contentId
	) {
		public static ScrapCreateResponseDto from(ContentEntity content) {
			return new ScrapCreateResponseDto(
				content.getId()
			);
		}
	}

}
