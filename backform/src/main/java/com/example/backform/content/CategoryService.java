package com.example.backform.content;

import com.example.backform.content.dto.CategoryResponse;
import com.example.backform.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CategoryService {
    private final CategoryMapper categoryMapper;
    public CategoryService(CategoryMapper categoryMapper) { this.categoryMapper = categoryMapper; }
    public List<CategoryResponse> listPublishedCategories() {
        return categoryMapper.findPublishedCategories().stream().map(this::toResponse).toList();
    }
    private CategoryResponse toResponse(Map<String, Object> row) {
        return new CategoryResponse(((Number) row.get("id")).longValue(), String.valueOf(row.get("name")), String.valueOf(row.get("slug")), String.valueOf(row.get("description")));
    }
}
