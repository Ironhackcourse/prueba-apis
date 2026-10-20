// ==================================================
// MangaCo - Servicio de Editoriales
// Endpoints: /api/v1/publishers
// ==================================================

const publisherService = (() => {
    const BASE_URL = 'http://localhost:8080/api/v1/publishers';

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

    return {
        findAll: (params = {}) => request(`${BASE_URL}${buildQuery(params)}`),

        findByCountry: (countryId) => request(`${BASE_URL}/country/${countryId}`),

        findById: (id, includeDisabled = false) =>
            request(`${BASE_URL}/${id}${buildQuery({ includeDisabled })}`),

        create: (body) => request(BASE_URL, { method: 'POST', body: JSON.stringify(body) }),

        update: (id, body) =>
            request(`${BASE_URL}/${id}`, { method: 'PUT', body: JSON.stringify(body) }),

        enable: (id) => request(`${BASE_URL}/${id}/enable`, { method: 'PATCH' }),

        remove: (id) => request(`${BASE_URL}/${id}`, { method: 'DELETE' }),
    };
})();
