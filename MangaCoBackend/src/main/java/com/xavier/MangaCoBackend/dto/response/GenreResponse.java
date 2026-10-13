package com.xavier.MangaCoBackend.dto.response;

public record GenreResponse(
    Long idGenre,
    String nameGenre,
    Boolean enabled
) {}