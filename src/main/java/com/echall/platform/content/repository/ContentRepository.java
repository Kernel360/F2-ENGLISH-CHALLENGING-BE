package com.echall.platform.content.repository;

import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.content.repository.custom.ContentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<ContentEntity, Long>, ContentRepositoryCustom {
}
