package com.xavier.MangaCoBackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ClassificationRequest(

    @NotBlank(message = "El nombre de la clasificación es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    String nameClassification,

    @PositiveOrZero(message = "La edad mínima no puede ser negativa")
    Integer minAge,

    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    String description
) {}