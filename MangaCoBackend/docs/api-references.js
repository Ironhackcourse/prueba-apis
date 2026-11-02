/**
 * @file Referencias de la API REST de MangaCo (autogenerado).
 *
 * Este archivo se genera automáticamente a partir de los controladores
 * de Spring Boot mediante `npm run docs`. No editar a mano.
 *
 * @author MangaCo Team
 * @since 2026-10-21
 * @version 1.0.0
 * @tutorial introduccion
 */

/**
 * @file Endpoints REST del controlador **AuthorController**.
 *
 * Ruta base: `/api/v1/authors`.
 *
 * @module authorController
 * @author MangaCo Team
 * @since 2026-10-21
 * @version 1.0.0
 * @tutorial introduccion
 */

/**
 * **`GET /api/v1/authors`**
 *
 * Controlador: `AuthorController`. Método Java: `findAll`.
 *
 * @function findAll
 *
 * @param {string} [enabled] - Parámetro de consulta (query string).
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const authorController_findAll = () => {};

/**
 * **`GET /api/v1/authors/country/:countryId`**
 *
 * Controlador: `AuthorController`. Método Java: `findByCountry`.
 *
 * @function findByCountry
 *
 * @param {string} countryId - Parámetro de ruta.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const authorController_findByCountry = () => {};

/**
 * **`GET /api/v1/authors/:id`**
 *
 * Controlador: `AuthorController`. Método Java: `findById`.
 *
 * @function findById
 *
 * @param {string} id - Parámetro de ruta.
 *
 * @param {string} [includeDisabled] - Parámetro de consulta (query string).
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const authorController_findById = () => {};

/**
 * **`POST /api/v1/authors`**
 *
 * Controlador: `AuthorController`. Método Java: `create`.
 *
 * @function create
 * @param {Object} body - Cuerpo de la petición en formato JSON.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const authorController_create = () => {};

/**
 * **`PUT /api/v1/authors/:id`**
 *
 * Controlador: `AuthorController`. Método Java: `update`.
 *
 * @function update
 *
 * @param {string} id - Parámetro de ruta.
 * @param {Object} body - Cuerpo de la petición en formato JSON.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const authorController_update = () => {};

/**
 * **`PATCH /api/v1/authors/:id/enable`**
 *
 * Controlador: `AuthorController`. Método Java: `enable`.
 *
 * @function enable
 *
 * @param {string} id - Parámetro de ruta.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const authorController_enable = () => {};

/**
 * **`DELETE /api/v1/authors/:id`**
 *
 * Controlador: `AuthorController`. Método Java: `delete`.
 *
 * @function delete
 *
 * @param {string} id - Parámetro de ruta.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const authorController_delete_ = () => {};

/**
 * @file Endpoints REST del controlador **CategoryController**.
 *
 * Ruta base: `/api/v1/categories`.
 *
 * @module categoryController
 * @author MangaCo Team
 * @since 2026-10-21
 * @version 1.0.0
 * @tutorial introduccion
 */

/**
 * **`GET /api/v1/categories`**
 *
 * Controlador: `CategoryController`. Método Java: `findAll`.
 *
 * @function findAll
 *
 * @param {string} [enabled] - Parámetro de consulta (query string).
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const categoryController_findAll = () => {};

/**
 * **`GET /api/v1/categories/:id`**
 *
 * Controlador: `CategoryController`. Método Java: `findById`.
 *
 * @function findById
 *
 * @param {string} id - Parámetro de ruta.
 *
 * @param {string} [includeDisabled] - Parámetro de consulta (query string).
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const categoryController_findById = () => {};

/**
 * **`POST /api/v1/categories`**
 *
 * Controlador: `CategoryController`. Método Java: `create`.
 *
 * @function create
 * @param {Object} body - Cuerpo de la petición en formato JSON.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const categoryController_create = () => {};

/**
 * **`PUT /api/v1/categories/:id`**
 *
 * Controlador: `CategoryController`. Método Java: `update`.
 *
 * @function update
 *
 * @param {string} id - Parámetro de ruta.
 * @param {Object} body - Cuerpo de la petición en formato JSON.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const categoryController_update = () => {};

/**
 * **`PATCH /api/v1/categories/:id/enable`**
 *
 * Controlador: `CategoryController`. Método Java: `enable`.
 *
 * @function enable
 *
 * @param {string} id - Parámetro de ruta.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const categoryController_enable = () => {};

/**
 * **`DELETE /api/v1/categories/:id`**
 *
 * Controlador: `CategoryController`. Método Java: `delete`.
 *
 * @function delete
 *
 * @param {string} id - Parámetro de ruta.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const categoryController_delete_ = () => {};

/**
 * @file Endpoints REST del controlador **ClassificationController**.
 *
 * Ruta base: `/api/v1/classifications`.
 *
 * @module classificationController
 * @author MangaCo Team
 * @since 2026-10-21
 * @version 1.0.0
 * @tutorial introduccion
 */

/**
 * **`GET /api/v1/classifications`**
 *
 * Controlador: `ClassificationController`. Método Java: `findAll`.
 *
 * @function findAll
 *
 * @param {string} [enabled] - Parámetro de consulta (query string).
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const classificationController_findAll = () => {};

/**
 * **`GET /api/v1/classifications/:id`**
 *
 * Controlador: `ClassificationController`. Método Java: `findById`.
 *
 * @function findById
 *
 * @param {string} id - Parámetro de ruta.
 *
 * @param {string} [includeDisabled] - Parámetro de consulta (query string).
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const classificationController_findById = () => {};

/**
 * **`POST /api/v1/classifications`**
 *
 * Controlador: `ClassificationController`. Método Java: `create`.
 *
 * @function create
 * @param {Object} body - Cuerpo de la petición en formato JSON.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const classificationController_create = () => {};

/**
 * **`PUT /api/v1/classifications/:id`**
 *
 * Controlador: `ClassificationController`. Método Java: `update`.
 *
 * @function update
 *
 * @param {string} id - Parámetro de ruta.
 * @param {Object} body - Cuerpo de la petición en formato JSON.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const classificationController_update = () => {};

/**
 * **`PATCH /api/v1/classifications/:id/enable`**
 *
 * Controlador: `ClassificationController`. Método Java: `enable`.
 *
 * @function enable
 *
 * @param {string} id - Parámetro de ruta.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const classificationController_enable = () => {};

/**
 * **`DELETE /api/v1/classifications/:id`**
 *
 * Controlador: `ClassificationController`. Método Java: `delete`.
 *
 * @function delete
 *
 * @param {string} id - Parámetro de ruta.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const classificationController_delete_ = () => {};

/**
 * @file Endpoints REST del controlador **ComicController**.
 *
 * Ruta base: `/api/v1/comics`.
 *
 * @module comicController
 * @author MangaCo Team
 * @since 2026-10-21
 * @version 1.0.0
 * @tutorial introduccion
 */

/**
 * **`GET /api/v1/comics`**
 *
 * Controlador: `ComicController`. Método Java: `findAll`.
 *
 * @function findAll
 *
 * @param {string} [enabled] - Parámetro de consulta (query string).
 * @param {string} [seriesId] - Parámetro de consulta (query string).
 * @param {string} [publisherId] - Parámetro de consulta (query string).
 * @param {string} [countryId] - Parámetro de consulta (query string).
 * @param {string} [formatId] - Parámetro de consulta (query string).
 * @param {string} [title] - Parámetro de consulta (query string).
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const comicController_findAll = () => {};

/**
 * **`GET /api/v1/comics/series/:seriesId`**
 *
 * Controlador: `ComicController`. Método Java: `findBySeries`.
 *
 * @function findBySeries
 *
 * @param {string} seriesId - Parámetro de ruta.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const comicController_findBySeries = () => {};

/**
 * **`GET /api/v1/comics/:id`**
 *
 * Controlador: `ComicController`. Método Java: `findById`.
 *
 * @function findById
 *
 * @param {string} id - Parámetro de ruta.
 *
 * @param {string} [includeDisabled] - Parámetro de consulta (query string).
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const comicController_findById = () => {};

/**
 * **`POST /api/v1/comics`**
 *
 * Controlador: `ComicController`. Método Java: `create`.
 *
 * @function create
 * @param {Object} body - Cuerpo de la petición en formato JSON.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const comicController_create = () => {};

/**
 * **`PUT /api/v1/comics/:id`**
 *
 * Controlador: `ComicController`. Método Java: `update`.
 *
 * @function update
 *
 * @param {string} id - Parámetro de ruta.
 * @param {Object} body - Cuerpo de la petición en formato JSON.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const comicController_update = () => {};

/**
 * **`PATCH /api/v1/comics/:id/enable`**
 *
 * Controlador: `ComicController`. Método Java: `enable`.
 *
 * @function enable
 *
 * @param {string} id - Parámetro de ruta.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const comicController_enable = () => {};

/**
 * **`DELETE /api/v1/comics/:id`**
 *
 * Controlador: `ComicController`. Método Java: `delete`.
 *
 * @function delete
 *
 * @param {string} id - Parámetro de ruta.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const comicController_delete_ = () => {};

/**
 * @file Endpoints REST del controlador **CountryController**.
 *
 * Ruta base: `/api/v1/countries`.
 *
 * @module countryController
 * @author MangaCo Team
 * @since 2026-10-21
 * @version 1.0.0
 * @tutorial introduccion
 */

/**
 * **`GET /api/v1/countries`**
 *
 * Controlador: `CountryController`. Método Java: `findAll`.
 *
 * @function findAll
 *
 * @param {string} [enabled] - Parámetro de consulta (query string).
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const countryController_findAll = () => {};

/**
 * **`GET /api/v1/countries/:id`**
 *
 * Controlador: `CountryController`. Método Java: `findById`.
 *
 * @function findById
 *
 * @param {string} id - Parámetro de ruta.
 *
 * @param {string} [includeDisabled] - Parámetro de consulta (query string).
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const countryController_findById = () => {};

/**
 * **`POST /api/v1/countries`**
 *
 * Controlador: `CountryController`. Método Java: `create`.
 *
 * @function create
 * @param {Object} body - Cuerpo de la petición en formato JSON.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const countryController_create = () => {};

/**
 * **`PUT /api/v1/countries/:id`**
 *
 * Controlador: `CountryController`. Método Java: `update`.
 *
 * @function update
 *
 * @param {string} id - Parámetro de ruta.
 * @param {Object} body - Cuerpo de la petición en formato JSON.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const countryController_update = () => {};

/**
 * **`PATCH /api/v1/countries/:id/enable`**
 *
 * Controlador: `CountryController`. Método Java: `enable`.
 *
 * @function enable
 *
 * @param {string} id - Parámetro de ruta.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const countryController_enable = () => {};

/**
 * **`DELETE /api/v1/countries/:id`**
 *
 * Controlador: `CountryController`. Método Java: `delete`.
 *
 * @function delete
 *
 * @param {string} id - Parámetro de ruta.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const countryController_delete_ = () => {};

/**
 * @file Endpoints REST del controlador **FormatController**.
 *
 * Ruta base: `/api/v1/formats`.
 *
 * @module formatController
 * @author MangaCo Team
 * @since 2026-10-21
 * @version 1.0.0
 * @tutorial introduccion
 */

/**
 * **`GET /api/v1/formats`**
 *
 * Controlador: `FormatController`. Método Java: `findAll`.
 *
 * @function findAll
 *
 * @param {string} [enabled] - Parámetro de consulta (query string).
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const formatController_findAll = () => {};

/**
 * **`GET /api/v1/formats/:id`**
 *
 * Controlador: `FormatController`. Método Java: `findById`.
 *
 * @function findById
 *
 * @param {string} id - Parámetro de ruta.
 *
 * @param {string} [includeDisabled] - Parámetro de consulta (query string).
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const formatController_findById = () => {};

/**
 * **`POST /api/v1/formats`**
 *
 * Controlador: `FormatController`. Método Java: `create`.
 *
 * @function create
 * @param {Object} body - Cuerpo de la petición en formato JSON.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const formatController_create = () => {};

/**
 * **`PUT /api/v1/formats/:id`**
 *
 * Controlador: `FormatController`. Método Java: `update`.
 *
 * @function update
 *
 * @param {string} id - Parámetro de ruta.
 * @param {Object} body - Cuerpo de la petición en formato JSON.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const formatController_update = () => {};

/**
 * **`PATCH /api/v1/formats/:id/enable`**
 *
 * Controlador: `FormatController`. Método Java: `enable`.
 *
 * @function enable
 *
 * @param {string} id - Parámetro de ruta.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const formatController_enable = () => {};

/**
 * **`DELETE /api/v1/formats/:id`**
 *
 * Controlador: `FormatController`. Método Java: `delete`.
 *
 * @function delete
 *
 * @param {string} id - Parámetro de ruta.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const formatController_delete_ = () => {};

/**
 * @file Endpoints REST del controlador **GenreController**.
 *
 * Ruta base: `/api/v1/genres`.
 *
 * @module genreController
 * @author MangaCo Team
 * @since 2026-10-21
 * @version 1.0.0
 * @tutorial introduccion
 */

/**
 * **`GET /api/v1/genres`**
 *
 * Controlador: `GenreController`. Método Java: `findAll`.
 *
 * @function findAll
 *
 * @param {string} [enabled] - Parámetro de consulta (query string).
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const genreController_findAll = () => {};

/**
 * **`GET /api/v1/genres/:id`**
 *
 * Controlador: `GenreController`. Método Java: `findById`.
 *
 * @function findById
 *
 * @param {string} id - Parámetro de ruta.
 *
 * @param {string} [includeDisabled] - Parámetro de consulta (query string).
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const genreController_findById = () => {};

/**
 * **`POST /api/v1/genres`**
 *
 * Controlador: `GenreController`. Método Java: `create`.
 *
 * @function create
 * @param {Object} body - Cuerpo de la petición en formato JSON.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const genreController_create = () => {};

/**
 * **`PUT /api/v1/genres/:id`**
 *
 * Controlador: `GenreController`. Método Java: `update`.
 *
 * @function update
 *
 * @param {string} id - Parámetro de ruta.
 * @param {Object} body - Cuerpo de la petición en formato JSON.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const genreController_update = () => {};

/**
 * **`PATCH /api/v1/genres/:id/enable`**
 *
 * Controlador: `GenreController`. Método Java: `enable`.
 *
 * @function enable
 *
 * @param {string} id - Parámetro de ruta.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const genreController_enable = () => {};

/**
 * **`DELETE /api/v1/genres/:id`**
 *
 * Controlador: `GenreController`. Método Java: `delete`.
 *
 * @function delete
 *
 * @param {string} id - Parámetro de ruta.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const genreController_delete_ = () => {};

/**
 * @file Endpoints REST del controlador **PublisherController**.
 *
 * Ruta base: `/api/v1/publishers`.
 *
 * @module publisherController
 * @author MangaCo Team
 * @since 2026-10-21
 * @version 1.0.0
 * @tutorial introduccion
 */

/**
 * **`GET /api/v1/publishers`**
 *
 * Controlador: `PublisherController`. Método Java: `findAll`.
 *
 * @function findAll
 *
 * @param {string} [enabled] - Parámetro de consulta (query string).
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const publisherController_findAll = () => {};

/**
 * **`GET /api/v1/publishers/country/:countryId`**
 *
 * Controlador: `PublisherController`. Método Java: `findByCountry`.
 *
 * @function findByCountry
 *
 * @param {string} countryId - Parámetro de ruta.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const publisherController_findByCountry = () => {};

/**
 * **`GET /api/v1/publishers/:id`**
 *
 * Controlador: `PublisherController`. Método Java: `findById`.
 *
 * @function findById
 *
 * @param {string} id - Parámetro de ruta.
 *
 * @param {string} [includeDisabled] - Parámetro de consulta (query string).
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const publisherController_findById = () => {};

/**
 * **`POST /api/v1/publishers`**
 *
 * Controlador: `PublisherController`. Método Java: `create`.
 *
 * @function create
 * @param {Object} body - Cuerpo de la petición en formato JSON.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const publisherController_create = () => {};

/**
 * **`PUT /api/v1/publishers/:id`**
 *
 * Controlador: `PublisherController`. Método Java: `update`.
 *
 * @function update
 *
 * @param {string} id - Parámetro de ruta.
 * @param {Object} body - Cuerpo de la petición en formato JSON.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const publisherController_update = () => {};

/**
 * **`PATCH /api/v1/publishers/:id/enable`**
 *
 * Controlador: `PublisherController`. Método Java: `enable`.
 *
 * @function enable
 *
 * @param {string} id - Parámetro de ruta.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const publisherController_enable = () => {};

/**
 * **`DELETE /api/v1/publishers/:id`**
 *
 * Controlador: `PublisherController`. Método Java: `delete`.
 *
 * @function delete
 *
 * @param {string} id - Parámetro de ruta.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const publisherController_delete_ = () => {};

/**
 * @file Endpoints REST del controlador **RoleController**.
 *
 * Ruta base: `/api/v1/roles`.
 *
 * @module roleController
 * @author MangaCo Team
 * @since 2026-10-21
 * @version 1.0.0
 * @tutorial introduccion
 */

/**
 * **`GET /api/v1/roles`**
 *
 * Controlador: `RoleController`. Método Java: `findAll`.
 *
 * @function findAll
 *
 * @param {string} [enabled] - Parámetro de consulta (query string).
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const roleController_findAll = () => {};

/**
 * **`GET /api/v1/roles/:id`**
 *
 * Controlador: `RoleController`. Método Java: `findById`.
 *
 * @function findById
 *
 * @param {string} id - Parámetro de ruta.
 *
 * @param {string} [includeDisabled] - Parámetro de consulta (query string).
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const roleController_findById = () => {};

/**
 * **`POST /api/v1/roles`**
 *
 * Controlador: `RoleController`. Método Java: `create`.
 *
 * @function create
 * @param {Object} body - Cuerpo de la petición en formato JSON.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const roleController_create = () => {};

/**
 * **`PUT /api/v1/roles/:id`**
 *
 * Controlador: `RoleController`. Método Java: `update`.
 *
 * @function update
 *
 * @param {string} id - Parámetro de ruta.
 * @param {Object} body - Cuerpo de la petición en formato JSON.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const roleController_update = () => {};

/**
 * **`PATCH /api/v1/roles/:id/enable`**
 *
 * Controlador: `RoleController`. Método Java: `enable`.
 *
 * @function enable
 *
 * @param {string} id - Parámetro de ruta.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const roleController_enable = () => {};

/**
 * **`DELETE /api/v1/roles/:id`**
 *
 * Controlador: `RoleController`. Método Java: `delete`.
 *
 * @function delete
 *
 * @param {string} id - Parámetro de ruta.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const roleController_delete_ = () => {};

/**
 * @file Endpoints REST del controlador **SeriesController**.
 *
 * Ruta base: `/api/v1/series`.
 *
 * @module seriesController
 * @author MangaCo Team
 * @since 2026-10-21
 * @version 1.0.0
 * @tutorial introduccion
 */

/**
 * **`GET /api/v1/series`**
 *
 * Controlador: `SeriesController`. Método Java: `findAll`.
 *
 * @function findAll
 *
 * @param {string} [enabled] - Parámetro de consulta (query string).
 * @param {string} [status] - Parámetro de consulta (query string).
 * @param {string} [categoryId] - Parámetro de consulta (query string).
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const seriesController_findAll = () => {};

/**
 * **`GET /api/v1/series/:id`**
 *
 * Controlador: `SeriesController`. Método Java: `findById`.
 *
 * @function findById
 *
 * @param {string} id - Parámetro de ruta.
 *
 * @param {string} [includeDisabled] - Parámetro de consulta (query string).
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const seriesController_findById = () => {};

/**
 * **`POST /api/v1/series`**
 *
 * Controlador: `SeriesController`. Método Java: `create`.
 *
 * @function create
 * @param {Object} body - Cuerpo de la petición en formato JSON.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const seriesController_create = () => {};

/**
 * **`PUT /api/v1/series/:id`**
 *
 * Controlador: `SeriesController`. Método Java: `update`.
 *
 * @function update
 *
 * @param {string} id - Parámetro de ruta.
 * @param {Object} body - Cuerpo de la petición en formato JSON.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const seriesController_update = () => {};

/**
 * **`PATCH /api/v1/series/:id/enable`**
 *
 * Controlador: `SeriesController`. Método Java: `enable`.
 *
 * @function enable
 *
 * @param {string} id - Parámetro de ruta.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const seriesController_enable = () => {};

/**
 * **`DELETE /api/v1/series/:id`**
 *
 * Controlador: `SeriesController`. Método Java: `delete`.
 *
 * @function delete
 *
 * @param {string} id - Parámetro de ruta.
 * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.
 * @throws {Error} Si la petición falla (4xx/5xx).
 * @async
 */
const seriesController_delete_ = () => {};
