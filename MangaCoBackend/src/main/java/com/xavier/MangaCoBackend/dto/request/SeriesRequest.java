package com.xavier.MangaCoBackend.dto.request;

import com.xavier.MangaCoBackend.enums.SeriesStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record SeriesRequest(

    @NotBlank(message = "El título de la serie es obligatorio")
    @Size(max = 250, message = "El título no puede exceder 250 caracteres")
    String title,

    @Size(max = 250, message = "El título original no puede exceder 250 caracteres")
    String originalTitle,

    String synopsis,

    Long categoryId,

    Long classificationId,

    @PositiveOrZero(message = "El total de tomos no puede ser negativo")
    Integer totalVolumes,

    SeriesStatus status,

    @Size(max = 500, message = "La URL de la portada no puede exceder 500 caracteres")
    String coverUrl,

    LocalDate startDate,

    LocalDate endDate
) {}