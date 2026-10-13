package com.xavier.MangaCoBackend.dto.response;

public record ClassificationResponse(
    Long idClassification,
    String nameClassification,
    Integer minAge,
    String description,
    Boolean enabled
) {}