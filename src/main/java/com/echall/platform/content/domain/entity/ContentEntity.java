package com.echall.platform.content.domain.entity;

import java.util.ArrayList;

import org.bson.types.ObjectId;

import com.echall.platform.content.domain.dto.ContentRequestDto;
import com.echall.platform.content.domain.dto.ContentResponseDto;
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

	@Enumerated(EnumType.STRING)
	private ContentType contentType;

	@Enumerated(EnumType.STRING)
	private ContentStatus contentStatus = ContentStatus.ACTIVATED;

	private ObjectId mongoContentId;

	@Builder
	public ContentEntity(
		String url, String title, String channelName, ContentType contentType, ObjectId mongoContentId
	) {
		this.url = url;
		this.title = title;
		this.channelName = channelName;
		this.contentType = contentType;
		this.mongoContentId = mongoContentId;
	}

	public ContentResponseDto.ContentViewResponseDto toPreviewDto() {
		return new ContentResponseDto.ContentViewResponseDto(
			this.getMongoContentId(),
			this.getTitle(),
			new ArrayList<>(),
			this.contentType,
			this.getCreatedAt(),
			this.getUpdatedAt()
		);
	}
	public void update(ContentRequestDto.ContentUpdateRequestDto dto){
		this.url = dto.url();
		this.title = dto.title();
		this.channelName = dto.channelName();
		this.contentStatus = dto.contentStatus();
	}

	public void updateStatus(ContentStatus contentStatus) {
		this.contentStatus = contentStatus;
	}
}
