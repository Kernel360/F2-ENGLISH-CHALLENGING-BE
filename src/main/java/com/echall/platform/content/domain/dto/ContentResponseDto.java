package com.echall.platform.content.domain.dto;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;

import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.content.domain.enums.ContentStatus;

public record ContentResponseDto(
	ObjectId id,
	String url,
	String title,
	String script,
	String channelName,
	ContentStatus contentStatus,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {

	public ContentResponseDto(ContentEntity content) {
		this(
			content.getId(),
			content.getUrl(),
			content.getTitle(),
			content.getScript(),
			content.getChannelName(),
			content.getContentStatus(),
			content.getCreatedAt(),
			content.getUpdatedAt()
		);
	}

	public static ContentResponseDto of(ContentEntity content) {
		return new ContentResponseDto(content);
	}
}