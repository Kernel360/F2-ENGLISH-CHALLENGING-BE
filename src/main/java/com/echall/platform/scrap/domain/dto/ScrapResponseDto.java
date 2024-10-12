package com.echall.platform.scrap.domain.dto;

import com.echall.platform.scrap.domain.entity.ScrapEntity;

public class ScrapResponseDto {

	public record ScrapViewResponseDto(
		Long scrapId,
		Long contentId
	) {
		public static ScrapViewResponseDto of(ScrapEntity scrap) {
			return new ScrapViewResponseDto(
				scrap.getId(),
				scrap.getContentId()
			);
		}
	}

	public record ScrapCreateResponseDto(
		Long contentId
	) {
		public static ScrapCreateResponseDto of(Long contentId) {
			return new ScrapCreateResponseDto(
				contentId
			);
		}
	}

	public record ScrapDeleteResponseDto(
		Long scrapId
	) {
	}
}
