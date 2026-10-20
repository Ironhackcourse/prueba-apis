// ==================================================
// MangaCo - Servicio de Búsqueda Global
// Responsabilidad: coordinar la búsqueda unificada entre cómics, series y autores
// Principios: Single Responsibility, Dependency Inversion (abstrae llamadas a servicios)
// ==================================================

const searchService = (() => {
    /**
     * Normaliza texto para comparación case-insensitive y sin acentos básicos
     */
    const normalize = (text = '') => {
        return text
            .toString()
            .toLowerCase()
            .normalize('NFD')
            .replace(/[\u0300-\u036f]/g, '');
    };

    /**
     * Busca coincidencias en un campo de texto
     */
    const matches = (field, query) => {
        if (!query) return false;
        if (!field) return false;
        return normalize(field).includes(normalize(query));
    };

    /**
     * Realiza búsqueda global filtrando por tipo
     * @param {Object} params
     * @param {string} params.query - Texto a buscar
     * @param {string[]} params.types - ['comic', 'serie', 'autor'] o vacío para todos
     */
    const searchGlobal = async ({ query, types = [] }) => {
        const q = (query || '').trim();
        if (!q) return { comics: [], series: [], authors: [] };

        const includeAll = types.length === 0;
        const includeComic = includeAll || types.includes('comic');
        const includeSerie = includeAll || types.includes('serie');
        const includeAutor = includeAll || types.includes('autor');

        const [comics, series, authors] = await Promise.all([
            includeComic ? comicService.findAll() : Promise.resolve([]),
            includeSerie ? seriesService.findAll() : Promise.resolve([]),
            includeAutor ? authorService.findAll() : Promise.resolve([]),
        ]);

        return {
            comics: (comics || []).filter(c => 
                matches(c.title, q) || matches(c.seriesTitle, q) || 
                (Array.isArray(c.authors) && c.authors.some(a => matches(`${a.nameAuthor} ${a.lastnameAuthor}`, q) || matches(a.penName, q)))
            ),
            series: (series || []).filter(s => matches(s.title, q) || matches(s.originalTitle, q)),
            authors: (authors || []).filter(a => 
                matches(a.nameAuthor, q) || matches(a.lastnameAuthor, q) || 
                matches(`${a.nameAuthor} ${a.lastnameAuthor}`, q) || matches(a.penName, q)
            ),
        };
    };

    return {
        searchGlobal,
    };
})();
