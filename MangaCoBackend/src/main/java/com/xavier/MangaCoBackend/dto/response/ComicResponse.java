package com.xavier.MangaCoBackend.dto.response;

import java.time.LocalDate;
import java.util.Set;

public record ComicResponse(
    Long idComic,
    Long seriesId,
    String seriesTitle,
    String title,
    Integer volume,
    String isbn,
    Long publisherId,
    String publisherName,
    Long countryId,
    String countryName,
    Long formatId,
    String formatName,
    Integer pageCount,
    LocalDate releaseDate,
    String coverUrl,
    Boolean enabled,
    Set<GenreSummary> genres,
    Set<AuthorSummary> authors
) {}