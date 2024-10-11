package com.echall.platform.question.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.echall.platform.question.domain.entity.QuestionDocument;

public interface QuestionRepository extends MongoRepository<QuestionDocument, ObjectId> {

}
