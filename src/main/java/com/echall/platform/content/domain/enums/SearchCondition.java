package com.echall.platform.content.domain.enums;

import java.util.List;

import lombok.Data;

@Data
public class SearchCondition {
	private List<String> script;
	private String title;
}
