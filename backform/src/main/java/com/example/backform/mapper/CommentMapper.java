package com.example.backform.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommentMapper {
    long countVisibleByArticleId(@Param("articleId") Long articleId);
    List<Map<String, Object>> findVisibleByArticleId(@Param("articleId") Long articleId, @Param("size") int size, @Param("offset") int offset);
    int insertComment(@Param("articleId") Long articleId, @Param("userId") Long userId, @Param("content") String content, @Param("status") String status);
    Map<String, Object> findById(@Param("id") Long id);
    long countAdminComments(@Param("status") String status);
    List<Map<String, Object>> findAdminComments(@Param("status") String status, @Param("size") int size, @Param("offset") int offset);
    int updateStatus(@Param("id") Long id, @Param("status") String status, @Param("adminNote") String adminNote);
}
