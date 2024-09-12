package com.echall.platform.user.repository.custom;

import java.util.List;

import com.echall.platform.user.domain.entity.UserEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class UserRepositoryImpl implements UserRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	public UserRepositoryImpl(EntityManager entityManager) {
		this.jpaQueryFactory = new JPAQueryFactory(entityManager);
	}


}
