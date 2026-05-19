package com.example.backform.admin.dto;

import java.util.List;

public record DashboardResponse(
        Long userCount,
        Long articleCount,
        Long likeCount,
        Long commentCount,
        Long shareCount,
        Long viewCount,
        List<InteractionRankingResponse> popularArticles,
        List<AdminCommentResponse> latestComments
) {
}
