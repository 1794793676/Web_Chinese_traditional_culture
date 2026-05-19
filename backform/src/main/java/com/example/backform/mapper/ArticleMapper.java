package com.example.backform.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ArticleMapper {
    List<Map<String, Object>> findFeatured(@Param("limit") int limit);
    long countByCategorySlug(@Param("slug") String slug);
    List<Map<String, Object>> findByCategorySlug(@Param("slug") String slug, @Param("size") int size, @Param("offset") int offset);
    Map<String, Object> findDetailBySlug(@Param("slug") String slug);
    List<Map<String, Object>> findSourcesByArticleId(@Param("articleId") Long articleId);
    int incrementViewCount(@Param("articleId") Long articleId);
    long countLikes(@Param("articleId") Long articleId);
    long countComments(@Param("articleId") Long articleId);
    long countShares(@Param("articleId") Long articleId);
    long currentViewCount(@Param("articleId") Long articleId);
}
