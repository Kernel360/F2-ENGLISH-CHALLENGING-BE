package com.echall.platform.content.repository.custom;

import com.echall.platform.content.domain.dto.ContentResponseDto;
import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.content.domain.enums.ContentType;
import com.echall.platform.content.repository.ContentScriptRepository;
import com.echall.platform.message.error.exception.CommonException;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.echall.platform.content.domain.entity.QContentEntity.contentEntity;
import static com.echall.platform.message.error.code.ContentErrorCode.CONTENT_SORT_COL_NOT_FOUND;

public class ContentRepositoryImpl extends QuerydslRepositorySupport implements ContentRepositoryCustom {

	private final ContentScriptRepository contentScriptRepository;

	public ContentRepositoryImpl(ContentScriptRepository contentScriptRepository) {
		super(ContentEntity.class);
		this.contentScriptRepository = contentScriptRepository;
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
	public Page<ContentEntity> findAllByContentTypeAndCategory(ContentType contentType, Pageable pageable, Long categoryId) {
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

}
