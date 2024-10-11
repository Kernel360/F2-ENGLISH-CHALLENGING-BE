package com.echall.platform.content.domain.dto;

import java.util.List;

import org.bson.types.ObjectId;

import com.echall.platform.content.domain.entity.ContentDocument;
import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.content.domain.entity.Script;
import com.echall.platform.content.domain.enums.ContentStatus;
import com.echall.platform.content.domain.enums.ContentType;
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