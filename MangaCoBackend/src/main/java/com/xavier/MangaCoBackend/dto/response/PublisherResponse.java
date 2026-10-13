package com.xavier.MangaCoBackend.dto.response;

public record PublisherResponse(
    Long idPublisher,
    String namePublisher,
    Long countryId,
    String website,
    Boolean enabled
) {}