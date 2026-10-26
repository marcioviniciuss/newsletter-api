package com.marcio.newsletter_api.controllers;

import com.marcio.newsletter_api.dtos.news.NewsResponseDto;
import com.marcio.newsletter_api.services.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService){
        this.newsService = newsService;
    }

    @GetMapping
    @CrossOrigin(origins = "*")
    public ResponseEntity<NewsResponseDto> getNews(@RequestParam(defaultValue = "all") String category,
                                  @RequestParam(defaultValue = "pt") String language,
                                  @RequestParam(defaultValue = "week") String timeframe,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "15") int pageSize) {

        return ResponseEntity.status(HttpStatus.OK).body(newsService.getNewsForPortal(category, language, timeframe, page, pageSize));
    }
}
