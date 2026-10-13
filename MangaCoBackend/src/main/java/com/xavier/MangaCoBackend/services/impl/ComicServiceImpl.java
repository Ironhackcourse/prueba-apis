package com.xavier.MangaCoBackend.services.impl;

import com.xavier.MangaCoBackend.dto.request.AuthorAssignmentRequest;
import com.xavier.MangaCoBackend.dto.request.ComicRequest;
import com.xavier.MangaCoBackend.dto.response.ComicResponse;
import com.xavier.MangaCoBackend.entity.*;
import com.xavier.MangaCoBackend.exception.DuplicateResourceException;
import com.xavier.MangaCoBackend.exception.ResourceNotFoundException;
import com.xavier.MangaCoBackend.mapper.ComicMapper;
import com.xavier.MangaCoBackend.repository.*;
import com.xavier.MangaCoBackend.services.ComicService;
import com.xavier.MangaCoBackend.specification.ComicSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ComicServiceImpl implements ComicService {

    private final ComicRepository comicRepository;
    private final SeriesRepository seriesRepository;
    private final PublisherRepository publisherRepository;
    private final CountryRepository countryRepository;
    private final FormatRepository formatRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final ComicMapper comicMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ComicResponse> findAll(Boolean enabled, Long seriesId, Long publisherId,
                                       Long countryId, Long formatId, String title) {
        Specification<Comic> spec = Specification
                .where(ComicSpecification.isEnabled(enabled))
                .and(ComicSpecification.hasSeries(seriesId))
                .and(ComicSpecification.hasPublisher(publisherId))
                .and(ComicSpecification.hasCountry(countryId))
                .and(ComicSpecification.hasFormat(formatId))
                .and(ComicSpecification.titleContains(title));

        return comicRepository.findAll(spec)
                .stream()
                .map(comicMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComicResponse> findBySeries(Long seriesId) {
        return comicRepository.findAllBySeriesId(seriesId)
                .stream()
                .map(comicMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ComicResponse findById(Long id, boolean includeDisabled) {
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comic no encontrado con id: " + id));
        if (!includeDisabled && Boolean.FALSE.equals(comic.getEnabled())) {
            throw new ResourceNotFoundException("Comic no encontrado con id: " + id);
        }
        return comicMapper.toResponse(comic);
    }

    @Override
    @Transactional
    public ComicResponse create(ComicRequest request) {
        if (request.volume() != null &&
                comicRepository.existsBySeriesIdAndVolume(request.seriesId(), request.volume())) {
            throw new DuplicateResourceException(
                    "Ya existe un comic de la serie con id " + request.seriesId() +
                    " y volumen " + request.volume());
        }

        if (request.isbn() != null && comicRepository.existsByIsbn(request.isbn())) {
            throw new DuplicateResourceException("Ya existe un comic con el ISBN: " + request.isbn());
        }

        Series series = resolveSeries(request.seriesId());
        Publisher publisher = resolvePublisher(request.publisherId());
        Country country = resolveCountry(request.countryId());
        Format format = resolveFormat(request.formatId());

        Comic comic = comicMapper.toEntity(request, series, publisher, country, format);
        comic.setGenres(resolveGenres(request.genreIds()));
        addAuthorsToComic(comic, request.authors());

        return comicMapper.toResponse(comicRepository.save(comic));
    }

    @Override
    @Transactional
    public ComicResponse update(Long id, ComicRequest request) {
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comic no encontrado con id: " + id));

        if (request.volume() != null && request.seriesId() != null) {
            comicRepository.findBySeriesIdAndVolume(request.seriesId(), request.volume())
                    .ifPresent(existing -> {
                        if (!existing.getId().equals(id)) {
                            throw new DuplicateResourceException(
                                    "Ya existe otro comic con series_id=" + request.seriesId() +
                                    " y volumen=" + request.volume());
                        }
                    });
        }

        Series series = resolveSeries(request.seriesId());
        Publisher publisher = resolvePublisher(request.publisherId());
        Country country = resolveCountry(request.countryId());
        Format format = resolveFormat(request.formatId());

        comicMapper.updateEntity(comic, request, series, publisher, country, format);

        if (request.genreIds() != null) {
            comic.getGenres().clear();
            comic.getGenres().addAll(resolveGenres(request.genreIds()));
        }

        if (request.authors() != null) {
            comic.getComicAuthors().clear();
            addAuthorsToComic(comic, request.authors());
        }

        return comicMapper.toResponse(comicRepository.save(comic));
    }

    @Override
    @Transactional
    public void enable(Long id) {
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comic no encontrado con id: " + id));
        if (Boolean.TRUE.equals(comic.getEnabled())) {
            throw new DuplicateResourceException("El comic ya está habilitado");
        }
        comic.setEnabled(true);
        comicRepository.save(comic);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comic no encontrado con id: " + id));
        comic.setEnabled(false);
        comicRepository.save(comic);
    }

    // ==========================
    // Resolución de FK
    // ==========================

    private Series resolveSeries(Long seriesId) {
        return seriesRepository.findById(seriesId)
                .orElseThrow(() -> new ResourceNotFoundException("Series no encontrada con id: " + seriesId));
    }

    private Publisher resolvePublisher(Long publisherId) {
        if (publisherId == null) return null;
        return publisherRepository.findById(publisherId)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher no encontrado con id: " + publisherId));
    }

    private Country resolveCountry(Long countryId) {
        if (countryId == null) return null;
        return countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException("Country no encontrado con id: " + countryId));
    }

    private Format resolveFormat(Long formatId) {
        if (formatId == null) return null;
        return formatRepository.findById(formatId)
                .orElseThrow(() -> new ResourceNotFoundException("Format no encontrado con id: " + formatId));
    }

    private Set<Genre> resolveGenres(Set<Long> genreIds) {
        if (genreIds == null || genreIds.isEmpty()) {
            return new HashSet<>();
        }
        Set<Genre> genres = new HashSet<>();
        for (Long genreId : genreIds) {
            Genre genre = genreRepository.findById(genreId)
                    .orElseThrow(() -> new ResourceNotFoundException("Genre no encontrado con id: " + genreId));
            genres.add(genre);
        }
        return genres;
    }

    private void addAuthorsToComic(Comic comic, List<AuthorAssignmentRequest> authors) {
        if (authors == null || authors.isEmpty()) {
            return;
        }

        Set<Long> processedAuthorIds = new HashSet<>();

        for (AuthorAssignmentRequest a : authors) {
            if (!processedAuthorIds.add(a.authorId())) {
                throw new DuplicateResourceException(
                        "El autor con id " + a.authorId() + " está repetido en este comic");
            }

            Author author = authorRepository.findById(a.authorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Author no encontrado con id: " + a.authorId()));

            ComicAuthor comicAuthor = ComicAuthor.builder()
                    .comic(comic)
                    .author(author)
                    .authorRole(a.authorRole() != null && !a.authorRole().isBlank()
                                ? a.authorRole()
                                : "writer")
                    .build();

            comic.getComicAuthors().add(comicAuthor);
        }
    }
}