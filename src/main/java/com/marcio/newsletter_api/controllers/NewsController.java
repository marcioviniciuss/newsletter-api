package com.marcio.newsletter_api.controllers;

import com.marcio.newsletter_api.dtos.news.NewsResponseDto;
import com.marcio.newsletter_api.integrations.NewsApiClient;
import com.marcio.newsletter_api.services.NewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService){
        this.newsService = newsService;
    }

    @GetMapping
    public NewsResponseDto getNews(String category, String language) {
        return newsService.getNews(category, language);
    }
}
