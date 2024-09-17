package com.echall.platform.content.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.echall.platform.content.domain.dto.ContentRequestDto;
import com.echall.platform.content.domain.dto.ContentResponseDto;
import com.echall.platform.content.domain.dto.ContentUpdateRequestDto;
import com.echall.platform.content.domain.entity.ContentEntity;


public interface ContentService {
	Page<ContentResponseDto> get(Pageable pageable);
	ContentResponseDto createContent(ContentRequestDto contentRequestDto); //새로운 컨텐츠 생성
	List<ContentEntity> getAllContent(); //모든 컨텐츠 가져오기
	ContentEntity getContentById(ObjectId id); //아이디로 컨텐츠 가져오기 -> TODO : 추후 수정 필요
	ContentResponseDto updateContent(String id, ContentUpdateRequestDto updatedContent);
	ContentResponseDto deactivateContent(String id);
}
