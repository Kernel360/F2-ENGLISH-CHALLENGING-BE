package com.echall.platform.content.service;

import static com.echall.platform.message.error.code.ContentErrorCode.*;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.echall.platform.content.domain.dto.ContentRequestDto;
import com.echall.platform.content.domain.dto.ContentResponseDto;
import com.echall.platform.content.domain.entity.ContentDocument;
import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.content.domain.enums.ContentStatus;
import com.echall.platform.content.domain.enums.ContentType;
import com.echall.platform.content.domain.enums.SearchCondition;
import com.echall.platform.content.repository.ContentRepository;
import com.echall.platform.content.repository.ContentScriptRepository;
import com.echall.platform.crawling.domain.dto.CrawlingResponseDto;
import com.echall.platform.crawling.service.CrawlingServiceImpl;
import com.echall.platform.message.error.exception.CommonException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

	private final ContentRepository contentRepository;
	private final ContentScriptRepository contentScriptRepository;
	private final CrawlingServiceImpl crawlingService;

	@Override
	public Page<ContentResponseDto.ContentViewResponseDto> getAllContents(
		Pageable pageable, SearchCondition searchCondition
	) {
		return contentRepository.search(pageable, searchCondition);
	}

	@Override
	public ContentResponseDto.ContentCreateResponseDto createContent(
		Authentication authentication,
		ContentRequestDto.ContentCreateRequestDto contentCreateRequestDto
	) throws Exception {
		CrawlingResponseDto.CrawlingContentResponseDto crawlingContentResponseDto = null;

		if (contentCreateRequestDto.contentType().equals(ContentType.LISTENING)) {
			crawlingContentResponseDto = crawlingService.getYoutubeInfo(
				contentCreateRequestDto.url(), String.valueOf(authentication.getCredentials())
			);
		}
		if (contentCreateRequestDto.contentType().equals(ContentType.LEADING)) {
			crawlingContentResponseDto = crawlingService.getCNNInfo(
				contentCreateRequestDto.url(), String.valueOf(authentication.getCredentials())
			);
		}

		ContentDocument contentDocument = crawlingContentResponseDto.toDocument();
		contentScriptRepository.save(contentDocument);

		ContentEntity content = crawlingContentResponseDto.toEntity(
			contentDocument.getId(), contentCreateRequestDto.contentType()
		);
		contentRepository.save(content);

		return new ContentResponseDto.ContentCreateResponseDto(contentDocument.getId().toString(), content.getId());
	}

	@Override
	public ContentResponseDto.ContentUpdateResponseDto updateContent(
		Long id, ContentRequestDto.ContentUpdateRequestDto contentUpdateRequest
	) {
		ContentEntity content = contentRepository.findById(id)
			.orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));
		content.update(contentUpdateRequest);
		contentRepository.save(content);

		ContentDocument contentDocument
			= contentScriptRepository.findContentDocumentById(new ObjectId(content.getMongoContentId()));
		contentDocument.updateScript(contentUpdateRequest.script());
		contentScriptRepository.save(contentDocument);

		return new ContentResponseDto.ContentUpdateResponseDto(content.getId());
	}

	@Override
	public ContentResponseDto.ContentUpdateResponseDto deactivateContent(Long id) {
		ContentEntity content = contentRepository.findById(id)
			.orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));
		content.updateStatus(ContentStatus.DEACTIVATED);
		contentRepository.save(content);

		return new ContentResponseDto.ContentUpdateResponseDto(content.getId());
	}

	@Override
	public ContentResponseDto.ContentDetailResponseDto getScriptsOfContent(Long id) {
		ContentEntity content = contentRepository.findById(id)
			.orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));
		ContentDocument contentDocument = contentScriptRepository.findById(
			new ObjectId(content.getMongoContentId())
		).orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));

		return new ContentResponseDto.ContentDetailResponseDto(
			id,
			content.getContentType(),
			content.getCategory(),
			content.getTitle(),
			content.getThumbnailUrl(),
			contentDocument.getScripts()
		);
	}

}
