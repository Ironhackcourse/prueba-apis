package com.xavier.MangaCoBackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    String nameCategory,

    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    String description
) {}