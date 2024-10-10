package com.echall.platform.bookmark.service;

import static com.echall.platform.message.error.code.BookmarkErrorCode.*;
import static com.echall.platform.message.error.code.UserErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.echall.platform.bookmark.domain.dto.BookmarkRequestDto;
import com.echall.platform.bookmark.domain.dto.BookmarkResponseDto;
import com.echall.platform.bookmark.domain.entity.BookmarkEntity;
import com.echall.platform.bookmark.repository.BookmarkRepository;
import com.echall.platform.message.error.exception.CommonException;
import com.echall.platform.user.domain.entity.UserEntity;
import com.echall.platform.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
	private final UserRepository userRepository;
	private final BookmarkRepository bookmarkRepository;

	@Override
	@Transactional(readOnly = true)
	public List<BookmarkResponseDto.BookmarkMyListResponse> getBookmarks(String email, Long contentId) {
		UserEntity user = userRepository.findByEmail(email)
			.orElseThrow(() -> new CommonException(USER_NOT_FOUND));
		List<BookmarkEntity> bookmarks = user.getBookmarks()
			.stream()
			.filter(bookmarkEntity -> bookmarkEntity.getScriptIndex().equals(contentId))
			.toList();
		if (bookmarks.isEmpty()) {
			throw new CommonException(BOOKMARK_NOT_FOUND);
		}

		return bookmarks.stream()
			.map(BookmarkResponseDto.BookmarkMyListResponse::of)
			.toList();
	}

	@Override
	@Transactional
	public BookmarkResponseDto.BookmarkCreateResponse createBookmark(
		String email, BookmarkRequestDto.BookmarkCreateRequest bookmarkRequestDto, Long contentId
	) {
		// TODO: 스크립 기능 생기면 word, sentence 모두 null인 경우 예외 처리 해야 함
		if (bookmarkRequestDto.wordIndex() != null && bookmarkRequestDto.sentenceIndex() == null) {
			throw new CommonException(BOOKMARK_WORD_NEED_SENTENCE);
		} // TODO: DTO 커스텀 해서 DTO 에서 검증 가능

		UserEntity user = userRepository.findByEmail(email)
			.orElseThrow(() -> new CommonException(USER_NOT_FOUND));

		BookmarkEntity bookmark = BookmarkEntity.builder()
			.scriptIndex(contentId)
			.sentenceIndex(bookmarkRequestDto.sentenceIndex())
			.wordIndex(bookmarkRequestDto.wordIndex())
			.description(null)
			.build();

		if (bookmarkRepository.existsByScriptIndexAndScriptIndexAndWordIndex(
			bookmark.getScriptIndex(), bookmark.getSentenceIndex(), bookmark.getWordIndex())
		) {
			throw new CommonException(BOOKMARK_ALREADY_EXISTS);
		}

		bookmarkRepository.save(bookmark);
		user.updateUserBookmark(bookmark);

		return BookmarkResponseDto.BookmarkCreateResponse.of(bookmark);
	}

	@Override
	@Transactional
	public BookmarkResponseDto.BookmarkMyListResponse updateBookmark(
		BookmarkRequestDto.BookmarkUpdateRequest bookmarkRequestDto, Long contentId
	) {

		BookmarkEntity bookmark = bookmarkRepository.findById(bookmarkRequestDto.bookmarkId())
			.orElseThrow(() -> new CommonException(BOOKMARK_NOT_FOUND));

		bookmark.updateDescription(bookmarkRequestDto);

		return BookmarkResponseDto.BookmarkMyListResponse.of(bookmark);
	}

	@Override
	@Transactional
	public BookmarkResponseDto.BookmarkDeleteResponse deleteBookmark(Long bookmarkId) {
		BookmarkEntity bookmark = bookmarkRepository.findById(bookmarkId)
			.orElseThrow(() -> new CommonException(BOOKMARK_NOT_FOUND));

		bookmarkRepository.delete(bookmark);

		return new BookmarkResponseDto.BookmarkDeleteResponse(bookmarkId);
	}
}
