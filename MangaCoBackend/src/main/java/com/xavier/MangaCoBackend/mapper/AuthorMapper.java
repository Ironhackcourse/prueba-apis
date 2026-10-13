package com.xavier.MangaCoBackend.mapper;

import com.xavier.MangaCoBackend.dto.request.AuthorRequest;
import com.xavier.MangaCoBackend.dto.response.AuthorResponse;
import com.xavier.MangaCoBackend.entity.Author;
import com.xavier.MangaCoBackend.entity.Country;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

    public Author toEntity(AuthorRequest request, Country country) {
        return Author.builder()
                .nameAuthor(request.nameAuthor())
                .lastnameAuthor(request.lastnameAuthor())
                .penName(request.penName())
                .birthDate(request.birthDate())
                .country(country)
                .biography(request.biography())
                .photoUrl(request.photoUrl())
                .build();
    }

    public AuthorResponse toResponse(Author entity) {
        Country country = entity.getCountry();
        return new AuthorResponse(
                entity.getId(),
                entity.getNameAuthor(),
                entity.getLastnameAuthor(),
                entity.getPenName(),
                entity.getBirthDate(),
                country != null ? country.getId() : null,
                country != null ? country.getNameCountry() : null,
                entity.getBiography(),
                entity.getPhotoUrl(),
                entity.getEnabled()
        );
    }

    public void updateEntity(Author entity, AuthorRequest request, Country country) {
        entity.setNameAuthor(request.nameAuthor());
        entity.setLastnameAuthor(request.lastnameAuthor());
        entity.setPenName(request.penName());
        entity.setBirthDate(request.birthDate());
        entity.setCountry(country);
        entity.setBiography(request.biography());
        entity.setPhotoUrl(request.photoUrl());
    }
}