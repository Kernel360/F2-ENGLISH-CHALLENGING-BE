package com.echall.platform.scrap.domain.entity;

import com.echall.platform.user.domain.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "scrap")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScrapEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	private Long contentId;

	@Builder
	public ScrapEntity(Long id, Long contentId) {
		this.id = id;
		this.contentId = contentId;
	}

}
