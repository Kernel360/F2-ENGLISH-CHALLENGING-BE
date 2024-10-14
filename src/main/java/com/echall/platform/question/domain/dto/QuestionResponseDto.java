package com.echall.platform.question.domain.dto;

import java.util.List;

import com.echall.platform.question.domain.enums.QuestionType;

public class QuestionResponseDto {
	public record QuestionCreateResponseDto(
		List<String> questionDocumentIds
	) {
	}

	public record QuestionViewResponseDto (
		String question,
		String answer,
		QuestionType type
	){
	}
}
