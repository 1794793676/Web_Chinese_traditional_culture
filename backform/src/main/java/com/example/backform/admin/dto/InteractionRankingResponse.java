package com.example.backform.admin.dto;

public record InteractionRankingResponse(
        Long articleId,
        String title,
        String categoryName,
        Long viewCount,
        Long likeCount,
        Long commentCount,
        Long shareCount,
        Double totalScore
) {
}
