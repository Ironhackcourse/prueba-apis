package com.xavier.MangaCoBackend.services.impl;

import com.xavier.MangaCoBackend.dto.request.GenreRequest;
import com.xavier.MangaCoBackend.dto.response.GenreResponse;
import com.xavier.MangaCoBackend.entity.Genre;
import com.xavier.MangaCoBackend.exception.DuplicateResourceException;
import com.xavier.MangaCoBackend.exception.ResourceNotFoundException;
import com.xavier.MangaCoBackend.mapper.GenreMapper;
import com.xavier.MangaCoBackend.repository.GenreRepository;
import com.xavier.MangaCoBackend.services.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    @Transactional(readOnly = true)
    public List<GenreResponse> findAll(Boolean enabled) {
        List<Genre> genres;
        if (enabled == null || enabled) {
            genres = genreRepository.findAllByEnabledTrue();
        } else {
            genres = genreRepository.findAllByEnabledFalse();
        }
        return genres.stream()
                .map(genreMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GenreResponse findById(Long id, boolean includeDisabled) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre no encontrado con id: " + id));
        if (!includeDisabled && Boolean.FALSE.equals(genre.getEnabled())) {
            throw new ResourceNotFoundException("Genre no encontrado con id: " + id);
        }
        return genreMapper.toResponse(genre);
    }

    @Override
    @Transactional
    public GenreResponse create(GenreRequest request) {
        genreRepository.findByNameGenreIgnoreCase(request.nameGenre())
                .ifPresent(existing -> {
                    if (Boolean.TRUE.equals(existing.getEnabled())) {
                        throw new DuplicateResourceException(
                                "Ya existe un género activo con el nombre: " + request.nameGenre());
                    } else {
                        throw new DuplicateResourceException(
                                "Ya existe un género deshabilitado con el nombre: '" + request.nameGenre() +
                                "'. Reactívelo con PATCH /api/v1/genres/" + existing.getId() + "/enable");
                    }
                });

        Genre genre = genreMapper.toEntity(request);
        return genreMapper.toResponse(genreRepository.save(genre));
    }

    @Override
    @Transactional
    public GenreResponse update(Long id, GenreRequest request) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre no encontrado con id: " + id));
        genreMapper.updateEntity(genre, request);
        return genreMapper.toResponse(genreRepository.save(genre));
    }

    @Override
    @Transactional
    public void enable(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre no encontrado con id: " + id));
        if (Boolean.TRUE.equals(genre.getEnabled())) {
            throw new DuplicateResourceException("El género ya está habilitado");
        }
        genre.setEnabled(true);
        genreRepository.save(genre);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre no encontrado con id: " + id));
        genre.setEnabled(false);
        genreRepository.save(genre);
    }
}