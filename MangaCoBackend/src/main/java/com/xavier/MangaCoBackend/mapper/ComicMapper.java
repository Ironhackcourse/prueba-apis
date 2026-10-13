package com.xavier.MangaCoBackend.mapper;

import com.xavier.MangaCoBackend.dto.request.ComicRequest;
import com.xavier.MangaCoBackend.dto.response.AuthorSummary;
import com.xavier.MangaCoBackend.dto.response.ComicResponse;
import com.xavier.MangaCoBackend.dto.response.GenreSummary;
import com.xavier.MangaCoBackend.entity.*;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ComicMapper {

    public Comic toEntity(ComicRequest request, Series series, Publisher publisher,
                          Country country, Format format) {
        return Comic.builder()
                .series(series)
                .title(request.title())
                .volume(request.volume())
                .isbn(request.isbn())
                .publisher(publisher)
                .country(country)
                .format(format)
                .pageCount(request.pageCount())
                .releaseDate(request.releaseDate())
                .coverUrl(request.coverUrl())
                .build();
        // genres y comicAuthors se asignan en el service
    }

    public ComicResponse toResponse(Comic entity) {
        Series series = entity.getSeries();
        Publisher publisher = entity.getPublisher();
        Country country = entity.getCountry();
        Format format = entity.getFormat();

        Set<GenreSummary> genres = entity.getGenres().stream()
                .map(g -> new GenreSummary(g.getId(), g.getNameGenre()))
                .collect(Collectors.toSet());

        Set<AuthorSummary> authors = entity.getComicAuthors().stream()
                .map(ca -> new AuthorSummary(
                        ca.getAuthor().getId(),
                        ca.getAuthor().getNameAuthor(),
                        ca.getAuthor().getLastnameAuthor(),
                        ca.getAuthorRole()
                ))
                .collect(Collectors.toSet());

        return new ComicResponse(
                entity.getId(),
                series != null ? series.getId() : null,
                series != null ? series.getTitle() : null,
                entity.getTitle(),
                entity.getVolume(),
                entity.getIsbn(),
                publisher != null ? publisher.getId() : null,
                publisher != null ? publisher.getNamePublisher() : null,
                country != null ? country.getId() : null,
                country != null ? country.getNameCountry() : null,
                format != null ? format.getId() : null,
                format != null ? format.getNameFormat() : null,
                entity.getPageCount(),
                entity.getReleaseDate(),
                entity.getCoverUrl(),
                entity.getEnabled(),
                genres,
                authors
        );
    }

    public void updateEntity(Comic entity, ComicRequest request, Series series,
                             Publisher publisher, Country country, Format format) {
        entity.setSeries(series);
        entity.setTitle(request.title());
        entity.setVolume(request.volume());
        entity.setIsbn(request.isbn());
        entity.setPublisher(publisher);
        entity.setCountry(country);
        entity.setFormat(format);
        entity.setPageCount(request.pageCount());
        entity.setReleaseDate(request.releaseDate());
        entity.setCoverUrl(request.coverUrl());
    }
}