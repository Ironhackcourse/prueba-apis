package com.xavier.MangaCoBackend.services.impl;

import com.xavier.MangaCoBackend.dto.request.FormatRequest;
import com.xavier.MangaCoBackend.dto.response.FormatResponse;
import com.xavier.MangaCoBackend.entity.Format;
import com.xavier.MangaCoBackend.exception.DuplicateResourceException;
import com.xavier.MangaCoBackend.exception.ResourceNotFoundException;
import com.xavier.MangaCoBackend.mapper.FormatMapper;
import com.xavier.MangaCoBackend.repository.FormatRepository;
import com.xavier.MangaCoBackend.services.FormatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormatServiceImpl implements FormatService {

    private final FormatRepository formatRepository;
    private final FormatMapper formatMapper;

    @Override
    @Transactional(readOnly = true)
    public List<FormatResponse> findAll(Boolean enabled) {
        List<Format> formats;
        if (enabled == null || enabled) {
            formats = formatRepository.findAllByEnabledTrue();
        } else {
            formats = formatRepository.findAllByEnabledFalse();
        }
        return formats.stream()
                .map(formatMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FormatResponse findById(Long id, boolean includeDisabled) {
        Format format = formatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Format no encontrado con id: " + id));
        if (!includeDisabled && Boolean.FALSE.equals(format.getEnabled())) {
            throw new ResourceNotFoundException("Format no encontrado con id: " + id);
        }
        return formatMapper.toResponse(format);
    }

    @Override
    @Transactional
    public FormatResponse create(FormatRequest request) {
        formatRepository.findByNameFormatIgnoreCase(request.nameFormat())
                .ifPresent(existing -> {
                    if (Boolean.TRUE.equals(existing.getEnabled())) {
                        throw new DuplicateResourceException(
                                "Ya existe un formato activo con el nombre: " + request.nameFormat());
                    } else {
                        throw new DuplicateResourceException(
                                "Ya existe un formato deshabilitado con el nombre: '" + request.nameFormat() +
                                "'. Reactívelo con PATCH /api/v1/formats/" + existing.getId() + "/enable");
                    }
                });

        Format format = formatMapper.toEntity(request);
        return formatMapper.toResponse(formatRepository.save(format));
    }

    @Override
    @Transactional
    public FormatResponse update(Long id, FormatRequest request) {
        Format format = formatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Format no encontrado con id: " + id));
        formatMapper.updateEntity(format, request);
        return formatMapper.toResponse(formatRepository.save(format));
    }

    @Override
    @Transactional
    public void enable(Long id) {
        Format format = formatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Format no encontrado con id: " + id));
        if (Boolean.TRUE.equals(format.getEnabled())) {
            throw new DuplicateResourceException("El formato ya está habilitado");
        }
        format.setEnabled(true);
        formatRepository.save(format);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Format format = formatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Format no encontrado con id: " + id));
        format.setEnabled(false);
        formatRepository.save(format);
    }
}