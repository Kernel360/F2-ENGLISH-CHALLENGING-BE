package com.echall.platform.content.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.content.domain.enums.ContentStatus;
import com.echall.platform.content.repository.ContentRepository;

//TODO : custom exception 클래스 작성후 적용 필요합니다
@Service
public class ContentServiceImpl implements ContentService{

	private final ContentRepository contentRepository;

	@Autowired
	public ContentServiceImpl(ContentRepository contentRepository) {
		this.contentRepository = contentRepository;
	}

	@Override
	public Page<ContentEntity> get(Pageable pageable) {
		return contentRepository.findAllByContentStatus(ContentStatus.ACTIVATED, pageable); //활성화된 컨텐츠만 조회되도록
	}

	@Override
	public ContentEntity createContent(ContentEntity newContent) {
		return contentRepository.save(newContent);
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
	public ContentEntity updateContent(ContentEntity updatedContent) {
		Optional<ContentEntity> optionalContent = contentRepository.findById(updatedContent.getId());

		if (optionalContent.isPresent()) {
			ContentEntity existingContent = optionalContent.get();

			// toBuilder()를 사용하여 새로운 엔티티 생성
			ContentEntity modifiedContent = existingContent.toBuilder()
				.url(updatedContent.getUrl())
				.title(updatedContent.getTitle())
				.script(updatedContent.getScript())
				.channelName(updatedContent.getChannelName())
				.contentStatus(updatedContent.getContentStatus())
				.updatedAt(LocalDateTime.now())
				.build();

			return contentRepository.save(modifiedContent);
		} else {
			throw new IllegalArgumentException("해당 ID의 컨텐츠를 찾을 수 없습니다.");
		}
	}

	@Override
	public void deactivateContent(String id) {
		ObjectId objectId;
		try {
			objectId = new ObjectId(id);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("유효하지 않은 ID 형식입니다.");
		}

		Optional<ContentEntity> content = contentRepository.findById(objectId);

		if (content.isPresent()) {
			ContentEntity existingContent = content.get();

			// toBuilder()를 사용하여 상태 변경
			ContentEntity modifiedContent = existingContent.toBuilder()
				.contentStatus(ContentStatus.DEACTIVATED)
				.updatedAt(LocalDateTime.now())
				.build();

			contentRepository.save(modifiedContent);
		} else {
			throw new IllegalArgumentException("해당 ID의 컨텐츠를 찾을 수 없습니다.");
		}
	}
}
