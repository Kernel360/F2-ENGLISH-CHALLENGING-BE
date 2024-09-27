package com.echall.platform.crawling.domain.dto;

import java.util.List;

public class CrawlingResponseDto {

	public record YoutubeResponseDto(
		String url,
		String channelName,
		String title,
		String imgUrl,
		String category,
		List<String> script

	) {

	}

	public record CNNResponseDto(
		String url,
		String title,
		String imgUrl,
		String category,
		List<String> script
	) {
	}
}
