package com.example.backform.content.dto;

public record CommentResponse(
        Long id,
        Long articleId,
        Long userId,
        String nickname,
        String username,
        String content,
        String status,
        String createdAt
) {
}
