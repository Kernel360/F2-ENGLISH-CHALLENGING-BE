package com.echall.platform.content.repository;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.content.domain.enums.ContentStatus;

public interface ContentRepository extends MongoRepository<ContentEntity, ObjectId> {
	Page<ContentEntity> findAllByContentStatus(ContentStatus contentStatus, Pageable pageable);

}
