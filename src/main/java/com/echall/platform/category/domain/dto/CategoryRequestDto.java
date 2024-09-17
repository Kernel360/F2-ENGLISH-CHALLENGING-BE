package com.echall.platform.category.domain.dto;

import com.echall.platform.category.domain.entity.CategoryEntity;

public record CategoryRequestDto(
	String name
) {
	public CategoryEntity toEntity() {
		return CategoryEntity.builder()
			.name(this.name)
			.build();
	}
}
