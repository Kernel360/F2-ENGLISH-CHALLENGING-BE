package com.echall.platform.content.domain.entity;

import java.util.List;

import com.echall.platform.content.domain.dto.ContentRequestDto;
import com.echall.platform.content.domain.enums.ContentStatus;
import com.echall.platform.content.domain.enums.ContentType;
import com.echall.platform.user.domain.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "content")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@NotNull
	private String url;

	@NotNull
	private String title;

	private String category;

	@Column(columnDefinition = "integer default 0")
	private int hits;

	private String thumbnailUrl;

	@Size(max = 255)
	private String preScripts;

	@Enumerated(EnumType.STRING)
	private ContentType contentType;

	@Enumerated(EnumType.STRING)
	private ContentStatus contentStatus = ContentStatus.ACTIVATED;

	private String mongoContentId;

	@Builder
	public ContentEntity(
		String url, String title, String category, String thumbnailUrl,
		ContentType contentType, String mongoContentId,
		List<Script> preScripts
	) {
		this.url = url;
		this.title = title;
		this.category = category;
		this.thumbnailUrl = thumbnailUrl;
		this.contentType = contentType;
		this.mongoContentId = mongoContentId;
		this.preScripts = truncate(
			preScripts.subList(0, 5)
				.stream()
				.map(Script::getEnScript)
				.toList().toString()
			, 255
		);
	}

	@Transactional
	public void update(ContentRequestDto.ContentUpdateRequestDto dto) {
		this.url = dto.url();
		this.title = dto.title();
		this.contentStatus = dto.contentStatus() == null ? ContentStatus.ACTIVATED : dto.contentStatus();
		this.preScripts = truncate(
			dto.script().subList(0, 5)
				.stream()
				.map(Script::getEnScript)
				.toList().toString()
			, 255);
	}

	@Transactional
	public void updateStatus(ContentStatus contentStatus) {
		this.contentStatus = contentStatus == null ? ContentStatus.ACTIVATED : contentStatus;
	}

	private String truncate(String content, int maxLength) {
		return content.length() > maxLength ? content.substring(0, maxLength) : content;
	}
}
