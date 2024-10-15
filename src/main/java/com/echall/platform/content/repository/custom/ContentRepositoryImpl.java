package com.echall.platform.content.repository.custom;

import static com.echall.platform.content.domain.entity.QContentEntity.*;
import static com.echall.platform.message.error.code.ContentErrorCode.*;
import static com.echall.platform.scrap.domain.entity.QScrapEntity.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

import com.echall.platform.content.domain.dto.ContentRequestDto;
import com.echall.platform.content.domain.dto.ContentResponseDto;
import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.content.domain.enums.ContentType;
import com.echall.platform.message.error.exception.CommonException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
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
	public Page<ContentEntity> findAllBySearchCondition(
		ContentRequestDto.ContentSearchDto searchDto, Pageable pageable
	) {
		BooleanBuilder booleanBuilder = new BooleanBuilder();

		List<String> searchWords = splitIntoWords(searchDto.searchWords());

		for (String word : searchWords) {
			booleanBuilder.or(contentEntity.title.containsIgnoreCase(word)
				.or(contentEntity.preScripts.containsIgnoreCase(word)));
		}

		JPQLQuery<ContentEntity> query = Objects.requireNonNull(getQuerydsl()).createQuery(contentEntity)
			.select(contentEntity)
			.from(contentEntity)
			.where(booleanBuilder);

		List<ContentEntity> contents = Objects.requireNonNull(getQuerydsl())
			.applyPagination(pageable, query)
			.fetch();

		JPQLQuery<Long> countQuery = getQuerydsl().createQuery(contentEntity)
			.select(contentEntity.count())
			.from(contentEntity)
			.where(booleanBuilder);

		return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);

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
	public List<ContentResponseDto.ContentByScrapCountDto> contentByScrapCount(int num) {
		List<Tuple> tuples = from(scrapEntity)
			.select(scrapEntity.content.id, scrapEntity.count())
			.groupBy(scrapEntity.content.id)
			.orderBy(scrapEntity.count().desc())
			.limit(num)
			.fetch();

		List<Long> contentIds = tuples.stream()
			.map(
				tuple -> tuple.get(scrapEntity.content.id)
			).toList();

		List<ContentEntity> contents = from(contentEntity)
			.select(contentEntity)
			.where(contentEntity.id.in(contentIds))
			.fetch();

		return contents.stream()
			.map(content -> {
				Long count = tuples.stream()
					.filter(tuple -> Objects.equals(tuple.get(scrapEntity.content.id), content.getId()))
					.findFirst()
					.map(tuple -> tuple.get(scrapEntity.count()))
					.orElse(0L);
				return ContentResponseDto.ContentByScrapCountDto.of(content, count);
			}).sorted(Comparator.comparing(ContentResponseDto.ContentByScrapCountDto::countScrap).reversed())
			.toList();

	}

	private List<String> splitIntoWords(String words) {
		return Arrays.asList(words.replace(',', ' ').split("\\s+"));
	}
}
