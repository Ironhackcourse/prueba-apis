package com.xavier.MangaCoBackend.services;

import com.xavier.MangaCoBackend.dto.request.GenreRequest;
import com.xavier.MangaCoBackend.dto.response.GenreResponse;

import java.util.List;

public interface GenreService {

    List<GenreResponse> findAll(Boolean enabled);

    GenreResponse findById(Long id, boolean includeDisabled);

    GenreResponse create(GenreRequest request);

    GenreResponse update(Long id, GenreRequest request);

    void enable(Long id);

    void delete(Long id);
}