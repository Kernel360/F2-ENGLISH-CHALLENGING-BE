package com.echall.platform.content.repository.custom;

import static com.echall.platform.content.domain.entity.QContentEntity.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.echall.platform.content.domain.dto.ContentResponseDto;
import com.echall.platform.content.domain.entity.ContentDocument;
import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.content.domain.enums.SearchCondition;
import com.echall.platform.content.repository.ContentScriptRepository;
import com.querydsl.jpa.JPQLQuery;

public class ContentRepositoryImpl extends QuerydslRepositorySupport implements ContentRepositoryCustom {

	private final ContentScriptRepository contentScriptRepository;

	public ContentRepositoryImpl(ContentScriptRepository contentScriptRepository) {
		super(ContentEntity.class);
		this.contentScriptRepository = contentScriptRepository;
	}

	@Override
	public Page<ContentResponseDto.ContentViewResponseDto> search(Pageable pageable, SearchCondition searchCondition) {

		// Step 1: Fetch relevant MongoDB data
		List<String> mongoIds = contentScriptRepository.findByScriptList(searchCondition.getScript())
			.stream()
			.map(ContentDocument::getStringId)
			.toList();

		// Step 2: Fetch JPA data using Querydsl
		JPQLQuery<ContentEntity> jpaQuery = from(contentEntity)
			.select(contentEntity);
		if (searchCondition.getTitle() != null && !searchCondition.getTitle().isBlank()) {
			jpaQuery.where(contentEntity.title.containsIgnoreCase(searchCondition.getTitle()));
		}
		if (searchCondition.getScript() != null && !searchCondition.getScript().isEmpty()) {
			jpaQuery.where(contentEntity.mongoContentId.in(mongoIds));
		}

		List<ContentEntity> contentEntities = Optional.ofNullable(getQuerydsl())
			.orElseThrow(RuntimeException::new)
			.applyPagination(pageable, jpaQuery)
			.fetch();

		// Step 3: Fetch MongoDB data separately for DTO creation
		List<ContentResponseDto.ContentViewResponseDto> responseDtos = contentEntities.stream()
			.map(entity -> {
				// Fetch Mysql Entity
				String scriptSentences = entity.getPreScripts();
				if (!searchCondition.getScript().isEmpty() || !searchCondition.getTitle().isBlank()) {
					// Fetch MongoDB document
					scriptSentences
						= contentScriptRepository.findContentDocumentById(new ObjectId(entity.getMongoContentId()))
						.getScriptList().stream().limit(5).toString();
					if (scriptSentences.length() > 255) {
						scriptSentences = scriptSentences.substring(0, 255);
					}
				}

				// Create DTO with both JPA and MongoDB data
				return new ContentResponseDto.ContentViewResponseDto(
					entity.getMongoContentId(),
					entity.getTitle(),
					scriptSentences,
					entity.getContentType(),
					entity.getCreatedAt(),
					entity.getUpdatedAt()
				);
			})
			.collect(Collectors.toList());

		// Step 4: Return paginated result
		return new PageImpl<>(responseDtos, pageable, jpaQuery.fetchCount());
	}
}
