package com.echall.platform.crawling.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.echall.platform.crawling.domain.dto.CrawlingResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.bonigarcia.wdm.WebDriverManager;

@Service
public class CrawlingService {
	@Value("${YOUTUBE_API_KEY}")
	private String YOUTUBE_API_KEY;

	public CrawlingResponseDto.YoutubeResponseDto getYoutubeInfo(String youtubeUrl, String credentials)
		throws Exception {
		// Extract the video ID from the URL
		String videoId = extractVideoId(youtubeUrl);
		// Create the request URL for the transcript service
		String requestUrl = "https://www.googleapis.com/youtube/v3/videos?id=" + videoId
			+ "&part=snippet, contentDetails" + "&key=" + YOUTUBE_API_KEY;

		// Set up headers with your API key
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cookie", "access_token=" + credentials);

		// Create an HTTP entity with headers
		// Use RestTemplate to make the request
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response
			= restTemplate.exchange(requestUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);

		JsonNode snippetNode = getSnippetNode(response.getBody());

		return new CrawlingResponseDto.YoutubeResponseDto(
			youtubeUrl,
			snippetNode.path("channelTitle").asText(),
			snippetNode.path("title").asText(),
			snippetNode.path("thumbnails").asText(),
			getCategoryName(snippetNode.path("categoryId").asText()),
			getYoutubeScript(youtubeUrl)
		);
	}



	// Internal Methods ------------------------------------------------------------------------------------------------
	private JsonNode getSnippetNode(String body) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readTree(body).path("items").get(0).path("snippet");
	}

	private List<String> getYoutubeScript(String youtubeInfo) {
		WebDriverManager.chromedriver().setup();

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		options.addArguments(
			"user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36");

		WebDriver driver = new ChromeDriver(options);
		List<String> transcriptLines;

		try {
			transcriptLines = runSelenium(driver, youtubeInfo);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			driver.quit();
		}

		return transcriptLines;
	}

	private List<String> runSelenium(WebDriver driver, String youtubeInfo) throws Exception {
		List<String> scripts = new ArrayList<>();
		driver.get(youtubeInfo);
		driver.manage().window().maximize();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		JavascriptExecutor js = (JavascriptExecutor)driver;
		wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));

/*			// TODO: 혹시 필요하면 주석 풀어서 사용해야 함
			try {
				WebElement closeAdButton = wait.until(
					ExpectedConditions.elementToBeClickable(By.cssSelector("button[aria-label='광고 건너뛰기']")));
				closeAdButton.click();
			} catch (Exception e) {
				System.out.println("광고가 없습니다.");
			}*/

		// Zoom out & Scroll down
		js.executeScript("document.body.style.zoom='30%'");
		Thread.sleep(2000);

		// Click the "더보기" button to expand
		List<WebElement> expandButton = driver.findElements(By.cssSelector("tp-yt-paper-button#expand"));
		for (WebElement button : expandButton) {
			if (button.isDisplayed() && button.isEnabled()) {
				button.click();
				break;
			}
		}
		Thread.sleep(3000);

		// Locate and click the "스크립트 표시" button
		WebElement transcriptButton = wait.until(
			ExpectedConditions.elementToBeClickable(By.xpath("//yt-button-shape//button[@aria-label='스크립트 표시']")));
		transcriptButton.click();
		Thread.sleep(5000);

		// Use XPath to find all elements containing the transcript text
		List<WebElement> segmentTextElements = driver.findElements(
			By.xpath("//yt-formatted-string[@class='segment-text style-scope ytd-transcript-segment-renderer']"));

		for (WebElement element : segmentTextElements) {
			String text = element.getText();
			if (text != null && !text.isEmpty()) {
				scripts.add(text);
			}
		}
		return scripts;
	}

	private String extractVideoId(String youtubeUrl) {
		// Logic to extract video ID from URL
		String[] parts = youtubeUrl.split("v=");
		return parts.length > 1 ? parts[1] : "";
	}

	private String getCategoryName(String categoryId) throws Exception {
		String requestUrl =
			"https://www.googleapis.com/youtube/v3/videoCategories?part=snippet&regionCode=US&key=" + YOUTUBE_API_KEY;

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = objectMapper.readTree(response.getBody());
		JsonNode itemsNode = rootNode.path("items");

		for (JsonNode item : itemsNode) {
			if (item.path("id").asText().equals(categoryId)) {
				return item.path("snippet").path("title").asText();
			}
		}

		return "Unknown Category";
	}
}
