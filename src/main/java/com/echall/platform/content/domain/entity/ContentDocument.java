package com.echall.platform.content.domain.entity;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "contents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentDocument extends MongoBaseEntity {

	@NotNull
	private List<String> scriptList;


	@Builder
	public ContentDocument(List<String> scriptList) {
		this.scriptList = scriptList;
	}

	public void updateScript(List<String> scriptList) {
		this.scriptList = scriptList;
	}
}