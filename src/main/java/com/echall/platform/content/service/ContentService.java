package com.echall.platform.content.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import com.echall.platform.content.domain.dto.ContentRequestDto;
import com.echall.platform.content.domain.dto.ContentResponseDto;
import com.echall.platform.content.domain.enums.ContentType;
import com.echall.platform.util.PaginationDto;

public interface ContentService {
	PaginationDto<ContentResponseDto.ContentPreviewResponseDto> getAllContents(
		ContentType contentType, Pageable pageable, Long categoryId
	);

	ContentResponseDto.ContentDetailResponseDto getScriptsOfContent(Long id);

	ContentResponseDto.ContentCreateResponseDto createContent(
		Authentication authentication, ContentRequestDto.ContentCreateRequestDto contentCreateRequestDto
	) throws Exception;

	ContentResponseDto.ContentUpdateResponseDto updateContent(
		Long id,
		ContentRequestDto.ContentUpdateRequestDto contentUpdateRequest
	);

	ContentResponseDto.ContentUpdateResponseDto deactivateContent(Long id);

	List<ContentResponseDto.ContentPreviewResponseDto> findPreviewContents(
		ContentType contentType, String sortBy, int num
	);

	List<ContentResponseDto.ContentCountByScrapResponseDto> contentByScrapCount(int num);
}
