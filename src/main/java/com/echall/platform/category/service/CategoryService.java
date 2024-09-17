package com.echall.platform.category.service;

import java.util.List;

import com.echall.platform.category.domain.dto.CategoryRequestDto;
import com.echall.platform.category.domain.dto.CategoryResponseDto;

public interface CategoryService {
	CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto); //새로운 카테고리 생성하기
	CategoryResponseDto updateCategory(Long id, CategoryRequestDto updatedCategory);
	CategoryResponseDto deleteCategory(Long id);

	List<CategoryResponseDto> findAllCategory(); //모든 카테고리 가져오기

	CategoryResponseDto findCategoryById(Long id);
}