package com.xavier.MangaCoBackend.services.impl;

import com.xavier.MangaCoBackend.dto.request.ClassificationRequest;
import com.xavier.MangaCoBackend.dto.response.ClassificationResponse;
import com.xavier.MangaCoBackend.entity.Classification;
import com.xavier.MangaCoBackend.exception.DuplicateResourceException;
import com.xavier.MangaCoBackend.exception.ResourceNotFoundException;
import com.xavier.MangaCoBackend.mapper.ClassificationMapper;
import com.xavier.MangaCoBackend.repository.ClassificationRepository;
import com.xavier.MangaCoBackend.services.ClassificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassificationServiceImpl implements ClassificationService {

    private final ClassificationRepository classificationRepository;
    private final ClassificationMapper classificationMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ClassificationResponse> findAll(Boolean enabled) {
        List<Classification> classifications;
        if (enabled == null || enabled) {
            classifications = classificationRepository.findAllByEnabledTrue();
        } else {
            classifications = classificationRepository.findAllByEnabledFalse();
        }
        return classifications.stream()
                .map(classificationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClassificationResponse findById(Long id, boolean includeDisabled) {
        Classification classification = classificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classification no encontrada con id: " + id));
        if (!includeDisabled && Boolean.FALSE.equals(classification.getEnabled())) {
            throw new ResourceNotFoundException("Classification no encontrada con id: " + id);
        }
        return classificationMapper.toResponse(classification);
    }

    @Override
    @Transactional
    public ClassificationResponse create(ClassificationRequest request) {
        classificationRepository.findByNameClassificationIgnoreCase(request.nameClassification())
                .ifPresent(existing -> {
                    if (Boolean.TRUE.equals(existing.getEnabled())) {
                        throw new DuplicateResourceException(
                                "Ya existe una clasificación activa con el nombre: " + request.nameClassification());
                    } else {
                        throw new DuplicateResourceException(
                                "Ya existe una clasificación deshabilitada con el nombre: '" + request.nameClassification() +
                                "'. Reactívela con PATCH /api/v1/classifications/" + existing.getId() + "/enable");
                    }
                });

        Classification classification = classificationMapper.toEntity(request);
        return classificationMapper.toResponse(classificationRepository.save(classification));
    }

    @Override
    @Transactional
    public ClassificationResponse update(Long id, ClassificationRequest request) {
        Classification classification = classificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classification no encontrada con id: " + id));
        classificationMapper.updateEntity(classification, request);
        return classificationMapper.toResponse(classificationRepository.save(classification));
    }

    @Override
    @Transactional
    public void enable(Long id) {
        Classification classification = classificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classification no encontrada con id: " + id));
        if (Boolean.TRUE.equals(classification.getEnabled())) {
            throw new DuplicateResourceException("La clasificación ya está habilitada");
        }
        classification.setEnabled(true);
        classificationRepository.save(classification);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Classification classification = classificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classification no encontrada con id: " + id));
        classification.setEnabled(false);
        classificationRepository.save(classification);
    }
}