// ==================================================
// MangaCo - Controlador de Búsqueda Global (Index)
// Separación estricta Vista/Controlador - sigue SOLID
// SRP: Solo maneja eventos y renderizado de resultados de búsqueda
// ==================================================

/**
 * Inicializa el buscador global en el index
 * @param {Object} deps - Dependencias inyectadas (facilita testabilidad)
 */
const initGlobalSearch = (deps = {}) => {
    const {
        searchServiceInstance = searchService,
        dom = {
            input: document.getElementById('globalSearch'),
            button: document.getElementById('searchBtn'),
        },
    } = deps;

    if (!dom.input || !dom.button) return;

    /**
     * Renderiza resultados agrupados por tipo
     */
    const renderResults = (results) => {
        // Crear contenedor dinámico si no existe
        let container = document.getElementById('searchResults');
        if (!container) {
            container = document.createElement('section');
            container.id = 'searchResults';
            container.className = 'section';
            container.innerHTML = `
                <div class="container">
                    <div class="section-header">
                        <h2 class="section-title">Resultados de búsqueda</h2>
                    </div>
                    <div id="searchResultsContent"></div>
                </div>
            `;
            const hero = document.querySelector('.hero');
            if (hero && hero.parentNode) {
                hero.parentNode.insertBefore(container, hero.nextSibling);
            }
        }

        const content = document.getElementById('searchResultsContent');
        const { comics = [], series = [], authors = [] } = results;
        const total = comics.length + series.length + authors.length;

        if (total === 0) {
            content.innerHTML = `<p class="text-muted">No se encontraron resultados.</p>`;
            container.scrollIntoView({ behavior: 'smooth', block: 'start' });
            return;
        }

        let html = '';
        
        if (comics.length > 0) {
            html += `
                <h3 style="margin:1.5rem 0 1rem;">Cómics</h3>
                <div class="grid grid-4">
                    ${comics.slice(0, 8).map(c => `
                        <a href="comic.html?id=${c.idComic}" class="card">
                            <div class="card-cover">${typeof coverImage === 'function' ? coverImage(c.coverUrl, c.title || c.seriesTitle, '📚') : (c.coverUrl || '📚')}</div>
                            <div class="card-body">
                                <h3 class="card-title">${escapeHtml(c.title || c.seriesTitle)}</h3>
                                <p class="card-subtitle">${escapeHtml(c.seriesTitle || '')}</p>
                            </div>
                        </a>
                    `).join('')}
                </div>
            `;
        }

        if (series.length > 0) {
            html += `
                <h3 style="margin:2rem 0 1rem;">Series</h3>
                <div class="grid grid-4">
                    ${series.slice(0, 8).map(s => `
                        <a href="series.html?id=${s.idSeries}" class="card">
                            <div class="card-cover">${typeof coverImage === 'function' ? coverImage(s.coverUrl, s.title, '📖') : (s.coverUrl || '📖')}</div>
                            <div class="card-body">
                                <h3 class="card-title">${escapeHtml(s.title)}</h3>
                                <p class="card-subtitle">${escapeHtml(s.categoryName || '')}</p>
                            </div>
                        </a>
                    `).join('')}
                </div>
            `;
        }

        if (authors.length > 0) {
            html += `
                <h3 style="margin:2rem 0 1rem;">Autores</h3>
                <div class="grid grid-4">
                    ${authors.slice(0, 8).map(a => `
                        <a href="author.html?id=${a.idAuthor}" class="card">
                            <div class="card-cover">${typeof coverImage === 'function' ? coverImage(a.photoUrl, `${a.nameAuthor} ${a.lastnameAuthor}`, '✍️') : (a.photoUrl || '✍️')}</div>
                            <div class="card-body">
                                <h3 class="card-title">${escapeHtml(a.nameAuthor)} ${escapeHtml(a.lastnameAuthor)}</h3>
                                <p class="card-subtitle">${escapeHtml(a.penName || a.countryName || '')}</p>
                            </div>
                        </a>
                    `).join('')}
                </div>
            `;
        }

        content.innerHTML = html;
        container.scrollIntoView({ behavior: 'smooth', block: 'start' });
    };

    /**
     * Maneja el evento de búsqueda
     */
    const handleSearch = async () => {
        const query = dom.input.value;
        try {
            if (typeof showLoading === 'function') showLoading();
            const results = await searchServiceInstance.searchGlobal({ query });
            renderResults(results);
        } catch (err) {
            console.error('Error en búsqueda global:', err);
            alert('Error al realizar la búsqueda');
        } finally {
            if (typeof hideLoading === 'function') hideLoading();
        }
    };

    // Event listeners - separación de eventos del DOM
    dom.button.addEventListener('click', handleSearch);
    dom.input.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            handleSearch();
        }
    });
};

// Auto-inicialización
document.addEventListener('DOMContentLoaded', () => {
    initGlobalSearch();
});
