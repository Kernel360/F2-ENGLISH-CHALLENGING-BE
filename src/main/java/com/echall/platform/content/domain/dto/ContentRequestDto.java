package com.echall.platform.content.domain.dto;

import com.echall.platform.content.domain.entity.ContentEntity;

public record ContentRequestDto(
	String url,
	String title,
	String script,
	String channelName
) {
	public ContentEntity toEntity() {
		return ContentEntity.builder()
			.url(this.url)
			.title(this.title)
			.script(this.script)
			.channelName(this.channelName)
			.build();
	}
}
