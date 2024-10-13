package com.echall.platform.scrap.repository.custom;

import java.util.List;

import com.echall.platform.scrap.domain.entity.ScrapEntity;
import com.querydsl.core.types.dsl.BooleanExpression;

public interface ScrapRepositoryCustom {
	List<ScrapEntity> findAllByUserId(Long userId);

	void deleteScrap(Long userId, Long contentId);

	boolean checkScrap(Long userId, Long contentId);
}
