package com.xavier.MangaCoBackend.mapper;

import com.xavier.MangaCoBackend.dto.request.GenreRequest;
import com.xavier.MangaCoBackend.dto.response.GenreResponse;
import com.xavier.MangaCoBackend.entity.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {

    public Genre toEntity(GenreRequest request) {
        return Genre.builder()
                .nameGenre(request.nameGenre())
                .build();
    }

    public GenreResponse toResponse(Genre entity) {
        return new GenreResponse(
                entity.getId(),
                entity.getNameGenre(),
                entity.getEnabled()
        );
    }

    public void updateEntity(Genre entity, GenreRequest request) {
        entity.setNameGenre(request.nameGenre());
    }
}