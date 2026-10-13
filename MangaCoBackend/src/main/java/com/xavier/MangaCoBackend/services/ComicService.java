package com.xavier.MangaCoBackend.services;

import com.xavier.MangaCoBackend.dto.request.ComicRequest;
import com.xavier.MangaCoBackend.dto.response.ComicResponse;

import java.util.List;

public interface ComicService {

    List<ComicResponse> findAll(Boolean enabled, Long seriesId, Long publisherId,
                                 Long countryId, Long formatId, String title);

    List<ComicResponse> findBySeries(Long seriesId);

    ComicResponse findById(Long id, boolean includeDisabled);

    ComicResponse create(ComicRequest request);

    ComicResponse update(Long id, ComicRequest request);

    void enable(Long id);

    void delete(Long id);
}