# Documentación de la API REST — MangaCo Backend

Documentación oficial de los endpoints del backend **MangaCo** (Spring Boot),
generada con **JavaScript + Node.js + JSDoc**.

Este directorio reemplaza a la documentación Javadoc que se generaba en
`target/site/apidocs/`.

## ¿Cómo funciona?

1. `generate-api-references.js` lee los controladores REST de Spring Boot
   (`src/main/java/com/xavier/MangaCoBackend/controller/*.java`), extrae la
   ruta base de cada `@RequestMapping`, el verbo HTTP y la ruta de cada
   endpoint, y escribe `api-references.js` con bloques JSDoc.
2. JSDoc procesa `api-references.js` y genera el sitio HTML en `docs/api/`.

La lista de endpoints se deriva del **código Java real**, por lo que la
documentación no se desincroniza al añadir o modificar controladores.

## Generación

```bash
cd MangaCoBackend/docs
npm install          # instala JSDoc (solo la primera vez)
npm run docs         # regenera api-references.js y el sitio HTML
```

O bien desde la raíz del backend, con el perfil Maven `docs`:

```bash
cd MangaCoBackend
./mvnw -Pdocs verify
```

La salida HTML queda en `MangaCoBackend/docs/api/index.html`.

## Estructura

```
MangaCoBackend/docs/
├── generate-api-references.js   # Script Node que lee los controllers .java
├── api-references.js            # Referencias JSDoc autogeneradas (no editar)
├── jsdoc.json                   # Configuración de JSDoc
├── package.json                 # Tooling Node (JSDoc)
├── README.md                    # Este archivo (portada de la doc)
├── tutorials/
│   └── introduccion.markdown    # Tutorial introductorio (@tutorial)
└── api/                         # Sitio HTML generado (ignorado por git)
```

## Recursos de la API

Todos los endpoints cuelgan de `/api/v1`. La convención de rutas es:

| Recurso | Ruta base | Filtros especiales |
|---------|-----------|--------------------|
| Autores | `/api/v1/authors` | `GET /country/{countryId}` |
| Cómics | `/api/v1/comics` | `GET /series/{seriesId}` |
| Series | `/api/v1/series` | — |
| Editoriales | `/api/v1/publishers` | `GET /country/{countryId}` |
| Categorías | `/api/v1/categories` | — |
| Clasificaciones | `/api/v1/classifications` | — |
| Formatos | `/api/v1/formats` | — |
| Géneros | `/api/v1/genres` | — |
| Países | `/api/v1/countries` | — |
| Roles | `/api/v1/roles` | — |

Cada recurso expone el CRUD estándar:

| Método | Ruta | Descripción | Respuesta |
|--------|------|-------------|-----------|
| `GET` | `/{base}` | Lista todos (con filtros opcionales) | `200 OK` |
| `GET` | `/{base}/{id}` | Obtiene uno por id | `200 OK` / `404` |
| `POST` | `/{base}` | Crea un elemento | `201 Created` / `400` |
| `PUT` | `/{base}/{id}` | Actualiza completamente | `200 OK` / `400` / `404` |
| `PATCH` | `/{base}/{id}/enable` | Reactiva un elemento | `200 OK` / `404` |
| `DELETE` | `/{base}/{id}` | Elimina un elemento | `204 No Content` / `404` |

## Ejecución del backend

El backend se ejecuta con Maven:

```bash
cd MangaCoBackend
./mvnw spring-boot:run
```

Por defecto queda escuchando en `http://localhost:8080`.

## Autor

MangaCo Team
