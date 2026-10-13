package com.xavier.MangaCoBackend.dto.response;

public record FormatResponse(
    Long idFormat,
    String nameFormat,
    Boolean enabled
) {}