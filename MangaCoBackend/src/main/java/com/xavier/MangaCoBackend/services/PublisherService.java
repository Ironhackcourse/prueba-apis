package com.xavier.MangaCoBackend.services;

import com.xavier.MangaCoBackend.dto.request.PublisherRequest;
import com.xavier.MangaCoBackend.dto.response.PublisherResponse;

import java.util.List;

public interface PublisherService {

    List<PublisherResponse> findAll(Boolean enabled);

    List<PublisherResponse> findByCountry(Long countryId);

    PublisherResponse findById(Long id, boolean includeDisabled);

    PublisherResponse create(PublisherRequest request);

    PublisherResponse update(Long id, PublisherRequest request);

    void enable(Long id);

    void delete(Long id);
}