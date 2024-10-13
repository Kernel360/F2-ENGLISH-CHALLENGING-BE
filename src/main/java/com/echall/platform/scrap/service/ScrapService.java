package com.echall.platform.scrap.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.echall.platform.scrap.domain.dto.ScrapRequestDto;
import com.echall.platform.scrap.domain.dto.ScrapResponseDto;
import com.echall.platform.scrap.domain.entity.ScrapEntity;
import com.echall.platform.scrap.repository.custom.ScrapRepositoryCustom;

public interface ScrapService{
	List<ScrapResponseDto.ScrapViewResponseDto> getAllScraps(Long userId);

	ScrapResponseDto.ScrapCreateResponseDto createScrap(Long userId, ScrapRequestDto.ScrapCreateRequestDto requestDto);

	void deleteScrap(Long userId, ScrapRequestDto.ScrapDeleteRequestDto requestDto);

	boolean checkScrap(Long userId, ScrapRequestDto.ScrapCheckRequestDto requestDto);
}
