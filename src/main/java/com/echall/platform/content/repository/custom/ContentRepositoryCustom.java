package com.echall.platform.content.repository.custom;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.echall.platform.content.domain.dto.ContentResponseDto;
import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.content.domain.enums.ContentType;
import com.querydsl.core.Tuple;

public interface ContentRepositoryCustom {

	List<ContentResponseDto.ContentPreviewResponseDto> findPreviewContents(
		ContentType contentType, String sortBy, int num
	);

	int updateHit(Long contentId);

	Page<ContentEntity> findAllByContentTypeAndCategory(ContentType contentType, Pageable pageable, Long categoryId);

	String findTitleById(Long contentId);

	String findMongoIdByContentId(Long contentId);

	List<Tuple> contentByScrapCount(int num);
}
