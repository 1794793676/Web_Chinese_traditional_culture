package com.example.backform.content.dto;
public record ArticleCardResponse(Long id,String slug,String title,String summary,String coverUrl,String categoryName,Long likeCount,Long commentCount,Long shareCount,Long viewCount,String publishedAt){}
