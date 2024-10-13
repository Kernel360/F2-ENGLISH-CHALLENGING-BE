package com.echall.platform.crawling.domain.dto;

import java.util.List;

import com.echall.platform.category.domain.entity.CategoryEntity;
import org.bson.types.ObjectId;

import com.echall.platform.content.domain.entity.ContentDocument;
import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.content.domain.entity.Script;
import com.echall.platform.content.domain.enums.ContentType;

public class CrawlingResponseDto {

	public record CrawlingContentResponseDto(
		String url,
		String title,
		String imgUrl,
		String category,
		List<Script> script

	) {
		public ContentDocument toDocument(){
			return ContentDocument.builder()
				.scriptList(script)
				.build();
		}
		public ContentEntity toEntity(ObjectId contentScriptId, ContentType contentType, CategoryEntity category){
			return ContentEntity.builder()
				.contentType(contentType)
				.url(url)
				.title(title)
				.thumbnailUrl(imgUrl)
				.mongoContentId(contentScriptId.toString())
				.preScripts(script)
				.category(category)
				.build();

		}
		public static CrawlingContentResponseDto of(
			String url, String title, String imgUrl, String category, List<Script> script
		) {
			return new CrawlingContentResponseDto(url, title, imgUrl, category, script);
		}
		public CategoryEntity toCategoryEntity() {
			return CategoryEntity.builder()
				.name(this.category)
				.build();
		}
	}
}
