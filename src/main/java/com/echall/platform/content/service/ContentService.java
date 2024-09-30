package com.echall.platform.content.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import com.echall.platform.content.domain.dto.ContentRequestDto;
import com.echall.platform.content.domain.dto.ContentResponseDto;
import com.echall.platform.content.domain.enums.SearchCondition;

public interface ContentService {
	Page<ContentResponseDto.ContentViewResponseDto> getAllContents(Pageable pageable, SearchCondition searchCondition);

	ContentResponseDto.ContentDetailResponseDto getScriptsOfContent(Long id);

	ContentResponseDto.ContentCreateResponseDto createContent(
		Authentication authentication, ContentRequestDto.ContentCreateRequestDto contentCreateRequestDto)
		throws Exception; //새로운 컨텐츠 생성

	ContentResponseDto.ContentUpdateResponseDto updateContent(Long id,
		ContentRequestDto.ContentUpdateRequestDto contentUpdateRequest);

	ContentResponseDto.ContentUpdateResponseDto deactivateContent(Long id);

}
