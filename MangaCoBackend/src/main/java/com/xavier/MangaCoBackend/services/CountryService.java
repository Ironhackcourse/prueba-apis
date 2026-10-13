package com.xavier.MangaCoBackend.services;

import java.util.List;

import com.xavier.MangaCoBackend.dto.request.CountryRequest;
import com.xavier.MangaCoBackend.dto.response.CountryResponse;

public interface CountryService {

    List<CountryResponse> findAll(Boolean enabled);

    CountryResponse findById(Long id, boolean includeDisabled);

    CountryResponse create(CountryRequest request);

    CountryResponse update(Long id, CountryRequest request);

    void enable(Long id);

    void delete(Long id);
}
