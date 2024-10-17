package com.echall.platform.bookmark.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.echall.platform.bookmark.domain.entity.BookmarkEntity;
import com.echall.platform.bookmark.repository.custom.BookmarkRepositoryCustom;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long>, BookmarkRepositoryCustom {
	boolean existsByScriptIndexAndSentenceIndexAndWordIndex(Long scriptIndex, Long sentenceIndex, Long wordIndex);
}
