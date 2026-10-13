package com.xavier.MangaCoBackend.dto.response;

public record AuthorSummary(
    Long idAuthor,
    String nameAuthor,
    String lastnameAuthor,
    String authorRole
) {}