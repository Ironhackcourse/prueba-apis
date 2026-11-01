package com.xavier.MangaCoBackend.services.impl;

import com.xavier.MangaCoBackend.dto.request.PublisherRequest;
import com.xavier.MangaCoBackend.dto.response.PublisherResponse;
import com.xavier.MangaCoBackend.entity.Publisher;
import com.xavier.MangaCoBackend.exception.DuplicateResourceException;
import com.xavier.MangaCoBackend.exception.ResourceNotFoundException;
import com.xavier.MangaCoBackend.mapper.PublisherMapper;
import com.xavier.MangaCoBackend.entity.Country;
import com.xavier.MangaCoBackend.repository.CountryRepository;
import com.xavier.MangaCoBackend.repository.PublisherRepository;
import com.xavier.MangaCoBackend.services.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;
    private final CountryRepository countryRepository;
    private final PublisherMapper publisherMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PublisherResponse> findAll(Boolean enabled) {
        List<Publisher> publishers;
        if (enabled == null || enabled) {
            publishers = publisherRepository.findAllByEnabledTrue();
        } else {
            publishers = publisherRepository.findAllByEnabledFalse();
        }
        return publishers.stream()
                .map(publisherMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublisherResponse> findByCountry(Long countryId) {
        return publisherRepository.findByCountry_Id(countryId)
                .stream()
                .map(publisherMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PublisherResponse findById(Long id, boolean includeDisabled) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher no encontrado con id: " + id));
        if (!includeDisabled && Boolean.FALSE.equals(publisher.getEnabled())) {
            throw new ResourceNotFoundException("Publisher no encontrado con id: " + id);
        }
        return publisherMapper.toResponse(publisher);
    }

    private Country resolveCountry(Long countryId) {
        return countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "País no encontrado con id: " + countryId));
    }

    @Override
    @Transactional
    public PublisherResponse create(PublisherRequest request) {
        publisherRepository.findByNamePublisherIgnoreCase(request.namePublisher())
                .ifPresent(existing -> {
                    if (Boolean.TRUE.equals(existing.getEnabled())) {
                        throw new DuplicateResourceException(
                                "Ya existe una editorial activa con el nombre: " + request.namePublisher());
                    } else {
                        throw new DuplicateResourceException(
                                "Ya existe una editorial deshabilitada con el nombre: '" + request.namePublisher() +
                                        "'. Reactívela con PATCH /api/v1/publishers/" + existing.getId() + "/enable");
                    }
                });

        Country country = resolveCountry(request.countryId());
        Publisher publisher = publisherMapper.toEntity(request, country);
        return publisherMapper.toResponse(publisherRepository.save(publisher));
    }

    @Override
    @Transactional
    public PublisherResponse update(Long id, PublisherRequest request) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher no encontrado con id: " + id));
        Country country = resolveCountry(request.countryId());
        publisherMapper.updateEntity(publisher, request, country);
        return publisherMapper.toResponse(publisherRepository.save(publisher));
    }

    @Override
    @Transactional
    public void enable(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher no encontrado con id: " + id));
        if (Boolean.TRUE.equals(publisher.getEnabled())) {
            throw new DuplicateResourceException("La editorial ya está habilitada");
        }
        publisher.setEnabled(true);
        publisherRepository.save(publisher);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher no encontrado con id: " + id));
        publisher.setEnabled(false);
        publisherRepository.save(publisher);
    }
}