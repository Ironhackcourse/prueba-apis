package com.xavier.MangaCoBackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CountryRequest(
   @NotBlank (message = "Country name is required") 
   @Size (max = 100, message = "Country name must not exceed 100 characters")
   String nameCountry,
   @Size (max = 3, message = "ISO code must not exceed 3 characters")
   String isoCode
) {
    
}
