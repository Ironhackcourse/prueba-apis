package com.xavier.MangaCoBackend.repository;

import com.xavier.MangaCoBackend.entity.ComicAuthor;
import com.xavier.MangaCoBackend.entity.ComicAuthorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComicAuthorRepository extends JpaRepository<ComicAuthor, ComicAuthorId> {

    List<ComicAuthor> findAllByComicId(Long comicId);

    List<ComicAuthor> findAllByAuthorId(Long authorId);

    boolean existsByComicIdAndAuthorId(Long comicId, Long authorId);
}