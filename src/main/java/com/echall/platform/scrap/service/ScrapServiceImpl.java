package com.echall.platform.scrap.service;

import static com.echall.platform.message.error.code.ContentErrorCode.*;
import static com.echall.platform.message.error.code.ScrapErrorCode.*;
import static com.echall.platform.message.error.code.UserErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.echall.platform.content.repository.ContentRepository;
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
	private final ContentRepository contentRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public List<ScrapResponseDto.ScrapViewResponseDto> getAllScraps(Long userId) {
		List<ScrapEntity> scraps = scrapRepository.findAllByUserId(userId);

		return scraps
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

		ScrapEntity scrap = requestDto.toEntity(
			contentRepository.findById(requestDto.contentId())
				.orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND))
		);

		scrapRepository.save(scrap);
		user.getScraps().add(scrap);

		return ScrapResponseDto.ScrapCreateResponseDto.from(scrap.getContent());
	}

	@Override
	@Transactional
	public void deleteScrap(
		Long userId, ScrapRequestDto.ScrapDeleteRequestDto requestDto
	) {
		scrapRepository.deleteScrap(userId, requestDto.contentId());
	}

	@Override
	public boolean existsScrap(Long userId, Long contentId) {
		return scrapRepository.existsScrap(userId, contentId);
	}
}
