package com.xavier.MangaCoBackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.xavier.MangaCoBackend.entity.User;

/**
 * Repositorio de la entidad User.
 * Extiende JpaRepository para obtener operaciones CRUD básicas (save, findById,
 * etc.).
 * Añadimos findByUsername para buscar un usuario por su nombre de usuario al
 * autenticar.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);
}
