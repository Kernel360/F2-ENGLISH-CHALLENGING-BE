package com.echall.platform.content.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.content.repository.custom.ContentRepositoryCustom;

public interface ContentRepository extends JpaRepository<ContentEntity, Long>, ContentRepositoryCustom {
}
