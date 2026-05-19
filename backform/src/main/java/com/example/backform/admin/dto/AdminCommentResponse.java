package com.example.backform.admin.dto;

public record AdminCommentResponse(
        Long id,
        Long articleId,
        String articleTitle,
        Long userId,
        String userNickname,
        String content,
        String status,
        String adminNote,
        String createdAt
) {
}
