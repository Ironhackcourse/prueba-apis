# Tutorial: Introducción a la documentación de la API

Este tutorial explica cómo está documentada la **API REST del backend MangaCo**
y cómo se genera y lee su documentación HTML.

## ¿Qué es JSDoc?

[JSDoc](https://jsdoc.app/) es un generador de documentación para JavaScript.
La documentación se escribe en bloques de comentarios `/** ... */` directamente
sobre el código, y una herramienta los procesa para producir un sitio HTML
navegable.

En este proyecto, la API está escrita en Java (Spring Boot), pero su
documentación se genera con JSDoc a partir de un archivo JavaScript
(`api-references.js`) que **se autogenera** leyendo los controladores `.java`.

## Flujo de generación

1. **`generate-api-references.js`** (script Node) lee cada controlador de
   `src/main/java/.../controller/` y extrae:
   - la ruta base (`@RequestMapping`),
   - el verbo HTTP y la ruta de cada endpoint
     (`@GetMapping`, `@PostMapping`, `@PutMapping`, `@PatchMapping`,
     `@DeleteMapping`),
   - los `@PathVariable`, `@RequestParam` y `@RequestBody`.
2. El script escribe **`api-references.js`** con un módulo JSDoc por
   controlador y una función por endpoint.
3. **JSDoc** procesa ese archivo y genera el sitio en `docs/api/`.

## Anotaciones usadas

| Anotación | Uso |
|-----------|-----|
| `@file` | Encabezado del archivo generado. |
| `@module` | Un módulo por controlador (`authorController`, `comicController`, ...). |
| `@author` | Equipo responsable: `MangaCo Team`. |
| `@since` | Fecha de incorporación. |
| `@version` | Versión de la documentación. |
| `@param` | Parámetros de ruta (`:id`), de consulta (`query`) y cuerpo (`body`). |
| `@returns` | Respuesta JSON del backend. |
| `@throws` | Condiciones de error (4xx/5xx). |
| `@async` | Los endpoints son peticiones asíncronas. |
| `@tutorial` | Enlace a este tutorial desde cada módulo. |

## Convención de rutas

- Parámetros de ruta en estilo Node: `/api/v1/authors/:id` (en Java es
  `/api/v1/authors/{id}`).
- Parámetros de consulta documentados como `[nombre]` (opcionales).
- Cuerpo de petición documentado como `body` cuando el método usa
  `@RequestBody`.

## Cómo leer la documentación

1. Abre `docs/api/index.html` en el navegador.
2. En **Modules** verás todos los controladores; pulsa uno para ver sus
   endpoints (método + ruta + parámetros).
3. En **Tutorials** encontrarás este documento.

## Cómo regenerar

```bash
cd MangaCoBackend/docs
npm run docs
```

O desde la raíz del backend:

```bash
./mvnw -Pdocs verify
```
