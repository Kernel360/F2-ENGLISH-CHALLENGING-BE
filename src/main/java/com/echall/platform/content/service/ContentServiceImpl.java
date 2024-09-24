package com.echall.platform.content.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.echall.platform.content.domain.dto.ContentRequestDto;
import com.echall.platform.content.domain.dto.ContentResponseDto;
import com.echall.platform.content.domain.entity.ContentDocument;
import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.content.domain.enums.ContentStatus;
import com.echall.platform.content.repository.ContentRepository;
import com.echall.platform.content.repository.ContentScriptRepository;
import com.echall.platform.content.repository.custom.SearchCondition;

import lombok.RequiredArgsConstructor;

//TODO : custom exception 클래스 작성후 적용 필요합니다
@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

	private final ContentRepository contentRepository;
	private final ContentScriptRepository contentScriptRepository;

	@Override
	public Page<ContentResponseDto.ContentViewResponseDto> getAllContents(
		Pageable pageable, SearchCondition searchCondition
	) {
		return contentRepository.search(pageable, searchCondition);
	}

	@Override
	public ContentResponseDto.ContentCreateResponseDto createNewContent(
		ContentRequestDto.ContentCreateRequestDto contentCreateRequestDto
	) {

		ContentDocument contentDocument = contentCreateRequestDto.toDocument();
		contentScriptRepository.save(contentDocument);

		ContentEntity content = contentCreateRequestDto.toEntity(contentDocument.getId());
		contentRepository.save(content);

		return new ContentResponseDto.ContentCreateResponseDto(contentDocument.getId(), content.getId());
	}

	@Override
	public ContentResponseDto.ContentUpdateResponseDto updateContent(
		Long id, ContentRequestDto.ContentUpdateRequestDto contentUpdateRequest
	) {
		ContentEntity content = contentRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Content not found"));
		content.update(contentUpdateRequest);
		contentRepository.save(content);

		ContentDocument contentDocument = contentScriptRepository.findContentDocumentById(content.getMongoContentId());
		contentDocument.updateScript(contentUpdateRequest.script());
		contentScriptRepository.save(contentDocument);

		return new ContentResponseDto.ContentUpdateResponseDto(content.getId());
	}

	@Override
	public ContentResponseDto.ContentUpdateResponseDto deactivateContent(Long id) {
		ContentEntity content = contentRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Content not found"));
		content.updateStatus(ContentStatus.DEACTIVATED);
		contentRepository.save(content);

		return new ContentResponseDto.ContentUpdateResponseDto(content.getId());
	}

	@Override
	public ContentResponseDto.ContentDetailResponseDto getScriptsOfContent(Long id) {
		ContentDocument contentDocument = contentRepository.findById(id)
			.map(content -> contentScriptRepository.findContentDocumentById(content.getMongoContentId()))
			.orElseThrow(
				() -> new IllegalArgumentException("Content not found")
			);

		return new ContentResponseDto.ContentDetailResponseDto(
			id,
			contentDocument.getScriptList()
		);
	}

}
