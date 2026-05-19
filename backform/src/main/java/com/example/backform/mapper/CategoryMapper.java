package com.example.backform.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CategoryMapper {
    List<Map<String, Object>> findPublishedCategories();
}
