package com.echall.platform.question.domain.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import com.echall.platform.question.domain.enums.QuestionType;
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
	private QuestionType type;

	@Builder
	public QuestionDocument(String question, String answer, QuestionType type) {
		this.question = question;
		this.answer = answer;
		this.type = type;
	}

	public static QuestionDocument of(String question, String answer, QuestionType type) {
		return QuestionDocument.builder()
			.question(question)
			.answer(answer)
			.type(type)
			.build();
	}
}
