package com.xavier.MangaCoBackend.services.impl;

import com.xavier.MangaCoBackend.dto.request.AuthorRequest;
import com.xavier.MangaCoBackend.dto.response.AuthorResponse;
import com.xavier.MangaCoBackend.entity.Author;
import com.xavier.MangaCoBackend.entity.Country;
import com.xavier.MangaCoBackend.exception.DuplicateResourceException;
import com.xavier.MangaCoBackend.exception.ResourceNotFoundException;
import com.xavier.MangaCoBackend.mapper.AuthorMapper;
import com.xavier.MangaCoBackend.repository.AuthorRepository;
import com.xavier.MangaCoBackend.repository.CountryRepository;
import com.xavier.MangaCoBackend.services.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final CountryRepository countryRepository;
    private final AuthorMapper authorMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AuthorResponse> findAll(Boolean enabled) {
        List<Author> authors;
        if (enabled == null || enabled) {
            authors = authorRepository.findAllByEnabledTrue();
        } else {
            authors = authorRepository.findAllByEnabledFalse();
        }
        return authors.stream()
                .map(authorMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorResponse> findByCountry(Long countryId) {
        return authorRepository.findAllByCountryId(countryId)
                .stream()
                .map(authorMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorResponse findById(Long id, boolean includeDisabled) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author no encontrado con id: " + id));
        if (!includeDisabled && Boolean.FALSE.equals(author.getEnabled())) {
            throw new ResourceNotFoundException("Author no encontrado con id: " + id);
        }
        return authorMapper.toResponse(author);
    }

    @Override
    @Transactional
    public AuthorResponse create(AuthorRequest request) {
        authorRepository.findByLastnameAuthorIgnoreCaseAndNameAuthorIgnoreCase(
                request.lastnameAuthor(), request.nameAuthor())
                .ifPresent(existing -> {
                    if (Boolean.TRUE.equals(existing.getEnabled())) {
                        throw new DuplicateResourceException(
                                "Ya existe un autor activo con el nombre: " + request.nameAuthor() + " " + request.lastnameAuthor());
                    } else {
                        throw new DuplicateResourceException(
                                "Ya existe un autor deshabilitado con el nombre: '" + request.nameAuthor() + " " + request.lastnameAuthor() +
                                "'. Reactívelo con PATCH /api/v1/authors/" + existing.getId() + "/enable");
                    }
                });

        Country country = resolveCountry(request.countryId());
        Author author = authorMapper.toEntity(request, country);
        return authorMapper.toResponse(authorRepository.save(author));
    }

    @Override
    @Transactional
    public AuthorResponse update(Long id, AuthorRequest request) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author no encontrado con id: " + id));

        Country country = resolveCountry(request.countryId());
        authorMapper.updateEntity(author, request, country);
        return authorMapper.toResponse(authorRepository.save(author));
    }

    @Override
    @Transactional
    public void enable(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author no encontrado con id: " + id));
        if (Boolean.TRUE.equals(author.getEnabled())) {
            throw new DuplicateResourceException("El autor ya está habilitado");
        }
        author.setEnabled(true);
        authorRepository.save(author);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author no encontrado con id: " + id));
        author.setEnabled(false);
        authorRepository.save(author);
    }

    /**
     * Resuelve la entidad Country a partir del countryId.
     * - Si countryId es null, devuelve null (autor sin país).
     * - Si no existe, lanza ResourceNotFoundException.
     */
    private Country resolveCountry(Long countryId) {
        if (countryId == null) {
            return null;
        }
        return countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException("Country no encontrado con id: " + countryId));
    }
}