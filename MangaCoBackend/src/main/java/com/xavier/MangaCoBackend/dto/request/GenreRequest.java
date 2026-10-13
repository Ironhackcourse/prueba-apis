package com.xavier.MangaCoBackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GenreRequest(

    @NotBlank(message = "El nombre del género es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    String nameGenre
) {}