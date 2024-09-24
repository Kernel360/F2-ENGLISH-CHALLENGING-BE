package com.echall.platform.content.repository.custom;

import java.util.List;

import lombok.Data;

@Data
public class SearchCondition {
	private List<String> script;
	private String title;
}
