package com.xavier.MangaCoBackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AuthorRequest(

    @NotBlank(message = "El nombre del autor es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    String nameAuthor,

    @NotBlank(message = "El apellido del autor es obligatorio")
    @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
    String lastnameAuthor,

    @Size(max = 150, message = "El seudónimo no puede exceder 150 caracteres")
    String penName,

    @Past(message = "La fecha de nacimiento debe estar en el pasado")
    LocalDate birthDate,

    Long countryId,

    String biography,

    @Size(max = 500, message = "La URL de la foto no puede exceder 500 caracteres")
    String photoUrl
) {}