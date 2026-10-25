package com.marcio.newsletter_api.dtos.news.newsApi;

public record ArticleDto(
        SourceDto source,
        String author,
        String title,
        String description,
        String url,
        String urlToImage,
        String publishedAt,
        String content
) {
}
