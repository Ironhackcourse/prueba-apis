package com.xavier.MangaCoBackend.mapper;

import com.xavier.MangaCoBackend.dto.request.PublisherRequest;
import com.xavier.MangaCoBackend.dto.response.PublisherResponse;
import com.xavier.MangaCoBackend.entity.Publisher;
import org.springframework.stereotype.Component;

@Component
public class PublisherMapper {

    public Publisher toEntity(PublisherRequest request) {
        return Publisher.builder()
                .namePublisher(request.namePublisher())
                .countryId(request.countryId())
                .website(request.website())
                .build();
    }

    public PublisherResponse toResponse(Publisher entity) {
        return new PublisherResponse(
                entity.getId(),
                entity.getNamePublisher(),
                entity.getCountryId(),
                entity.getWebsite(),
                entity.getEnabled()
        );
    }

    public void updateEntity(Publisher entity, PublisherRequest request) {
        entity.setNamePublisher(request.namePublisher());
        entity.setCountryId(request.countryId());
        entity.setWebsite(request.website());
    }
}