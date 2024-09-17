package com.echall.platform.content.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.echall.platform.content.domain.dto.ContentRequestDto;
import com.echall.platform.content.domain.dto.ContentResponseDto;
import com.echall.platform.content.domain.dto.ContentUpdateRequestDto;
import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.content.domain.enums.ContentStatus;
import com.echall.platform.content.repository.ContentRepository;

import lombok.RequiredArgsConstructor;

//TODO : custom exception 클래스 작성후 적용 필요합니다
@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService{

	private final ContentRepository contentRepository;

	@Override
	public Page<ContentResponseDto> get(Pageable pageable) {
		Page<ContentEntity> pageContentList = contentRepository.findAllByContentStatus(ContentStatus.ACTIVATED,
			pageable);
		return pageContentList.map(ContentResponseDto::of);
	}


	@Override
	public ContentResponseDto createContent(ContentRequestDto contentRequestDto
	) {
		ContentEntity newContent = contentRequestDto.toEntity();
		ContentEntity savedContent = contentRepository.save(newContent);
		return ContentResponseDto.of(savedContent);
	}

	@Override
	public List<ContentEntity> getAllContent() {
		return contentRepository.findAll();
	}

	@Override
	public ContentEntity getContentById(ObjectId id) {
		return contentRepository.findById(id).orElse(null);
	}

	@Override
	public ContentResponseDto updateContent(String id, ContentUpdateRequestDto contentUpdateRequestDto) {
		ObjectId objectId;
		try {
			objectId = new ObjectId(id);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("유효하지 않은 ID 형식입니다.");
		}
		Optional<ContentEntity> optionalContent = contentRepository.findById(objectId);

		if (optionalContent.isPresent()) {
			ContentEntity existingContent = optionalContent.get();

			return ContentResponseDto.of(contentRepository.save(
				existingContent.toBuilder()
					.url(contentUpdateRequestDto.url())
					.title(contentUpdateRequestDto.title())
					.script(contentUpdateRequestDto.script())
					.channelName(contentUpdateRequestDto.channelName())
					.contentStatus(contentUpdateRequestDto.contentStatus())
					.updatedAt(LocalDateTime.now())
					.build()
			));
		} else {
			throw new IllegalArgumentException("해당 ID의 컨텐츠를 찾을 수 없습니다.");
		}
	}

	@Override
	public ContentResponseDto deactivateContent(String id) {
		ObjectId objectId;
		try {
			objectId = new ObjectId(id);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("유효하지 않은 ID 형식입니다.");
		}

		Optional<ContentEntity> content = contentRepository.findById(objectId);

		if (content.isPresent()) {
			ContentEntity existingContent = content.get();

			return ContentResponseDto.of(contentRepository.save(
					existingContent.toBuilder()
						.contentStatus(ContentStatus.DEACTIVATED)
						.updatedAt(LocalDateTime.now())
						.build()
			));
		} else {
			throw new IllegalArgumentException("해당 ID의 컨텐츠를 찾을 수 없습니다.");
		}
	}
}
