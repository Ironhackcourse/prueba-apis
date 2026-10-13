package com.xavier.MangaCoBackend.dto.response;

public record CategoryResponse(
    Long idCategory,
    String nameCategory,
    String description,
    Boolean enabled
) {}