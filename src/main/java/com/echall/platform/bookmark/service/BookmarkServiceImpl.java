package com.echall.platform.bookmark.service;

import static com.echall.platform.message.error.code.BookmarkErrorCode.*;
import static com.echall.platform.message.error.code.ContentErrorCode.*;
import static com.echall.platform.message.error.code.UserErrorCode.*;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.echall.platform.bookmark.domain.dto.BookmarkRequestDto;
import com.echall.platform.bookmark.domain.dto.BookmarkResponseDto;
import com.echall.platform.bookmark.domain.entity.BookmarkEntity;
import com.echall.platform.bookmark.repository.BookmarkRepository;
import com.echall.platform.content.domain.entity.ContentDocument;
import com.echall.platform.content.repository.ContentRepository;
import com.echall.platform.content.repository.ContentScriptRepository;
import com.echall.platform.message.error.exception.CommonException;
import com.echall.platform.user.domain.entity.UserEntity;
import com.echall.platform.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
	private final UserRepository userRepository;
	private final BookmarkRepository bookmarkRepository;
	private final ContentRepository contentRepository;
	private final ContentScriptRepository contentScriptRepository;

	@Override
	@Transactional(readOnly = true)
	public List<BookmarkResponseDto.BookmarkListResponseDto> getBookmarks(String email, Long contentId) {
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
			.map(BookmarkResponseDto.BookmarkListResponseDto::of)
			.toList();
	}

	@Override
	public List<BookmarkResponseDto.BookmarkMyListResponseDto> getAllBookmarks(Long userId) {
		List<BookmarkEntity> bookmarks = bookmarkRepository.getAllBookmarks(userId);

		return bookmarks.stream()
			.map(bookmark -> BookmarkResponseDto.BookmarkMyListResponseDto.of(
				bookmark, contentRepository.findTitleById(bookmark.getScriptIndex())
			)).toList();
	}

	@Override
	@Transactional
	public BookmarkResponseDto.BookmarkCreateResponse createBookmark(
		String email, BookmarkRequestDto.BookmarkCreateRequest bookmarkRequestDto, Long contentId
	) {

		UserEntity user = userRepository.findByEmail(email)
			.orElseThrow(() -> new CommonException(USER_NOT_FOUND));
		ContentDocument content = contentScriptRepository.findContentDocumentById(
			new ObjectId(contentRepository.findMongoIdByContentId(contentId))
		).orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));

		if (bookmarkRepository.existsByScriptIndexAndScriptIndexAndWordIndex(
			contentId, bookmarkRequestDto.sentenceIndex(), bookmarkRequestDto.wordIndex()
		)) {
			throw new CommonException(BOOKMARK_ALREADY_EXISTS);
		}

		BookmarkEntity bookmark = BookmarkEntity.builder()
			.scriptIndex(contentId)
			.sentenceIndex(bookmarkRequestDto.sentenceIndex())
			.wordIndex(bookmarkRequestDto.wordIndex())
			.description(bookmarkRequestDto.description())
			.detail(extractDetail(bookmarkRequestDto, content))
			.build();

		bookmarkRepository.save(bookmark);
		user.updateUserBookmark(bookmark);

		return BookmarkResponseDto.BookmarkCreateResponse.of(bookmark);
	}

	@Override
	@Transactional
	public BookmarkResponseDto.BookmarkListResponseDto updateBookmark(
		BookmarkRequestDto.BookmarkUpdateRequest bookmarkRequestDto, Long contentId
	) {

		BookmarkEntity bookmark = bookmarkRepository.findById(bookmarkRequestDto.bookmarkId())
			.orElseThrow(() -> new CommonException(BOOKMARK_NOT_FOUND));

		bookmark.updateDescription(bookmarkRequestDto);

		return BookmarkResponseDto.BookmarkListResponseDto.of(bookmark);
	}

	@Override
	@Transactional
	public void deleteBookmark(Long userId, Long bookmarkId) {
		bookmarkRepository.deleteBookmark(userId, bookmarkId);
	}

	// Internal Methods ------------------------------------------------------------------------------------------------
	private String extractDetail(
		BookmarkRequestDto.BookmarkCreateRequest bookmarkRequestDto, ContentDocument content
	) {
		return truncate(bookmarkRequestDto.wordIndex() == null ?
				content.getScripts().get(Math.toIntExact(bookmarkRequestDto.sentenceIndex())).getEnScript() :
				content.getScripts().get(Math.toIntExact(bookmarkRequestDto.sentenceIndex())).getEnScript()
					.split(" ")[Math.toIntExact(bookmarkRequestDto.wordIndex())],
			255);
	}

	private String truncate(String content, int maxLength) {
		return content.length() > maxLength ? content.substring(0, maxLength) : content;
	}
}
