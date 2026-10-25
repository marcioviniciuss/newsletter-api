package com.marcio.newsletter_api.integrations;

import com.marcio.newsletter_api.dtos.news.newsApi.NewsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class NewsApiClient {

    private final RestClient restClient;

    @Value("${newsapi.key}")
    private String apiKey;

    public NewsApiClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public NewsDto fetchNews(String query, String language, String from, String to, int page, int pageSize) {
        return restClient.get()
                .uri("https://newsapi.org/v2/everything", uriBuilder -> uriBuilder
                        .queryParam("q", query)
                        .queryParam("language", language)
                        .queryParam("sortBy", "popularity")
                        .queryParam("from", from)
                        .queryParam("to", to)
                        .queryParam("page", page)
                        .queryParam("pageSize", pageSize)
                        .build())
                .header("X-Api-Key", apiKey)
                .retrieve()
                .body(NewsDto.class);
    }
}