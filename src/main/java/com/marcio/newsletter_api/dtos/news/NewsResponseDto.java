package com.marcio.newsletter_api.dtos.news;

import java.util.List;

public record NewsResponseDto(
        int page,
        int pageSize,
        boolean hasMore,
        String timeframe,
        List<ArticleResponseDto> articles
) {}