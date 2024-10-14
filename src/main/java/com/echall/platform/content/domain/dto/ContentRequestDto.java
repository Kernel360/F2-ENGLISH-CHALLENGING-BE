package com.echall.platform.content.domain.dto;

import com.echall.platform.content.domain.enums.ContentStatus;
import com.echall.platform.content.domain.enums.ContentType;
import com.echall.platform.script.domain.entity.Script;

import java.util.List;
public class ContentRequestDto {

	public record ContentCreateRequestDto(
		ContentType contentType,
		String url
	) {
	}

	public record ContentUpdateRequestDto(
		String url,
		String title,
		List<Script> script,
		ContentStatus contentStatus
	) {

	}
}