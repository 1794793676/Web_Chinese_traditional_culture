package com.example.backform.content.dto;
public record InteractionCountResponse(Long articleId,Long likeCount,Long commentCount,Long shareCount,Long viewCount,Boolean likedByCurrentUser){}
