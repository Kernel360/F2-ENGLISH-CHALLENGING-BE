package com.echall.platform.content.repository.custom;

import static com.echall.platform.content.domain.entity.QContentEntity.*;
import static com.echall.platform.message.error.code.ContentErrorCode.*;
import static com.echall.platform.message.error.code.UserErrorCode.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;

import com.echall.platform.content.domain.dto.ContentResponseDto;
import com.echall.platform.content.domain.entity.ContentDocument;
import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.content.domain.entity.Script;
import com.echall.platform.content.domain.enums.ContentType;
import com.echall.platform.content.domain.enums.SearchCondition;
import com.echall.platform.content.repository.ContentScriptRepository;
import com.echall.platform.message.error.exception.CommonException;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;

public class ContentRepositoryImpl extends QuerydslRepositorySupport implements ContentRepositoryCustom {

	private final ContentScriptRepository contentScriptRepository;

	public ContentRepositoryImpl(ContentScriptRepository contentScriptRepository) {
		super(ContentEntity.class);
		this.contentScriptRepository = contentScriptRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ContentResponseDto.ContentViewResponseDto> search(Pageable pageable, SearchCondition searchCondition) {

		// Step 1: Fetch relevant MongoDB data
		List<String> mongoIds = contentScriptRepository.findByScripts(searchCondition.getScript())
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
			.orElseThrow(() -> new CommonException(USER_NOT_FOUND))
			.applyPagination(pageable, jpaQuery)
			.fetch();

		// Step 3: Fetch MongoDB data separately for DTO creation
		List<ContentResponseDto.ContentViewResponseDto> responseDtos = contentEntities.stream()
			.map(entity -> {
				// Fetch Mysql Entity
				String scriptSentences = entity.getPreScripts();
				if (searchCondition.getScript() != null || searchCondition.getTitle() != null) {
					// Fetch MongoDB document
					scriptSentences
						= contentScriptRepository.findContentDocumentById(new ObjectId(entity.getMongoContentId()))
						.orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND))
						.getScripts().subList(0, 5)
						.stream()
						.map(Script::getEnScript)
						.toList().toString();
					if (scriptSentences.length() > 255) {
						scriptSentences = scriptSentences.substring(0, 255);
					}
				}

				// Create DTO with both JPA and MongoDB data
				return new ContentResponseDto.ContentViewResponseDto(
					entity.getId(),
					entity.getMongoContentId(),
					entity.getTitle(),
					scriptSentences,
					scriptSentences,    // TODO: 번역 해야 하면 변경
					entity.getContentType(),
					entity.getCreatedAt(),
					entity.getUpdatedAt()
				);
			})
			.collect(Collectors.toList());

		// Step 4: Return paginated result
		return new PageImpl<>(responseDtos, pageable, jpaQuery.fetchCount());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ContentResponseDto.ContentPreviewResponseDto> getPreviewContents(
		ContentType contentType, String sortBy, int num
	) {
		Field field;
		try {
			field = contentEntity.getClass().getField(sortBy);
		} catch (NoSuchFieldException e) {
			throw new CommonException(CONTENT_SORT_COL_NOT_FOUND);
		}

		Path<?> path = Expressions.path(field.getType(), contentEntity, field.getName());

		List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
		orderSpecifiers.add(new OrderSpecifier(Order.DESC, path));

		List<ContentEntity> contents = from(contentEntity)
			.select(contentEntity)
			.where(contentEntity.contentType.eq(contentType))
			.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
			.limit(num)
			.fetch();

		return contents.stream()
			.map(content -> new ContentResponseDto.ContentPreviewResponseDto(
				content.getId(),
				content.getTitle(),
				content.getThumbnailUrl(),
				content.getContentType(),
				content.getPreScripts(),
				content.getCategory().getName(),
				content.getHits()
			)).toList();
	}

	@Override
	@Transactional
	public int updateHit(Long contentId) {
		return Math.toIntExact(
			update(contentEntity)
				.set(contentEntity.hits, contentEntity.hits.add(1))
				.where(contentEntity.id.eq(contentId))
				.execute()
		);
	}

	@Override
	public Page<ContentEntity> findAllByContentTypeAndCategory(ContentType contentType, Pageable pageable,
		Long categoryId) {
		JPQLQuery<ContentEntity> query = from(contentEntity)
			.select(contentEntity)
			.where(contentEntity.contentType.eq(contentType)
				.and(categoryId != null ? contentEntity.category.id.eq(categoryId) : null));

		List<ContentEntity> contents = getQuerydsl()
			.applyPagination(pageable, query)
			.fetch();

		JPQLQuery<Long> countQuery = from(contentEntity)
			.select(contentEntity.count())
			.where(contentEntity.contentType.eq(contentType)
				.and(categoryId != null ? contentEntity.category.id.eq(categoryId) : null));

		return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
	}

	@Override
	public String findTitleById(Long contentId) {

		return from(contentEntity)
			.select(contentEntity.title)
			.where(contentEntity.id.eq(contentId))
			.fetchFirst();
	}

	@Override
	public String findMongoIdByContentId(Long contentId) {
		return from(contentEntity)
			.select(contentEntity.mongoContentId)
			.where(contentEntity.id.eq(contentId))
			.fetchOne();
	}

}
