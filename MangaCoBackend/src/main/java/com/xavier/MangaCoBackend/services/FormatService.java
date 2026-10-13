package com.xavier.MangaCoBackend.services;

import com.xavier.MangaCoBackend.dto.request.FormatRequest;
import com.xavier.MangaCoBackend.dto.response.FormatResponse;

import java.util.List;

public interface FormatService {

    List<FormatResponse> findAll(Boolean enabled);

    FormatResponse findById(Long id, boolean includeDisabled);

    FormatResponse create(FormatRequest request);

    FormatResponse update(Long id, FormatRequest request);

    void enable(Long id);

    void delete(Long id);
}