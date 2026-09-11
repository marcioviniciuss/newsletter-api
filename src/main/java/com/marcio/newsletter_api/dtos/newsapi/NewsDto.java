package com.marcio.newsletter_api.dtos.newsapi;

import java.util.List;

public record NewsDto(
        String status,
        Integer totalResults,
        List<ArticleDto> articles
) {
}
