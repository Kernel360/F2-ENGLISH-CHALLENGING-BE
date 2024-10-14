package com.echall.platform.scrap.domain.dto;

import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.scrap.domain.entity.ScrapEntity;

public class ScrapRequestDto {

	public record ScrapCreateRequestDto(
		Long contentId
	) {
		public ScrapEntity toEntity(ContentEntity content) {
			return ScrapEntity.builder()
				.content(content)
				.build();
		}
	}

	public record ScrapDeleteRequestDto(
		Long contentId
	) {

	}
}
