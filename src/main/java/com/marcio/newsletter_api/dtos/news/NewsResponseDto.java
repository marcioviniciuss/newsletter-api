package com.marcio.newsletter_api.dtos.news;

public record NewsResponseDto (
    String title,
    String description,
    String url,
    String urlToImage,
    String publishedAt,
    String sourceName,
    String author,
    String category
){}
