package com.echall.platform.content.repository.custom;

import static com.echall.platform.content.domain.entity.QContentEntity.*;
import static com.echall.platform.message.error.code.ContentErrorCode.*;
import static com.echall.platform.scrap.domain.entity.QScrapEntity.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

import com.echall.platform.content.domain.dto.ContentResponseDto;
import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.content.domain.enums.ContentType;
import com.echall.platform.message.error.exception.CommonException;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;

public class ContentRepositoryImpl extends QuerydslRepositorySupport implements ContentRepositoryCustom {

	public ContentRepositoryImpl() {
		super(ContentEntity.class);
	}

	@Override
	public List<ContentResponseDto.ContentPreviewResponseDto> findPreviewContents(
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

	@Override
	public List<ContentEntity> countContentByScrap(int num) {
		return from(scrapEntity)
			.select(scrapEntity.content)
			.groupBy(scrapEntity.content)
			.orderBy(scrapEntity.count().desc())
			.limit(num)
			.fetch();

	}

}
