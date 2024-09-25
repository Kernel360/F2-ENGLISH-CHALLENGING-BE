package com.echall.platform.content.domain.entity;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.echall.platform.content.domain.dto.ContentRequestDto;
import com.echall.platform.content.domain.dto.ContentResponseDto;
import com.echall.platform.content.domain.enums.ContentStatus;
import com.echall.platform.content.domain.enums.ContentType;
import com.echall.platform.user.domain.entity.BaseEntity;
import com.echall.platform.util.StringListConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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

	@NotNull
	private String channelName;

	@Convert(converter = StringListConverter.class)
	private List<String> preScripts;

	@Enumerated(EnumType.STRING)
	private ContentType contentType;

	@Enumerated(EnumType.STRING)
	private ContentStatus contentStatus = ContentStatus.ACTIVATED;

	private String mongoContentId;

	@Builder
	public ContentEntity(
		String url, String title, String channelName,
		ContentType contentType, String mongoContentId,
		List<String> preScripts
	) {
		this.url = url;
		this.title = title;
		this.channelName = channelName;
		this.contentType = contentType;
		this.mongoContentId = mongoContentId;
		this.preScripts
			= preScripts.stream().limit(5).toList();
	}


	public void update(ContentRequestDto.ContentUpdateRequestDto dto) {
		this.url = dto.url();
		this.title = dto.title();
		this.channelName = dto.channelName();
		this.contentStatus = dto.contentStatus() == null ? ContentStatus.ACTIVATED : dto.contentStatus();
		this.preScripts = dto.script().stream().limit(5).toList();
	}

	public void updateStatus(ContentStatus contentStatus) {
		this.contentStatus = contentStatus == null ? ContentStatus.ACTIVATED : contentStatus;
	}
}
