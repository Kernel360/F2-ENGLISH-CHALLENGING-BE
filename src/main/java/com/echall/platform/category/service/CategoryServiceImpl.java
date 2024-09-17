package com.echall.platform.category.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.echall.platform.category.domain.dto.CategoryRequestDto;
import com.echall.platform.category.domain.dto.CategoryResponseDto;
import com.echall.platform.category.domain.entity.CategoryEntity;
import com.echall.platform.category.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService{

	private CategoryRepository categoryRepository;
	@Override
	public List<CategoryEntity> getAllCategories() {
		return categoryRepository.findAll();
	}

	@Override
	public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
		return null;
	}

	@Override
	public CategoryResponseDto updateCategory(Long id, CategoryRequestDto updatedCategory) {
		return null;
	}

	@Override
	public CategoryResponseDto deleteCategory(Long id) {
		return null;
	}
}
