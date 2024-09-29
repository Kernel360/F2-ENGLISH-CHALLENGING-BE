package com.echall.platform.crawling.service;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.WebDriver;

import com.echall.platform.crawling.domain.dto.CrawlingResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public interface CrawlingService {
	// YouTube
	CrawlingResponseDto.CrawlingContentResponseDto getYoutubeInfo(String youtubeUrl, String credentials) throws Exception;

	JsonNode getSnippetNode(String body) throws JsonProcessingException;

	List<String> getYoutubeScript(String youtubeInfo);

	List<String> runSelenium(WebDriver driver, String youtubeInfo) throws Exception;

	String extractVideoId(String youtubeUrl);

	String getCategoryName(String categoryId) throws Exception;

	// CNN
	CrawlingResponseDto.CrawlingContentResponseDto getCNNInfo(String cnnUrl, String credentials) throws Exception;

	CrawlingResponseDto.CrawlingContentResponseDto fetchArticle(String url) throws IOException;

	List<String> splitIntoSentences(String text);

}