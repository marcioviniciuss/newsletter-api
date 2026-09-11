package com.marcio.newsletter_api.integrations;

import com.marcio.newsletter_api.dtos.newsapi.NewsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;

@Service
public class NewsApiClient {
    private final RestClient restClient;
    String ontem = LocalDate.now().minusDays(1).toString();

    @Value("${newsapi.key}")
    private String apiKey;

    public NewsApiClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public NewsDto getNews(String category) {
        return restClient.get()
                .uri("https://newsapi.org/v2/everything", uriBuilder -> uriBuilder
                        .queryParam("q", category)
                        .queryParam("language", "pt")
                        .queryParam("sortBy", "popularity")
                        .queryParam("pageSize", 5)
                        .queryParam("from", ontem)
                        .build())
                .header("X-Api-Key", apiKey)
                .retrieve()
                .body(NewsDto.class);
    }
}
