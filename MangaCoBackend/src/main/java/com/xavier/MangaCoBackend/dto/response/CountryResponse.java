package com.xavier.MangaCoBackend.dto.response;

public record CountryResponse(
    Long idCountry,
    String nameCountry,
    String isoCode,
    Boolean enabled
) {
    
}
