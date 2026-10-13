package com.xavier.MangaCoBackend.mapper;

import com.xavier.MangaCoBackend.dto.request.SeriesRequest;
import com.xavier.MangaCoBackend.dto.response.SeriesResponse;
import com.xavier.MangaCoBackend.entity.Category;
import com.xavier.MangaCoBackend.entity.Classification;
import com.xavier.MangaCoBackend.entity.Series;
import com.xavier.MangaCoBackend.enums.SeriesStatus;
import org.springframework.stereotype.Component;

@Component
public class SeriesMapper {

    public Series toEntity(SeriesRequest request, Category category, Classification classification) {
        return Series.builder()
                .title(request.title())
                .originalTitle(request.originalTitle())
                .synopsis(request.synopsis())
                .category(category)
                .classification(classification)
                .totalVolumes(request.totalVolumes())
                .status(request.status() != null ? request.status() : SeriesStatus.ONGOING)
                .coverUrl(request.coverUrl())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .build();
    }

    public SeriesResponse toResponse(Series entity) {
        Category category = entity.getCategory();
        Classification classification = entity.getClassification();

        return new SeriesResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getOriginalTitle(),
                entity.getSynopsis(),
                category != null ? category.getId() : null,
                category != null ? category.getNameCategory() : null,
                classification != null ? classification.getId() : null,
                classification != null ? classification.getNameClassification() : null,
                entity.getTotalVolumes(),
                entity.getStatus(),
                entity.getCoverUrl(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getEnabled()
        );
    }

    public void updateEntity(Series entity, SeriesRequest request, Category category, Classification classification) {
        entity.setTitle(request.title());
        entity.setOriginalTitle(request.originalTitle());
        entity.setSynopsis(request.synopsis());
        entity.setCategory(category);
        entity.setClassification(classification);
        entity.setTotalVolumes(request.totalVolumes());
        if (request.status() != null) {
            entity.setStatus(request.status());
        }
        entity.setCoverUrl(request.coverUrl());
        entity.setStartDate(request.startDate());
        entity.setEndDate(request.endDate());
    }
}