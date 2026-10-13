package com.xavier.MangaCoBackend.repository;

import com.xavier.MangaCoBackend.entity.Series;
import com.xavier.MangaCoBackend.enums.SeriesStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Long> {

    Optional<Series> findByTitleIgnoreCase(String title);

    List<Series> findAllByEnabledTrue();

    List<Series> findAllByEnabledFalse();

    List<Series> findAllByCategoryId(Long categoryId);

    List<Series> findAllByClassificationId(Long classificationId);

    List<Series> findAllByStatus(SeriesStatus status);

    List<Series> findAllByCategoryIdAndStatus(Long categoryId, SeriesStatus status);

    boolean existsByTitleIgnoreCase(String title);
}