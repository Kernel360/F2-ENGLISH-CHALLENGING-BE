package com.echall.platform.bookmark.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.echall.platform.bookmark.domain.dto.BookmarkRequestDto;
import com.echall.platform.bookmark.domain.dto.BookmarkResponseDto;
import com.echall.platform.bookmark.domain.entity.BookmarkEntity;
import com.echall.platform.bookmark.repository.BookmarkRepository;
import com.echall.platform.exception.user.UserNotFoundException;
import com.echall.platform.user.domain.entity.UserEntity;
import com.echall.platform.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
	private final UserRepository userRepository;
	private final BookmarkRepository bookmarkRepository;

	@Override
	public List<BookmarkResponseDto.BookmarkMyListResponse> getBookmarks(String email, Long contentId) {
		UserEntity user = userRepository.findByEmail(email)
			.orElseThrow(() -> new UserNotFoundException(email));
		List<BookmarkEntity> bookmark = user.getBookmarks()
			.stream()
			.filter(bookmarkEntity -> bookmarkEntity.getScriptIndex().equals(contentId))
			.toList();

		return bookmark.stream()
			.map(BookmarkResponseDto.BookmarkMyListResponse::of
			)
			.toList();
	}

	@Override
	public BookmarkResponseDto.BookmarkUpdateResponse updateBookmark(
		String email, BookmarkRequestDto.BookmarkUpdateRequest bookmarkRequestDto
	) {
		UserEntity user = userRepository.findByEmail(email)
			.orElseThrow(() -> new UserNotFoundException(email));
		BookmarkEntity bookmark = BookmarkEntity.builder()
			.scriptIndex(bookmarkRequestDto.scriptIndex())
			.sentenceIndex(bookmarkRequestDto.sentenceIndex())
			.wordIndex(bookmarkRequestDto.wordIndex())
			.build();
		bookmarkRepository.save(bookmark);

		user.updateUserBookmark(bookmark);
		userRepository.save(user);

		return new BookmarkResponseDto.BookmarkUpdateResponse(
			bookmark.getScriptIndex(),
			bookmark.getId()
		);
	}
}
