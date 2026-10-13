package com.xavier.MangaCoBackend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record ComicRequest(

    @NotNull(message = "El ID de la serie es obligatorio")
    Long seriesId,

    @Size(max = 250, message = "El título no puede exceder 250 caracteres")
    String title,

    @Positive(message = "El número de volumen debe ser positivo")
    Integer volume,

    @Size(max = 20, message = "El ISBN no puede exceder 20 caracteres")
    String isbn,

    Long publisherId,

    Long countryId,

    Long formatId,

    @Positive(message = "El número de páginas debe ser positivo")
    Integer pageCount,

    LocalDate releaseDate,

    @Size(max = 500, message = "La URL de la portada no puede exceder 500 caracteres")
    String coverUrl,

    Set<Long> genreIds,

    List<AuthorAssignmentRequest> authors
) {}