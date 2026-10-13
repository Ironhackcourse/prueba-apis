package com.xavier.MangaCoBackend.mapper;

import com.xavier.MangaCoBackend.dto.request.FormatRequest;
import com.xavier.MangaCoBackend.dto.response.FormatResponse;
import com.xavier.MangaCoBackend.entity.Format;
import org.springframework.stereotype.Component;

@Component
public class FormatMapper {

    public Format toEntity(FormatRequest request) {
        return Format.builder()
                .nameFormat(request.nameFormat())
                .build();
    }

    public FormatResponse toResponse(Format entity) {
        return new FormatResponse(
                entity.getId(),
                entity.getNameFormat(),
                entity.getEnabled()
        );
    }

    public void updateEntity(Format entity, FormatRequest request) {
        entity.setNameFormat(request.nameFormat());
    }
}