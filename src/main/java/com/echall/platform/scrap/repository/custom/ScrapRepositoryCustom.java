package com.echall.platform.scrap.repository.custom;

import java.util.List;

import com.echall.platform.scrap.domain.entity.ScrapEntity;

public interface ScrapRepositoryCustom {
	List<ScrapEntity> findByUserId(Long userId);

	boolean findAlreadyExists(Long userId, Long contentId);

	Long deleteScrap(Long userId, Long scrapId);
}
