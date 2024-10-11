package com.echall.platform.question.domain.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import com.echall.platform.util.MongoBaseDocument;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "question")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionDocument extends MongoBaseDocument {

	private String question;
	private String answer;

	@Builder
	public QuestionDocument(String question, String answer) {
		this.question = question;
		this.answer = answer;
	}

	public static QuestionDocument of(String question, String answer) {
		return QuestionDocument.builder()
			.question(question)
			.answer(answer)
			.build();
	}
}
