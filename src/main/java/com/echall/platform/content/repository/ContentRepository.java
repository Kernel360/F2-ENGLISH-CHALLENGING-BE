package com.echall.platform.content.repository;

import com.echall.platform.content.domain.enums.ContentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.content.repository.custom.ContentRepositoryCustom;

public interface ContentRepository extends JpaRepository<ContentEntity, Long>, ContentRepositoryCustom {
    Page<ContentEntity> findByContentType(ContentType contentType, Pageable pageable);
}
