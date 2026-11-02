package com.xavier.MangaCoBackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración para servir archivos estáticos de la documentación JSDoc.
 *
 * Sirve el contenido de {@code docs/api/} bajo la URL {@code /docs/api/**},
 * permitiendo acceder a la documentación desplegada en Render.
 *
 * Ejemplo:
 *   https://prueba-apis-w2fq.onrender.com/docs/api/index.html
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Registra el manejador de recursos estáticos para la documentación API.
     *
     * Los archivos HTML, CSS, JS, fuentes, etc. de JSDoc están en
     * {@code MangaCoBackend/docs/api/} y se sirven bajo la ruta {@code /docs/api/**}.
     *
     * Se prueban varias ubicaciones porque el base directory de Render puede ser
     * la raíz del repositorio o el módulo {@code MangaCoBackend/}.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/docs/api/**")
                .addResourceLocations(
                        "file:./docs/api/",
                        "file:./MangaCoBackend/docs/api/"
                );
    }
}
