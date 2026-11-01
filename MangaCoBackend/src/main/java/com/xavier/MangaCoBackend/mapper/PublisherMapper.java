package com.xavier.MangaCoBackend.mapper;

import com.xavier.MangaCoBackend.dto.request.PublisherRequest;
import com.xavier.MangaCoBackend.dto.response.PublisherResponse;
import com.xavier.MangaCoBackend.entity.Country;
import com.xavier.MangaCoBackend.entity.Publisher;
import org.springframework.stereotype.Component;

@Component
public class PublisherMapper {

    public Publisher toEntity(PublisherRequest request, Country country) {
        return Publisher.builder()
                .namePublisher(request.namePublisher())
                .country(country)
                .website(request.website())
                .build();
    }

    public PublisherResponse toResponse(Publisher entity) {
        return new PublisherResponse(
                entity.getId(),
                entity.getNamePublisher(),
                entity.getCountry() != null ? entity.getCountry().getId() : null,
                entity.getWebsite(),
                entity.getEnabled()
        );
    }

    public void updateEntity(Publisher entity, PublisherRequest request, Country country) {
        entity.setNamePublisher(request.namePublisher());
        entity.setCountry(country);
        entity.setWebsite(request.website());
    }
}
