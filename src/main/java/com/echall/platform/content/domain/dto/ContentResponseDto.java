package com.echall.platform.content.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;

import com.echall.platform.content.domain.enums.ContentType;

public class ContentResponseDto {

	public record ContentViewResponseDto(
		ObjectId scriptId,
		String title,
		List<String> scripts,
		ContentType contentType,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {
		public void setPreScripts(List<String> preScripts) {
			preScripts.stream()
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.forEach(s -> this.scripts.add(s.trim()));

		}
	}

	public record ContentCreateResponseDto(
		ObjectId scriptId,
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
