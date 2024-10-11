package com.echall.platform.question.service;

import java.util.List;

import com.echall.platform.question.domain.dto.QuestionRequestDto;
import com.echall.platform.question.domain.dto.QuestionResponseDto;

public interface QuestionService {
	QuestionResponseDto.QuestionCreateResponseDto createQuestion(
		Long contentId, QuestionRequestDto.QuestionCreateRequestDto requestDto
	);

	List<QuestionResponseDto.QuestionViewResponseDto> getQuestions(Long contentId);
}
