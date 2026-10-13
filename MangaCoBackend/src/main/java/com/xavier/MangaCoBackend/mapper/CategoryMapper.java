package com.xavier.MangaCoBackend.mapper;

import com.xavier.MangaCoBackend.dto.request.CategoryRequest;
import com.xavier.MangaCoBackend.dto.response.CategoryResponse;
import com.xavier.MangaCoBackend.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest request) {
        return Category.builder()
                .nameCategory(request.nameCategory())
                .description(request.description())
                .build();
    }

    public CategoryResponse toResponse(Category entity) {
        return new CategoryResponse(
                entity.getId(),
                entity.getNameCategory(),
                entity.getDescription(),
                entity.getEnabled()
        );
    }

    public void updateEntity(Category entity, CategoryRequest request) {
        entity.setNameCategory(request.nameCategory());
        entity.setDescription(request.description());
    }
}