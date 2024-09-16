package com.echall.platform.content.domain.dto;

import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.content.domain.enums.ContentType;

public record ContentRequestDto(
	String url,
	String title,
	String script,
	ContentType contentType,
	String channelName
) {
	public ContentEntity toEntity() {
		return ContentEntity.builder()
			.url(this.url)
			.title(this.title)
			.script(this.script)
			.contentType(this.contentType)
			.channelName(this.channelName)
			.build();
	}
}
