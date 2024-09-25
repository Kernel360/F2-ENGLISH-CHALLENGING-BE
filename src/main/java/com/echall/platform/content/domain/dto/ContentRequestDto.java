package com.echall.platform.content.domain.dto;

import java.util.List;

import org.bson.types.ObjectId;

import com.echall.platform.content.domain.entity.ContentDocument;
import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.content.domain.enums.ContentStatus;
import com.echall.platform.content.domain.enums.ContentType;
public class ContentRequestDto {

	public record ContentCreateRequestDto(
		ContentType contentType,
		String title,
		String url,
		String channelName,
		List<String> scriptsList
	) {
		public ContentEntity toEntity(ObjectId contentScriptId){
			return ContentEntity.builder()
				.contentType(contentType)
				.title(title)
				.url(url)
				.channelName(channelName)
				.mongoContentId(contentScriptId.toString())
				.preScripts(scriptsList)
				.build();
		}

		public ContentDocument toDocument(){
			return ContentDocument.builder()
				.scriptList(this.scriptsList)
				.build();
		}
	}

	public record ContentUpdateRequestDto(
		String url,
		String title,
		List<String> script,
		String channelName,
		ContentStatus contentStatus
	) {

	}
}