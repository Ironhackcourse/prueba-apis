package com.xavier.MangaCoBackend.services;

import com.xavier.MangaCoBackend.dto.request.SeriesRequest;
import com.xavier.MangaCoBackend.dto.response.SeriesResponse;
import com.xavier.MangaCoBackend.enums.SeriesStatus;

import java.util.List;

public interface SeriesService {

    List<SeriesResponse> findAll(Boolean enabled, SeriesStatus status, Long categoryId);

    SeriesResponse findById(Long id, boolean includeDisabled);

    SeriesResponse create(SeriesRequest request);

    SeriesResponse update(Long id, SeriesRequest request);

    void enable(Long id);

    void delete(Long id);
}