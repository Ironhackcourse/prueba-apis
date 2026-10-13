package com.xavier.MangaCoBackend.services;

import com.xavier.MangaCoBackend.dto.request.AuthorRequest;
import com.xavier.MangaCoBackend.dto.response.AuthorResponse;

import java.util.List;

public interface AuthorService {

    List<AuthorResponse> findAll(Boolean enabled);

    List<AuthorResponse> findByCountry(Long countryId);

    AuthorResponse findById(Long id, boolean includeDisabled);

    AuthorResponse create(AuthorRequest request);

    AuthorResponse update(Long id, AuthorRequest request);

    void enable(Long id);

    void delete(Long id);
}