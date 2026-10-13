package com.xavier.MangaCoBackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublisherRequest(

    @NotBlank(message = "El nombre de la editorial es obligatorio")
    @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
    String namePublisher,

    Long countryId,

    @Size(max = 255, message = "El sitio web no puede exceder 255 caracteres")
    String website
) {}