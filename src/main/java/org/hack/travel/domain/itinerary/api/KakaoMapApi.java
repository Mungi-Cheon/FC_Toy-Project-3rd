package org.hack.travel.domain.itinerary.api;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.hack.travel.global.exception.LocationNotFoundException;
import org.hack.travel.global.exception.type.MessageType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class KakaoMapApi {

    private static final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/search/keyword";
    private static final Map<String, String> keyword2Address = new HashMap<>();
    private static String key;

    @Value("${kakao.rest.api-key}")
    private String tempKey;

    public static String getAddressByKeyword(String keyword) {

        if (keyword2Address.containsKey(keyword)) {
            return keyword2Address.get(keyword);
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + key);

        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        URI uri = UriComponentsBuilder
            .fromUriString(KAKAO_API_URL)
            .queryParam("query", keyword)
            .build()
            .encode(StandardCharsets.UTF_8).toUri();

        ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(uri, HttpMethod.GET,
            httpEntity, JsonNode.class);
        JsonNode jsonNode = Objects.requireNonNull(responseEntity.getBody()).get("documents");

        for (JsonNode node : jsonNode) {
            String roadAddressName = node.get("address_name").asText();

            if (roadAddressName.trim().isEmpty()) {
                continue;
            }
            String address = node.get("road_address_name").asText();
            keyword2Address.put(keyword, address);
            return address;
        }

        throw new LocationNotFoundException(MessageType.ADDRESS_NAME_NOT_FOUND, keyword);
    }

    @PostConstruct
    private void toKey() {
        key = tempKey;
    }
}
