package com.example.backform.content.dto;

public record ArticleSourceResponse(
        Long id,
        Long articleId,
        String sourceTitle,
        String sourceUrl,
        String sourceType
) {
}
