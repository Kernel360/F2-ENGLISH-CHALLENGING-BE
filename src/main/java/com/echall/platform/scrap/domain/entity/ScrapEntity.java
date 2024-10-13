package com.echall.platform.scrap.domain.entity;

import com.echall.platform.user.domain.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "scrap")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScrapEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long contentId;

	@Builder
	public ScrapEntity(Long id, Long contentId) {
		this.id = id;
		this.contentId = contentId;
	}

	@Builder
	public ScrapEntity(Long contentId) {
		this.contentId = contentId;
	}
}
