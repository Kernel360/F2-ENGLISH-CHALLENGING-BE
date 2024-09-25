package com.echall.platform.bookmark.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.echall.platform.bookmark.domain.entity.BookmarkEntity;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {
}
