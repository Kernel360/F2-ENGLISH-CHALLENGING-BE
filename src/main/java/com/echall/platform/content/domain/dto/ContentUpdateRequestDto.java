package com.echall.platform.content.domain.dto;

import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.content.domain.enums.ContentStatus;

public record ContentUpdateRequestDto(
	String url,
	String title,
	String script,
	String channelName,
	ContentStatus contentStatus
) {

	public ContentEntity toEntity() {
		return ContentEntity.builder()
			.url(this.url)
			.title(this.title)
			.script(this.script)
			.channelName(this.channelName)
			.contentStatus(this.contentStatus)
			.build();
	}
}
