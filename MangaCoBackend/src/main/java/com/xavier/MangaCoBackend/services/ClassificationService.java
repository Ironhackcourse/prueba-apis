package com.xavier.MangaCoBackend.services;

import com.xavier.MangaCoBackend.dto.request.ClassificationRequest;
import com.xavier.MangaCoBackend.dto.response.ClassificationResponse;

import java.util.List;

public interface ClassificationService {

    List<ClassificationResponse> findAll(Boolean enabled);

    ClassificationResponse findById(Long id, boolean includeDisabled);

    ClassificationResponse create(ClassificationRequest request);

    ClassificationResponse update(Long id, ClassificationRequest request);

    void enable(Long id);

    void delete(Long id);
}