package com.echall.platform.crawling.service;

import com.echall.platform.crawling.domain.dto.CrawlingResponseDto;
import com.echall.platform.script.domain.entity.Script;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.List;

public interface CrawlingService {
	// YouTube
	CrawlingResponseDto.CrawlingContentResponseDto getYoutubeInfo(String youtubeUrl, String credentials)
		throws Exception;

	List<Script> getYoutubeScript(String youtubeInfo, double duration);

	List<Script> runSelenium(WebDriver driver, String youtubeInfo, double duration) throws Exception;


	// CNN
	CrawlingResponseDto.CrawlingContentResponseDto getCNNInfo(String cnnUrl, String credentials) throws Exception;

	CrawlingResponseDto.CrawlingContentResponseDto fetchArticle(String url) throws IOException;


}