package com.example.backform.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ViewMapper {
    int insertView(@Param("articleId") Long articleId, @Param("userId") Long userId, @Param("pagePath") String pagePath, @Param("ipAddress") String ipAddress, @Param("userAgent") String userAgent);
}
