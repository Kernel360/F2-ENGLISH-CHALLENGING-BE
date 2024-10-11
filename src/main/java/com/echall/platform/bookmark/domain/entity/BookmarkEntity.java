package com.echall.platform.bookmark.domain.entity;

import com.echall.platform.bookmark.domain.dto.BookmarkRequestDto;
import com.echall.platform.user.domain.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "bookmark")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, columnDefinition = "bigint")
	private Long scriptIndex;

	@Column(columnDefinition = "bigint")
	private Long sentenceIndex;

	@Column(columnDefinition = "bigint")
	private Long wordIndex;

	@Column(columnDefinition = "varchar(255)")
	private String description;

	@Builder
	public BookmarkEntity(
		@NotNull Long scriptIndex, Long sentenceIndex, Long wordIndex, String description
	) {
		this.scriptIndex = scriptIndex;
		this.sentenceIndex = sentenceIndex;
		this.wordIndex = wordIndex;
		this.description = description;
	}

	public void updateDescription(BookmarkRequestDto.BookmarkUpdateRequest bookmarkRequestDto) {
		this.description = bookmarkRequestDto.description();
	}
}
