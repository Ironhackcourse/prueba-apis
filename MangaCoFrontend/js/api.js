// ==================================================
// MangaCo - Cliente API
// ==================================================

const API_BASE_URL = 'http://localhost:8080/api/v1';

async function apiRequest(endpoint, options = {}) {
    const url = `${API_BASE_URL}${endpoint}`;

    const defaultOptions = {
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
    };

    const config = { ...defaultOptions, ...options };

    try {
        const response = await fetch(url, config);

        if (!response.ok) {
            let errorData;
            try {
                errorData = await response.json();
            } catch {
                errorData = { message: `Error ${response.status}: ${response.statusText}` };
            }
            throw new Error(errorData.message || `Error ${response.status}`);
        }

        if (response.status === 204) return null;

        return await response.json();
    } catch (error) {
        console.error(`API Error [${endpoint}]:`, error);
        throw error;
    }
}

const api = {
    // Comics
    getComics: (params = {}) => apiRequest(`/comics${buildQuery(params)}`),
    getComicById: (id) => apiRequest(`/comics/${id}`),
    getComicsBySeries: (seriesId) => apiRequest(`/comics/series/${seriesId}`),

    // Series
    getSeries: (params = {}) => apiRequest(`/series${buildQuery(params)}`),
    getSeriesById: (id) => apiRequest(`/series/${id}`),

    // Autores
    getAuthors: (params = {}) => apiRequest(`/authors${buildQuery(params)}`),
    getAuthorById: (id) => apiRequest(`/authors/${id}`),

    // Géneros
    getGenres: (params = {}) => apiRequest(`/genres${buildQuery(params)}`),
    getGenreById: (id) => apiRequest(`/genres/${id}`),

    // Categorías
    getCategories: (params = {}) => apiRequest(`/categories${buildQuery(params)}`),

    // Editoriales
    getPublishers: (params = {}) => apiRequest(`/publishers${buildQuery(params)}`),

    // Países
    getCountries: (params = {}) => apiRequest(`/countries${buildQuery(params)}`),

    // Formatos
    getFormats: (params = {}) => apiRequest(`/formats${buildQuery(params)}`),

    // Clasificaciones
    getClassifications: (params = {}) => apiRequest(`/classifications${buildQuery(params)}`),
};

function buildQuery(params) {
    const cleanParams = Object.entries(params)
        .filter(([, v]) => v !== null && v !== undefined && v !== '')
        .map(([k, v]) => `${encodeURIComponent(k)}=${encodeURIComponent(v)}`);

    return cleanParams.length > 0 ? `?${cleanParams.join('&')}` : '';
}