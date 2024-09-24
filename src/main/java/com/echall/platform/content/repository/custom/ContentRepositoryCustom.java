package com.echall.platform.content.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.echall.platform.content.domain.dto.ContentResponseDto;

public interface ContentRepositoryCustom {
	Page<ContentResponseDto.ContentViewResponseDto> search(
		Pageable pageable, SearchCondition searchCondition
	);

}
