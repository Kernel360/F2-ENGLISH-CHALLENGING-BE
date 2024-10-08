package com.echall.platform.content.repository.custom;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.echall.platform.content.domain.dto.ContentResponseDto;
import com.echall.platform.content.domain.enums.ContentType;
import com.echall.platform.content.domain.enums.SearchCondition;

public interface ContentRepositoryCustom {
	Page<ContentResponseDto.ContentViewResponseDto> search(
		Pageable pageable, SearchCondition searchCondition
	);

	List<ContentResponseDto.ContentPreviewResponseDto> getPreviewContents(
		ContentType contentType, String sortBy, int num
	);

	int updateHit(Long contentId);

}
