package com.example.backform.content.dto;

import java.util.List;

public record ArticleDetailResponse(
        Long id,
        String slug,
        String title,
        String summary,
        String content,
        String coverUrl,
        String categoryName,
        String categorySlug,
        Long likeCount,
        Long commentCount,
        Long shareCount,
        Long viewCount,
        boolean likedByCurrentUser,
        List<ArticleSourceResponse> sources
) {
}
