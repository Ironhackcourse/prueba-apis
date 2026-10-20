// ==================================================
// MangaCo - Servicio de Metadatos
// Endpoints: /api/v1/{classifications, formats, categories, genres, countries, roles}
// ==================================================

const metadataService = (() => {
    const BASE_URL = 'http://localhost:8080/api/v1';

    async function request(url, options = {}) {
        const config = {
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
            },
            ...options,
        };

        const response = await fetch(url, config);

        if (!response.ok) {
            let errorData = null;
            try {
                errorData = await response.json();
            } catch {
                errorData = null;
            }
            const message =
                (errorData && errorData.message) ||
                `Error ${response.status}: ${response.statusText}`;
            throw new Error(message);
        }

        if (response.status === 204) return null;

        return await response.json();
    }

    function buildQuery(params = {}) {
        const query = Object.entries(params)
            .filter(([, value]) => value !== null && value !== undefined && value !== '')
            .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`);
        return query.length > 0 ? `?${query.join('&')}` : '';
    }

    function crudService(resource) {
        const url = `${BASE_URL}/${resource}`;
        return {
            findAll: (params = {}) => request(`${url}${buildQuery(params)}`),

            findById: (id, includeDisabled = false) =>
                request(`${url}/${id}${buildQuery({ includeDisabled })}`),

            create: (body) => request(url, { method: 'POST', body: JSON.stringify(body) }),

            update: (id, body) =>
                request(`${url}/${id}`, { method: 'PUT', body: JSON.stringify(body) }),

            enable: (id) => request(`${url}/${id}/enable`, { method: 'PATCH' }),

            remove: (id) => request(`${url}/${id}`, { method: 'DELETE' }),
        };
    }

    return {
        classifications: crudService('classifications'),
        formats: crudService('formats'),
        categories: crudService('categories'),
        genres: crudService('genres'),
        countries: crudService('countries'),
        roles: crudService('roles'),
    };
})();
