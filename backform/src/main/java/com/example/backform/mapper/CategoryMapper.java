package com.example.backform.mapper;


import java.util.List;
import java.util.Map;

public interface CategoryMapper {
    List<Map<String, Object>> findPublishedCategories();
}
