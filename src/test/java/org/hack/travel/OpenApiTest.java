package org.hack.travel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.hack.travel.global.exception.LocationNotFoundException;
import org.hack.travel.domain.itinerary.api.KakaoMapApi;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest
public class OpenApiTest {

	@MockBean
	private RestTemplate restTemplate;

	@InjectMocks
	private KakaoMapApi kakaoMapApi;

	@Value("${kakao.rest.api-key}")
	private String key;

	@Test
	public void testGetLocationByKeyword_Success() throws Exception {
		String keyword = "신라호텔";
		String jsonResponse = "{ \"documents\": [ { \"address_name\": \"서울 중구 동호로 249\", \"road_address_name\": \"서울 중구 동호로 249\" } ] }";

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(jsonResponse);

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + key);

		HttpEntity<String> entity = new HttpEntity<>(headers);
		URI uri = UriComponentsBuilder
			.fromUriString("https://dapi.kakao.com/v2/local/search/keyword.json")
			.queryParam("query", keyword)
			.build()
			.encode(StandardCharsets.UTF_8).toUri();

		when(restTemplate.exchange(uri, HttpMethod.GET, entity, JsonNode.class))
			.thenReturn(new ResponseEntity<>(jsonNode, HttpStatus.OK));

		String result = kakaoMapApi.getAddressByKeyword(keyword);

		assertEquals("서울 중구 동호로 249", result);
	}

	@Test
	public void testGetLocationByKeyword_NotFound() throws JsonProcessingException {
		String keyword = "없는호텔";
		String jsonResponse = "{ \"documents\": [] }";

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(jsonResponse);

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + key);

		HttpEntity<String> entity = new HttpEntity<>(headers);
		URI uri = UriComponentsBuilder
			.fromUriString("https://dapi.kakao.com/v2/local/search/keyword.json")
			.queryParam("query", keyword)
			.build()
			.encode(StandardCharsets.UTF_8).toUri();

		when(restTemplate.exchange(uri, HttpMethod.GET, entity, JsonNode.class))
			.thenReturn(new ResponseEntity<>(jsonNode, HttpStatus.OK));

		assertThrows(LocationNotFoundException.class, () -> {
			kakaoMapApi.getAddressByKeyword(keyword);
		});
	}
}
