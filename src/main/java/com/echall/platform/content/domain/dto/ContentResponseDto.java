package com.echall.platform.content.domain.dto;

import com.echall.platform.content.domain.entity.ContentDocument;
import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.content.domain.entity.Script;
import com.echall.platform.content.domain.enums.ContentType;
import com.echall.platform.message.error.exception.CommonException;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

import static com.echall.platform.message.error.code.CategoryErrorCode.CATEGORY_NOT_FOUND;

public class ContentResponseDto {

	// ContentRepositoryImpl의 search 메서드에서 사용하는 Dto
	public record ContentViewResponseDto(
		Long contentId,
		String scriptId,
		String title,
		String enScripts,
		String koScripts,
		ContentType contentType,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {

	}

	public record ContentCreateResponseDto(
		String scriptId,
		Long contentId
	) {

	}

	public record ContentUpdateResponseDto(
		Long contentId
	) {

	}

	@Builder
	public record ScriptDto(
		@JsonInclude(JsonInclude.Include.NON_NULL)
		Double startTimeInSecond,
		@JsonInclude(JsonInclude.Include.NON_NULL)
		Double durationInSecond,
		String enScript,
		String koScript
	) {
		public static ScriptDto fromReadingScript(Script script) {
			return ScriptDto.builder()
				.enScript(script.getEnScript())
				.koScript(script.getKoScript())
				.build();
		}

		public static ScriptDto fromListeningScript(Script script) {
			return ScriptDto.builder()
				.startTimeInSecond(script.getStartTimeInSecond())
				.durationInSecond(script.getDurationInSecond())
				.enScript(script.getEnScript())
				.koScript(script.getKoScript())
				.build();
		}
	}

	@Builder
	public record ContentDetailResponseDto(
		Long contentId,
		ContentType contentType,
		String category,
		String title,
		String thumbnailUrl,
		@JsonInclude(JsonInclude.Include.NON_NULL)
		String listeningUrl,
		List<ScriptDto> scriptList
	) {
		public static ContentDetailResponseDto of(ContentEntity content, ContentDocument contentDocument) {
			return ContentDetailResponseDto.builder()
				.contentId(content.getId())
				.contentType(content.getContentType())
				.category(content.getCategory().getName())
				.title(content.getTitle())
				.thumbnailUrl(content.getThumbnailUrl())
				.listeningUrl(toListeningUrl(content))
				.scriptList(toScriptDto(content, contentDocument))
				.build();
		}

		private static String toListeningUrl(ContentEntity content) {
			switch (content.getContentType()) {
				case LISTENING -> {
					return content.getUrl();
				}
				case READING -> {
					return null;
				}
				default -> throw new CommonException(CATEGORY_NOT_FOUND);
			}
		}

		private static List<ScriptDto> toScriptDto(ContentEntity content, ContentDocument contentDocument) {
			switch (content.getContentType()) {
				case LISTENING -> {
					return contentDocument.getScripts().stream()
						.map(ScriptDto::fromListeningScript)
						.toList();
				}
				case READING -> {
					return contentDocument.getScripts().stream()
						.map(ScriptDto::fromReadingScript)
						.toList();
				}
				default -> throw new CommonException(CATEGORY_NOT_FOUND);
			}
		}
	}

	public record ContentPreviewResponseDto(
		Long contentId,
		String title,
		String thumbnailUrl,
		ContentType contentType,
		String preScripts,
		String category,
		int hits
	) {
		public static ContentPreviewResponseDto from(ContentEntity content) {
			return new ContentPreviewResponseDto(
				content.getId(),
				content.getTitle(),
				content.getThumbnailUrl(),
				content.getContentType(),
				content.getPreScripts(),
				content.getCategory().getName(),
				content.getHits()
			);
		}
	}
}
