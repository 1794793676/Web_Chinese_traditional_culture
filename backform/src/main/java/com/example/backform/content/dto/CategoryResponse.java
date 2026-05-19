package com.example.backform.content.dto;

public record CategoryResponse(
        Long id,
        String name,
        String slug,
        String description
) {
}
