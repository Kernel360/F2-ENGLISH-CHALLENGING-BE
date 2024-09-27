package com.echall.platform.crawling.service;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
public class CrawlingServiceImpl implements CrawlingService {
	@Value("${YOUTUBE_API_KEY}")
	private String YOUTUBE_API_KEY;

	private static List<String> splitIntoSentences(String text) {
		List<String> sentences = new ArrayList<>();

		// 정규 표현식을 사용하여 문장 단위로 나누기
		String[] splitSentences = text.split("(?<=[.!?])\\s+");

		for (String sentence : splitSentences) {
			sentences.add(sentence.trim());
		}

		return sentences;
	}

	@Override
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

	@Override
	public CrawlingResponseDto.CNNResponseDto getCNNInfo(String cnnUrl, String credentials) {
		CrawlingResponseDto.CNNResponseDto cnnResponseDto = null;
		try {
			cnnResponseDto = fetchArticle(cnnUrl);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return cnnResponseDto;
	}

	// Internal Methods ------------------------------------------------------------------------------------------------
	@Override
	public JsonNode getSnippetNode(String body) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readTree(body).path("items").get(0).path("snippet");
	}

	@Override
	public List<String> getYoutubeScript(String youtubeInfo) {
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

	@Override
	public List<String> runSelenium(WebDriver driver, String youtubeInfo) throws Exception {
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

	@Override
	public String extractVideoId(String youtubeUrl) {
		// Logic to extract video ID from URL
		String[] parts = youtubeUrl.split("v=");
		return parts.length > 1 ? parts[1] : "";
	}

	@Override
	public String getCategoryName(String categoryId) throws Exception {
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

	public CrawlingResponseDto.CNNResponseDto fetchArticle(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();

		// 제목 추출
		String title = doc.select("h1.headline__text").text();

		// 카테고리 추출
		Element categoryElement = doc.selectFirst("meta[name=meta-section]");
		String category = categoryElement != null ? categoryElement.attr("content") : "Unknown Category";

		// 이미지 URL 추출
		Elements images = doc.select("img.image__dam-img[src]");
		List<String> imageUrls = new ArrayList<>();
		for (Element img : images) {
			imageUrls.add(img.attr("src"));
		}
		String imgUrl = imageUrls.toString();

		// 본문 추출
		Elements paragraphs = doc.select("div.article__content p");
		StringBuilder fullText = new StringBuilder();
		for (Element paragraph : paragraphs) {
			fullText.append(paragraph.text()).append(" ");
		}

		// 본문을 문장 단위로 나누기
		List<String> sentences = splitIntoSentences(fullText.toString());

		return new CrawlingResponseDto.CNNResponseDto(
			url,
			title,
			imgUrl,
			category,
			sentences
		);
	}
}