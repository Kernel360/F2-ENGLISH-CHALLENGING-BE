package com.echall.platform.scrap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.echall.platform.scrap.domain.entity.ScrapEntity;
import com.echall.platform.scrap.repository.custom.ScrapRepositoryCustom;

public interface ScrapRepository extends JpaRepository<ScrapEntity, Long>, ScrapRepositoryCustom {
	Long countByContentId(Long content_id);
}
