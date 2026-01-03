package com.picpick.api.naver;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NaverProductService {

    private final WebClient webClient;

    @Value("${NAVER_CLIENT_ID}")
    private String naverClientId;

    @Value("${NAVER_CLIENT_SECRET}")
    private String naverClientSecret;

    public NaverProductService(@Value("${NAVER_URL}") String naverUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(naverUrl)
                .build();
    }

    public List<NaverProductDto> naverShopSearchAPI(NaverRequestVariableDto naverVariable) {
        try {
            String searchSort = naverVariable.getSort();
            int searchDisplay = naverVariable.getDisplay() != null ? naverVariable.getDisplay() : 10;
            boolean isPriceSort = "asc".equals(searchSort) || "dsc".equals(searchSort);

            String responseBody = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/search/shop.json")
                            .queryParam("query", naverVariable.getQuery())
                            // Request more items if we are filtering/sorting in backend
                            .queryParam("display", isPriceSort ? 50 : searchDisplay)
                            .queryParam("start", naverVariable.getStart())
                            .queryParam("sort", isPriceSort ? "sim" : searchSort)
                            .build())
                    .header("X-Naver-Client-Id", naverClientId)
                    .header("X-Naver-Client-Secret", naverClientSecret)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // Synchronous block for easier logic flow

            List<NaverProductDto> dtos = fromJSONtoNaverProduct(responseBody);

            // Filtering logic
            List<String> queryTokens = Arrays.stream(naverVariable.getQuery().toLowerCase().split(" "))
                    .filter(t -> !t.isEmpty())
                    .toList();

            List<NaverProductDto> filteredList = dtos.stream()
                    .filter(item -> isHighlyRelevant(item.getProductName(), queryTokens))
                    .collect(Collectors.toList());

            // Backend Sorting
            if (isPriceSort) {
                Comparator<NaverProductDto> priceComparator = Comparator.comparingInt(NaverProductDto::getLowestPrice);
                if ("dsc".equals(searchSort)) priceComparator = priceComparator.reversed();
                filteredList.sort(priceComparator);
            }

            return filteredList.stream().limit(searchDisplay).toList();

        } catch (Exception e) {
            log.error("Naver API Error: ", e);
            return Collections.emptyList(); // Return empty rather than 500
        }
    }

    private List<NaverProductDto> fromJSONtoNaverProduct(String result) {
        JSONObject rjson = new JSONObject(result);
        if (!rjson.has("items")) return Collections.emptyList();

        JSONArray items = rjson.getJSONArray("items");
        List<NaverProductDto> list = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            list.add(new NaverProductDto(items.getJSONObject(i)));
        }
        return list;
    }

    private boolean isHighlyRelevant(String title, List<String> queryTokens) {
        if (title == null) return false;
        String cleanTitle = title.toLowerCase();
        long matchCount = queryTokens.stream()
                .filter(cleanTitle::contains)
                .count();
        return (double) matchCount / queryTokens.size() >= 0.75;
    }
}