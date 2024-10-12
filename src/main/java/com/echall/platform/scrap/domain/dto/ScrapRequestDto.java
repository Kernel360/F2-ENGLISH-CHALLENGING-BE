package com.echall.platform.scrap.domain.dto;

public class ScrapRequestDto {

	public record ScrapCreateRequestDto(
		Long contentId
	) {

	}

	public record ScrapDeleteRequestDto(
		Long scrapId
	) {

	}
}
