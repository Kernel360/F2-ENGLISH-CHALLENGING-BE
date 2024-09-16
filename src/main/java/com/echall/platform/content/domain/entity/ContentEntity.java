package com.echall.platform.content.domain.entity;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import com.echall.platform.content.domain.enums.ContentStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "contents")
@Getter
@NoArgsConstructor
public class ContentEntity extends MongoBaseEntity {

	@NotNull
	private String url;

	@NotNull
	private String title;

	@NotNull
	private String script;

	@NotNull
	private String channelName;

	private ContentStatus contentStatus = ContentStatus.ACTIVATED;

	@Builder(toBuilder = true)
	@PersistenceConstructor
	public ContentEntity(ObjectId id, String url, String title, String script, String channelName, ContentStatus contentStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super(id, createdAt, updatedAt);
		this.url = url;
		this.title = title;
		this.script = script;
		this.channelName = channelName;
		this.contentStatus = contentStatus != null ? contentStatus : ContentStatus.ACTIVATED;
	}

}