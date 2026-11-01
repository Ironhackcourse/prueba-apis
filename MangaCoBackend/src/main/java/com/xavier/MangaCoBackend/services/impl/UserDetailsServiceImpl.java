package com.xavier.MangaCoBackend.services.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.xavier.MangaCoBackend.repository.UserRepository;

/**
 * Implementación de UserDetailsService de Spring Security.
 *
 * Spring Security llama a loadUserByUsername() cuando necesita autenticar un
 * usuario.
 * Nosotros buscamos el usuario en la base de datos por su username y lo
 * devolvemos.
 * Como nuestra entidad User ya implementa UserDetails, podemos devolverla
 * directamente.
 *
 * Si el usuario no existe, lanzamos UsernameNotFoundException (Spring Security
 * la traduce a 401).
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }
}
