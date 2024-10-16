package com.echall.platform.content.repository.custom;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.echall.platform.content.domain.dto.ContentRequestDto;
import com.echall.platform.content.domain.dto.ContentResponseDto;
import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.content.domain.enums.ContentType;

public interface ContentRepositoryCustom {

	Page<ContentEntity> findAllBySearchCondition(
		ContentRequestDto.ContentSearchDto searchDto, Pageable pageable
	);

	List<ContentResponseDto.ContentPreviewResponseDto> findPreviewContents(
		ContentType contentType, String sortBy, int num
	);

	int updateHit(Long contentId);

	Page<ContentEntity> findAllByContentTypeAndCategory(ContentType contentType, Pageable pageable, Long categoryId);

	String findTitleById(Long contentId);

	String findMongoIdByContentId(Long contentId);

	List<ContentResponseDto.ContentByScrapCountDto> contentByScrapCount(int num);

	ContentType findContentTypeById(Long scriptIndex);

	boolean existsByUrl(String url);
}
