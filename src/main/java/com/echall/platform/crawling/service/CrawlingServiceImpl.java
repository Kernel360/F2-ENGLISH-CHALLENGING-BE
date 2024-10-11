package com.echall.platform.crawling.service;

import static com.echall.platform.message.error.code.CrawlingErrorCode.*;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import com.echall.platform.content.domain.entity.Script;
import com.echall.platform.crawling.domain.dto.CrawlingResponseDto;
import com.echall.platform.message.error.exception.CommonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import groovy.util.logging.Slf4j;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;

@lombok.extern.slf4j.Slf4j
@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlingServiceImpl implements CrawlingService {
	private final TranslateService translateService;
	@Value("${YOUTUBE_API_KEY}")
	private String YOUTUBE_API_KEY;

	@Override
	public CrawlingResponseDto.CrawlingContentResponseDto getYoutubeInfo(String youtubeUrl, String credentials)
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

		JsonNode snippetNode = getSnippetNode(response.getBody()).path("snippet");
		JsonNode contentDetailsNode = getSnippetNode(response.getBody()).path("contentDetails");

		Duration duration = Duration.parse(contentDetailsNode.path("duration").asText());
		if (duration.compareTo(Duration.ofMinutes(8)) > 0) {
			throw new CommonException(CRAWLING_OUT_OF_BOUNDS);
		}

		return new CrawlingResponseDto.CrawlingContentResponseDto(
			youtubeUrl,
			snippetNode.path("title").asText(),
			getThumbnailUrl(snippetNode.path("thumbnails"), 0),
			getCategoryName(snippetNode.path("categoryId").asText()),
			getYoutubeScript(youtubeUrl, Double.parseDouble(String.valueOf(duration.getSeconds())))
		);
	}

	@Override
	public CrawlingResponseDto.CrawlingContentResponseDto getCNNInfo(String cnnUrl, String credentials) {
		CrawlingResponseDto.CrawlingContentResponseDto cnnResponseDto = null;
		try {
			cnnResponseDto = fetchArticle(cnnUrl);
		} catch (IOException e) {
			throw new CommonException(CRAWLING_JSOUP_FAILURE);
		}

		return cnnResponseDto;
	}

	// Internal Methods ------------------------------------------------------------------------------------------------

	@Override
	public JsonNode getSnippetNode(String body) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readTree(body).path("items").get(0);
	}

	@Override
	public String getThumbnailUrl(JsonNode thumbnailNode, int index) {
		Iterator<Map.Entry<String, JsonNode>> thumbnails = thumbnailNode.fields();
		HashMap<String, JsonNode> thumbnailHashMap = new HashMap<>();

		while (thumbnails.hasNext()) {
			Map.Entry<String, JsonNode> thumbnailMap = thumbnails.next();
			thumbnailHashMap.put(thumbnailMap.getKey(), thumbnailMap.getValue());
		}

		List<String> keys = Collections.singletonList(thumbnailHashMap.keySet().stream().toList().get(0));

		return thumbnailHashMap.get(keys.get(index)).get("url").toString();
	}

	@Override
	public List<Script> getYoutubeScript(String youtubeInfo, double seconds) {
		// 운영체제 감지
		String os = System.getProperty("os.name").toLowerCase();
		System.out.println("Operating System: " + os);

		// 공통 옵션 설정
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-gpu");
		options.addArguments("--remote-allow-origins=*");
		options.addArguments("--disable-popup-blocking");
		options.addArguments("--lang=en-US");

		WebDriverManager.chromedriver().setup();

		if (os.contains("linux")) {
			log.error("LINUX");
			// Ubuntu의 경우
			options.addArguments("--headless");
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");
			options.addArguments("--ignore-ssl-errors=yes");
			options.addArguments("--ignore-certificate-errors");
			/*// Xvfb를 사용하는 경우
			try {
				if (System.getenv("DISPLAY") == null) {
					log.error("XVFB");
					// Xvfb가 설치되어 있는지 확인
					Process process = Runtime.getRuntime().exec("which xvfb");
					int exitCode = process.waitFor();
					if (exitCode != 0) {
						// Xvfb가 설치되어 있지 않으면 설치
						Process installProcess = Runtime.getRuntime().exec("sudo apt-get install -y xvfb");
						installProcess.waitFor();
					}
					System.setProperty("DISPLAY", ":99");
					// Xvfb를 실행
					Process xvfbProcess = Runtime.getRuntime().exec("Xvfb :99 -ac &");
					xvfbProcess.waitFor();
					log.error("XVFB START");
				}
			}catch (IOException | InterruptedException e){
				throw new CommonException(SELENIUM_RUNTIME_ERROR);
			}*/
		}

		WebDriver driver = new ChromeDriver(options);
		List<Script> transcriptLines;

		try {
			log.error("SELENIUM DRIVER SET SUCCESS");
			transcriptLines = runSelenium(driver, youtubeInfo, seconds);
		} catch (Exception e) {
			throw new CommonException(SELENIUM_RUNTIME_ERROR);
		} finally {
			driver.quit();
		}

		return transcriptLines;
	}

	@Override
	public List<Script> runSelenium(WebDriver driver, String youtubeInfo, double seconds) throws Exception {
		List<Script> scripts = new ArrayList<>();

		driver.get(youtubeInfo);
		setUpSelenium(driver);
		log.error("SELENIUM DRIVER SET SUCCESS2");

		// Use XPath to find all elements containing the transcript text
		List<WebElement> segmentElements = driver.findElements(
			By.xpath("//ytd-transcript-segment-renderer"));
		for (int i = 0; i < segmentElements.size(); ++i) {
			log.error("START CRAWLING");

			String time = segmentElements.get(i)
				.findElement(By.xpath(".//div[contains(@class, 'timestamp')]"))
				.getText();
			double startTime = getSecondsFromString(time);
			double endtime = seconds;

			if (i + 1 < segmentElements.size()) {
				endtime = getSecondsFromString(
					segmentElements.get(i + 1)
						.findElement(By.xpath(".//div[contains(@class, 'timestamp')]"))
						.getText()
				);
			}

			String text = segmentElements.get(i)
				.findElement(By.xpath(".//yt-formatted-string[contains(@class, 'segment-text')]"))
				.getText();
			log.error("FIND TEXT");
			if (text != null && !text.isEmpty()) {
				scripts.add(
					Script.builder()
						.startTimeInSecond(startTime)
						.durationInSecond(endtime - startTime)
						.enScript(text)
						.koScript(translateService.translate(text, "en", "ko"))
						.build()
				);

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

	@Override
	public CrawlingResponseDto.CrawlingContentResponseDto fetchArticle(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();

		// 제목 추출
		String title = doc.select("h1.headline__text").text();

		// 카테고리 추출
		Element categoryElement = doc.selectFirst("meta[name=meta-section]");
		String category = categoryElement != null ? categoryElement.attr("content") : "Unknown Category";

		// 이미지 URL 추출
		Elements images = doc.select("img.image__dam-img[src]");
		/*List<String> imageUrls = new ArrayList<>();
		for (Element img : images) {
			imageUrls.add(img.attr("src"));
		}
		String imgUrl = imageUrls.toString();*/
		String imgUrl = images.get(0).attr("src").toString();

		// 본문 추출
		Elements paragraphs = doc.select("div.article__content p");
		StringBuilder fullText = new StringBuilder();
		for (Element paragraph : paragraphs) {
			fullText.append(paragraph.text()).append(" ");
		}

		// 본문을 문장 단위로 나누기
		List<String> sentences = splitIntoSentences(fullText.toString());

		return new CrawlingResponseDto.CrawlingContentResponseDto(
			url,
			title,
			imgUrl,
			category,
			sentences.stream()
				.map(
					sentence -> Script.builder()
						.startTimeInSecond(0)
						.durationInSecond(0)
						.enScript(sentence)
						.koScript(translateService.translate(sentence, "en", "ko"))
						.build()
				).toList()
		);
	}

	// Private Methods -------------------------------------------------------------------------------------------------
	private void setUpSelenium(WebDriver driver)
		throws InterruptedException {
		log.error("SETUP SELENIUM START");
		// Initial setting
		driver.manage().window().maximize();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		JavascriptExecutor js = (JavascriptExecutor)driver;
		wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));
		Thread.sleep(5000);
		log.error("SETUP SUCCESS");
		// Zoom out
		js.executeScript("document.body.style.zoom='30%'");
		Thread.sleep(5000);
		log.error("ZOOMOUT SUCESS");
		// Click the "expand" button to expand
		List<WebElement> expandButton = driver.findElements(By.cssSelector("tp-yt-paper-button#expand"));
		log.error("FIND EXPAND BUTTON : {} ", expandButton);
		for (WebElement button : expandButton) {
			log.error("FIND BUTTON : {} ", button.getText());
			if (button.isDisplayed() && button.isEnabled()) {
				button.click();
				log.error("EXPAND BUTTON CLICK");
				break;
			}
		}
		Thread.sleep(3000);
		// Locate and click the "Show transcript" button
		WebElement transcriptButton = wait.until(
			ExpectedConditions.elementToBeClickable(
				By.xpath("//yt-button-shape//button[@aria-label='Show transcript']")));
		log.error("FIND TRANSCRIPT BUTTON");
		transcriptButton.click();
		log.error("CLICK TRANSCRIPTION");
		Thread.sleep(5000);
	}
/*
	private String translateTextWithPython(String text) {
		String pythonPath = "src\\main\\java\\com\\echall\\platform\\crawling\\bot\\Translator.py";

		try {
			ProcessBuilder processBuilder = new ProcessBuilder(Arrays.asList("python", pythonPath));
			Process process = processBuilder.start();

			// Write input to Python process
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			writer.write(text);
			writer.newLine();
			writer.flush();

			// Read output from Python process
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			return reader.readLine();
		} catch (IOException e) {
			throw new CommonException(CRAWLING_TRANSLATE_FAILURE);
		}
	}*/

	private List<String> splitIntoSentences(String text) {
		List<String> sentences = new ArrayList<>();

		// 정규 표현식을 사용하여 문장 단위로 나누기
		String[] splitSentences = text.split("(?<=[.!?])\\s+");

		for (String sentence : splitSentences) {
			sentences.add(sentence.trim());
		}

		return sentences;
	}

	private double getSecondsFromString(String time) {
		return Double.parseDouble(time.split(":")[0]) * 60 + Double.parseDouble(time.split(":")[1]);
	}
}