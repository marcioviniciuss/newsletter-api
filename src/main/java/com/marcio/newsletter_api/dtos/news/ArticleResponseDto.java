package com.marcio.newsletter_api.dtos.news;

public record ArticleResponseDto(
        String title,
        String description,
        String url,
        String imageUrl,
        String publishedAt,
        String sourceName,
        String category
) {}