package com.xavier.MangaCoBackend.repository;

import com.xavier.MangaCoBackend.entity.Comic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Long>, JpaSpecificationExecutor<Comic> {

    Optional<Comic> findByIsbn(String isbn);

    List<Comic> findAllByEnabledTrue();

    List<Comic> findAllBySeriesId(Long seriesId);

    Optional<Comic> findBySeriesIdAndVolume(Long seriesId, Integer volume);

    boolean existsBySeriesIdAndVolume(Long seriesId, Integer volume);

    boolean existsByIsbn(String isbn);
}