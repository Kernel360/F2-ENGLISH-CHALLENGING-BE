package com.echall.platform.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.echall.platform.category.domain.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

}
