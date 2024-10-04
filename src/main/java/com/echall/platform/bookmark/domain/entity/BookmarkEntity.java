package com.echall.platform.bookmark.domain.entity;

import com.echall.platform.user.domain.entity.BaseEntity;

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

	@NotNull
	private Long scriptIndex;

	private Long sentenceIndex;

	private Long wordIndex;

	@Builder
	public BookmarkEntity(
		@NotNull Long scriptIndex, Long sentenceIndex, Long wordIndex
	) {
		this.scriptIndex = scriptIndex;
		this.sentenceIndex = sentenceIndex;
		this.wordIndex = wordIndex;
	}

}
