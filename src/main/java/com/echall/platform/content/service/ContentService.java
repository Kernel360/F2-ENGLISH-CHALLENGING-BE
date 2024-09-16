package com.echall.platform.content.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.echall.platform.content.domain.entity.ContentEntity;

public interface ContentService {
	Page<ContentEntity> get(Pageable pageable);
	ContentEntity createContent(ContentEntity newContent); //새로운 컨텐츠 생성
	List<ContentEntity> getAllContent(); //모든 컨텐츠 가져오기
	ContentEntity getContentById(ObjectId id); //아이디로 컨텐츠 가져오기 -> TODO : 추후 수정 필요
	ContentEntity updateContent(ContentEntity updatedContent);
	void deactivateContent(String id);
}
