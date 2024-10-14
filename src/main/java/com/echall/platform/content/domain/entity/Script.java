package com.echall.platform.content.domain.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Script {
	private double startTimeInSecond;
	private double durationInSecond;
	private String enScript;
	private String koScript;

	@Builder
	public Script(
		double startTimeInSecond,
		double durationInSecond,
		String enScript,
		String koScript
	) {
		this.startTimeInSecond = startTimeInSecond;
		this.durationInSecond = durationInSecond;
		this.enScript = enScript;
		this.koScript = koScript;
	}

	public static Script of(
		double startTimeInSecond,
		double durationInSecond,
		String enScript,
		String koScript
	){
		return new Script(startTimeInSecond, durationInSecond, enScript, koScript);
	}
}
