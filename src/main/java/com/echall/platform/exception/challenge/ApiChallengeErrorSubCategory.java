package com.echall.platform.exception.challenge;

import com.echall.platform.exception.common.ApiErrorSubCategory;

public enum ApiChallengeErrorSubCategory implements ApiErrorSubCategory {
	CHALLENGE_NOT_FOUND("존재하지 않는 챌린지입니다."),

	CHALLENGE_ALREADY_EXISTS("이미 존재하는 챌린지입니다."),

	CHALLENGE_DEACTIVATE("삭제된 챌린지입니다.");

	private final String challengeApiErrorSubCategory;

	ApiChallengeErrorSubCategory(String challengeApiErrorSubCategory) {
		this.challengeApiErrorSubCategory = challengeApiErrorSubCategory;
	}

	@Override
	public String toString() {
		return String.format("[원인: %s]", this.challengeApiErrorSubCategory);
	}
}
