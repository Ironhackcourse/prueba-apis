package com.xavier.MangaCoBackend.mapper;

import com.xavier.MangaCoBackend.dto.request.ClassificationRequest;
import com.xavier.MangaCoBackend.dto.response.ClassificationResponse;
import com.xavier.MangaCoBackend.entity.Classification;
import org.springframework.stereotype.Component;

@Component
public class ClassificationMapper {

    public Classification toEntity(ClassificationRequest request) {
        return Classification.builder()
                .nameClassification(request.nameClassification())
                .minAge(request.minAge())
                .description(request.description())
                .build();
    }

    public ClassificationResponse toResponse(Classification entity) {
        return new ClassificationResponse(
                entity.getId(),
                entity.getNameClassification(),
                entity.getMinAge(),
                entity.getDescription(),
                entity.getEnabled()
        );
    }

    public void updateEntity(Classification entity, ClassificationRequest request) {
        entity.setNameClassification(request.nameClassification());
        entity.setMinAge(request.minAge());
        entity.setDescription(request.description());
    }
}