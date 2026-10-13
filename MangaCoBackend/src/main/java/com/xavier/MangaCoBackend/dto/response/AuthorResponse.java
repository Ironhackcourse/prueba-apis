package com.xavier.MangaCoBackend.dto.response;

import java.time.LocalDate;

public record AuthorResponse(
    Long idAuthor,
    String nameAuthor,
    String lastnameAuthor,
    String penName,
    LocalDate birthDate,
    Long countryId,
    String countryName,
    String biography,
    String photoUrl,
    Boolean enabled
) {}