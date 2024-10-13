package com.echall.platform.scrap.domain.dto;

import com.echall.platform.scrap.domain.entity.ScrapEntity;

public class ScrapRequestDto {

	public record ScrapCreateRequestDto(
		Long contentId
	) {
		public ScrapEntity toEntity() {
			return ScrapEntity.builder()
				.contentId(contentId)
				.build();
		}
	}

	public record ScrapDeleteRequestDto(
		Long scrapId
	) {

	}
}
