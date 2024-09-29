package com.echall.platform.content.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.echall.platform.content.domain.enums.ContentType;

public class ContentResponseDto {

	public record ContentViewResponseDto(
		String scriptId,
		String title,
		String scripts,
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
		List<String> scriptList
	) {

	}
}
