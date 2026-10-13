package com.xavier.MangaCoBackend.mapper;

import org.springframework.stereotype.Component;

import com.xavier.MangaCoBackend.dto.request.CountryRequest;
import com.xavier.MangaCoBackend.dto.response.CountryResponse;
import com.xavier.MangaCoBackend.entity.Country;

@Component 
public class CountryMapper {

    public Country toEntity(CountryRequest request) {
        return Country.builder()
                .nameCountry(request.nameCountry())
                .isoCode(request.isoCode())
                .build();
    }

    public CountryResponse toResponse(Country entity) {
        return new CountryResponse(
                entity.getId(),
                entity.getNameCountry(),
                entity.getIsoCode(),
                entity.getEnabled()
        );
    }

    public void updateEntity(Country entity, CountryRequest request) {
        entity.setNameCountry(request.nameCountry());
        entity.setIsoCode(request.isoCode());
    }
    
}
