package com.echall.platform.scrap.service;

import static com.echall.platform.message.error.code.ScrapErrorCode.*;
import static com.echall.platform.message.error.code.UserErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.echall.platform.message.error.exception.CommonException;
import com.echall.platform.scrap.domain.dto.ScrapRequestDto;
import com.echall.platform.scrap.domain.dto.ScrapResponseDto;
import com.echall.platform.scrap.domain.entity.ScrapEntity;
import com.echall.platform.scrap.repository.ScrapRepository;
import com.echall.platform.user.domain.entity.UserEntity;
import com.echall.platform.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScrapServiceImpl implements ScrapService {
	private final ScrapRepository scrapRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public List<ScrapResponseDto.ScrapViewResponseDto> getAllScraps(Long userId) {
		List<ScrapEntity> scrap = scrapRepository.findAllByUserId(userId);
		if (scrap.isEmpty()) {
			throw new CommonException(SCRAP_NOT_FOUND);
		}

		return scrap
			.stream()
			.map(ScrapResponseDto.ScrapViewResponseDto::from)
			.toList();
	}

	@Override
	@Transactional
	public ScrapResponseDto.ScrapCreateResponseDto createScrap(
		Long userId, ScrapRequestDto.ScrapCreateRequestDto requestDto
	) {
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new CommonException(USER_NOT_FOUND));

		if (user.hasContent(requestDto.contentId())) {
			throw new CommonException(SCRAP_ALREADY_EXISTS);
		}

		ScrapEntity scrap = requestDto.toEntity();

		scrapRepository.save(scrap);
		user.getScraps().add(scrap);

		return ScrapResponseDto.ScrapCreateResponseDto.from(scrap.getContentId());
	}

	@Override
	@Transactional
	public void deleteScrap(
		Long userId, ScrapRequestDto.ScrapDeleteRequestDto requestDto
	) {
		scrapRepository.deleteScrap(userId, requestDto.scrapId());
	}
}
