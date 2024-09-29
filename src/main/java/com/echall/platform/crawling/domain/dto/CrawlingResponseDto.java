package com.echall.platform.crawling.domain.dto;

import java.util.List;

import org.bson.types.ObjectId;

import com.echall.platform.content.domain.entity.ContentDocument;
import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.content.domain.enums.ContentType;

public class CrawlingResponseDto {

	public record CrawlingContentResponseDto(
		String url,
		String title,
		String imgUrl,
		String category,
		List<String> script

	) {
		public ContentDocument toDocument(){
			return ContentDocument.builder()
				.scriptList(script)
				.build();
		}
		public ContentEntity toEntity(ObjectId contentScriptId, ContentType contentType){
			return ContentEntity.builder()
				.contentType(contentType)
				.title(title)
				.url(url)
				.mongoContentId(contentScriptId.toString())
				.preScripts(script)
				.build();

		}
	}

}
