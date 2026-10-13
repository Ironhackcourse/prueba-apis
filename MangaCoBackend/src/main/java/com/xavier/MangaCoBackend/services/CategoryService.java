package com.xavier.MangaCoBackend.services;

import com.xavier.MangaCoBackend.dto.request.CategoryRequest;
import com.xavier.MangaCoBackend.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> findAll(Boolean enabled);

    CategoryResponse findById(Long id, boolean includeDisabled);

    CategoryResponse create(CategoryRequest request);

    CategoryResponse update(Long id, CategoryRequest request);

    void enable(Long id);

    void delete(Long id);
}