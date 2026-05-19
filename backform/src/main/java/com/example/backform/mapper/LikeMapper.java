package com.example.backform.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LikeMapper {
    int existsLike(@Param("articleId") Long articleId, @Param("userId") Long userId);
    int insertLike(@Param("articleId") Long articleId, @Param("userId") Long userId);
    int deleteLike(@Param("articleId") Long articleId, @Param("userId") Long userId);
}
