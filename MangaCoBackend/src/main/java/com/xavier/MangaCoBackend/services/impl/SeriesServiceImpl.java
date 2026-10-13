package com.xavier.MangaCoBackend.services.impl;

import com.xavier.MangaCoBackend.dto.request.SeriesRequest;
import com.xavier.MangaCoBackend.dto.response.SeriesResponse;
import com.xavier.MangaCoBackend.entity.Category;
import com.xavier.MangaCoBackend.entity.Classification;
import com.xavier.MangaCoBackend.entity.Series;
import com.xavier.MangaCoBackend.enums.SeriesStatus;
import com.xavier.MangaCoBackend.exception.DuplicateResourceException;
import com.xavier.MangaCoBackend.exception.ResourceNotFoundException;
import com.xavier.MangaCoBackend.mapper.SeriesMapper;
import com.xavier.MangaCoBackend.repository.CategoryRepository;
import com.xavier.MangaCoBackend.repository.ClassificationRepository;
import com.xavier.MangaCoBackend.repository.SeriesRepository;
import com.xavier.MangaCoBackend.services.SeriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeriesServiceImpl implements SeriesService {

    private final SeriesRepository seriesRepository;
    private final CategoryRepository categoryRepository;
    private final ClassificationRepository classificationRepository;
    private final SeriesMapper seriesMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SeriesResponse> findAll(Boolean enabled, SeriesStatus status, Long categoryId) {
        List<Series> series;

        // Combinamos filtros. De momento, con if/else simple. Si crecen mucho, se pasa a Specification.
        if (categoryId != null && status != null) {
            series = seriesRepository.findAllByCategoryIdAndStatus(categoryId, status);
        } else if (categoryId != null) {
            series = seriesRepository.findAllByCategoryId(categoryId);
        } else if (status != null) {
            series = seriesRepository.findAllByStatus(status);
        } else if (enabled != null && !enabled) {
            series = seriesRepository.findAllByEnabledFalse();
        } else {
            series = seriesRepository.findAllByEnabledTrue();
        }

        // Filtro final por enabled si viene explícito (activos por defecto)
        if (enabled != null && !enabled) {
            return series.stream()
                    .filter(s -> Boolean.FALSE.equals(s.getEnabled()))
                    .map(seriesMapper::toResponse)
                    .toList();
        }

        return series.stream()
                .filter(s -> Boolean.TRUE.equals(s.getEnabled()))
                .map(seriesMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SeriesResponse findById(Long id, boolean includeDisabled) {
        Series series = seriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Series no encontrada con id: " + id));
        if (!includeDisabled && Boolean.FALSE.equals(series.getEnabled())) {
            throw new ResourceNotFoundException("Series no encontrada con id: " + id);
        }
        return seriesMapper.toResponse(series);
    }

    @Override
    @Transactional
    public SeriesResponse create(SeriesRequest request) {
        seriesRepository.findByTitleIgnoreCase(request.title())
                .ifPresent(existing -> {
                    if (Boolean.TRUE.equals(existing.getEnabled())) {
                        throw new DuplicateResourceException(
                                "Ya existe una serie activa con el título: " + request.title());
                    } else {
                        throw new DuplicateResourceException(
                                "Ya existe una serie deshabilitada con el título: '" + request.title() +
                                "'. Reactívela con PATCH /api/v1/series/" + existing.getId() + "/enable");
                    }
                });

        Category category = resolveCategory(request.categoryId());
        Classification classification = resolveClassification(request.classificationId());

        Series series = seriesMapper.toEntity(request, category, classification);
        return seriesMapper.toResponse(seriesRepository.save(series));
    }

    @Override
    @Transactional
    public SeriesResponse update(Long id, SeriesRequest request) {
        Series series = seriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Series no encontrada con id: " + id));

        Category category = resolveCategory(request.categoryId());
        Classification classification = resolveClassification(request.classificationId());

        seriesMapper.updateEntity(series, request, category, classification);
        return seriesMapper.toResponse(seriesRepository.save(series));
    }

    @Override
    @Transactional
    public void enable(Long id) {
        Series series = seriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Series no encontrada con id: " + id));
        if (Boolean.TRUE.equals(series.getEnabled())) {
            throw new DuplicateResourceException("La serie ya está habilitada");
        }
        series.setEnabled(true);
        seriesRepository.save(series);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Series series = seriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Series no encontrada con id: " + id));
        series.setEnabled(false);
        seriesRepository.save(series);
    }

    private Category resolveCategory(Long categoryId) {
        if (categoryId == null) return null;
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category no encontrada con id: " + categoryId));
    }

    private Classification resolveClassification(Long classificationId) {
        if (classificationId == null) return null;
        return classificationRepository.findById(classificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Classification no encontrada con id: " + classificationId));
    }
}