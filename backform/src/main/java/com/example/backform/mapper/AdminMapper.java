package com.example.backform.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AdminMapper {
    Map<String, Object> dashboardStats();
    List<Map<String, Object>> topArticles(@Param("limit") int limit);
    List<Map<String, Object>> latestComments(@Param("limit") int limit);
    long totalViews();
    List<Map<String, Object>> interactionRanking(@Param("limit") int limit);
    int insertAdminLog(@Param("adminId") Long adminId, @Param("action") String action, @Param("targetType") String targetType, @Param("targetId") Long targetId, @Param("detail") String detail);
}
