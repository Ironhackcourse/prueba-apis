package com.xavier.MangaCoBackend.dto.response;

import com.xavier.MangaCoBackend.enums.SeriesStatus;

import java.time.LocalDate;

public record SeriesResponse(
    Long idSeries,
    String title,
    String originalTitle,
    String synopsis,
    Long categoryId,
    String categoryName,
    Long classificationId,
    String classificationName,
    Integer totalVolumes,
    SeriesStatus status,
    String coverUrl,
    LocalDate startDate,
    LocalDate endDate,
    Boolean enabled
) {}