package com.echall.platform.category.domain.dto;

import com.echall.platform.category.domain.entity.CategoryEntity;

public record CategoryResponseDto(
	Long id,
	String name
) {
	public CategoryResponseDto(CategoryEntity category) {
		this(
			category.getId(),
			category.getName()
		);
	}

	public static CategoryResponseDto of(CategoryEntity category) {
		return new CategoryResponseDto(category);
	}
}
