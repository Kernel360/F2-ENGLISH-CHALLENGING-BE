package com.echall.platform.content.domain.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "content")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentDocument extends MongoBaseEntity {

	private List<Script> scripts;

	@Builder
	public ContentDocument(List<Script> scriptList) {
		this.scripts = scriptList;
	}

	public void updateScript(List<Script> scriptList) {
		this.scripts = scriptList;
	}
}