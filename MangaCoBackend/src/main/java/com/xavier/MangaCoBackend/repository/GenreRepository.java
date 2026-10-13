package com.xavier.MangaCoBackend.repository;

import com.xavier.MangaCoBackend.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findByNameGenreIgnoreCase(String nameGenre);

    List<Genre> findAllByEnabledTrue();

    List<Genre> findAllByEnabledFalse();

    boolean existsByNameGenreIgnoreCase(String nameGenre);
}