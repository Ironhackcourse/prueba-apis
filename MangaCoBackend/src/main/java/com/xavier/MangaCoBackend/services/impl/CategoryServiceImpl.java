package com.xavier.MangaCoBackend.services.impl;

import com.xavier.MangaCoBackend.dto.request.CategoryRequest;
import com.xavier.MangaCoBackend.dto.response.CategoryResponse;
import com.xavier.MangaCoBackend.entity.Category;
import com.xavier.MangaCoBackend.exception.DuplicateResourceException;
import com.xavier.MangaCoBackend.exception.ResourceNotFoundException;
import com.xavier.MangaCoBackend.mapper.CategoryMapper;
import com.xavier.MangaCoBackend.repository.CategoryRepository;
import com.xavier.MangaCoBackend.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll(Boolean enabled) {
        List<Category> categories;
        if (enabled == null || enabled) {
            categories = categoryRepository.findAllByEnabledTrue();
        } else {
            categories = categoryRepository.findAllByEnabledFalse();
        }
        return categories.stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id, boolean includeDisabled) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category no encontrada con id: " + id));
        if (!includeDisabled && Boolean.FALSE.equals(category.getEnabled())) {
            throw new ResourceNotFoundException("Category no encontrada con id: " + id);
        }
        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        categoryRepository.findByNameCategoryIgnoreCase(request.nameCategory())
                .ifPresent(existing -> {
                    if (Boolean.TRUE.equals(existing.getEnabled())) {
                        throw new DuplicateResourceException(
                                "Ya existe una categoría activa con el nombre: " + request.nameCategory());
                    } else {
                        throw new DuplicateResourceException(
                                "Ya existe una categoría deshabilitada con el nombre: '" + request.nameCategory() +
                                "'. Reactívela con PATCH /api/v1/categories/" + existing.getId() + "/enable");
                    }
                });

        Category category = categoryMapper.toEntity(request);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category no encontrada con id: " + id));
        categoryMapper.updateEntity(category, request);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void enable(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category no encontrada con id: " + id));
        if (Boolean.TRUE.equals(category.getEnabled())) {
            throw new DuplicateResourceException("La categoría ya está habilitada");
        }
        category.setEnabled(true);
        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category no encontrada con id: " + id));
        category.setEnabled(false);
        categoryRepository.save(category);
    }
}