package com.xavier.MangaCoBackend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AuthorAssignmentRequest(

    @NotNull(message = "El ID del autor es obligatorio")
    Long authorId,

    @Size(max = 30, message = "El rol del autor no puede exceder 30 caracteres")
    String authorRole
) {}