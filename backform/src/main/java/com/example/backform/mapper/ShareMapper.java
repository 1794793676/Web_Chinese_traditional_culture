package com.example.backform.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShareMapper {
    int insertShare(@Param("articleId") Long articleId, @Param("userId") Long userId, @Param("channel") String channel);
}
