package com.echall.platform.content.service;

import com.echall.platform.content.domain.dto.ContentRequestDto;
import com.echall.platform.content.domain.dto.ContentResponseDto;
import com.echall.platform.content.domain.enums.ContentType;
import com.echall.platform.util.PaginationDto;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

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

	List<ContentResponseDto.ContentPreviewResponseDto> getPreviewContents(
		ContentType contentType, String sortBy, int num
	);

}
