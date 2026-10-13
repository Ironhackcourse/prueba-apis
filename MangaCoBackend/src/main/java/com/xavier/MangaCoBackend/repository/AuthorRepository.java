package com.xavier.MangaCoBackend.repository;

import com.xavier.MangaCoBackend.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByLastnameAuthorIgnoreCaseAndNameAuthorIgnoreCase(
            String lastnameAuthor, String nameAuthor);

    List<Author> findAllByEnabledTrue();

    List<Author> findAllByEnabledFalse();

    List<Author> findAllByCountryId(Long countryId);

    boolean existsByLastnameAuthorIgnoreCaseAndNameAuthorIgnoreCase(
            String lastnameAuthor, String nameAuthor);
}