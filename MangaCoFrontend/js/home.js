// ==================================================
// MangaCo - Home
// ==================================================

document.addEventListener('DOMContentLoaded', async () => {
    initSearch();
    await Promise.all([
        loadFeaturedSeries(),
        loadLatestComics(),
        loadFeaturedAuthors(),
        loadGenres(),
    ]);
});

function initSearch() {
    const input = document.getElementById('globalSearch');
    const btn = document.getElementById('searchBtn');

    const goToSearch = () => {
        const q = input.value.trim();
        if (q) {
            window.location.href = `index.html?search=${encodeURIComponent(q)}#comics-section`;
        }
    };

    btn.addEventListener('click', goToSearch);
    input.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') goToSearch();
    });
}

async function loadFeaturedSeries() {
    const container = document.getElementById('featuredSeries');
    try {
        const series = await api.getSeries();
        const featured = series.slice(0, 4);

        if (featured.length === 0) {
            renderEmpty('featuredSeries', 'No hay series todavía');
            return;
        }

        container.innerHTML = featured.map(s => `
            <a href="series.html?id=${s.idSeries}" class="card">
                <div class="card-cover">📖</div>
                <div class="card-body">
                    <h3 class="card-title">${escapeHtml(s.title)}</h3>
                    <p class="card-subtitle">${escapeHtml(s.categoryName || 'Sin categoría')}</p>
                    <div class="card-meta">
                        <span class="badge ${statusBadgeClass(s.status)}">${statusLabel(s.status)}</span>
                        <span class="badge">${s.totalVolumes || '?'} tomos</span>
                    </div>
                </div>
            </a>
        `).join('');
    } catch (err) {
        renderError('featuredSeries', err.message);
    }
}

async function loadLatestComics() {
    const container = document.getElementById('latestComics');
    try {
        const comics = await api.getComics();
        const latest = comics.slice(-8).reverse().slice(0, 4);

        if (latest.length === 0) {
            renderEmpty('latestComics', 'No hay comics todavía');
            return;
        }

        container.innerHTML = latest.map(c => `
            <a href="comic.html?id=${c.idComic}" class="card">
                <div class="card-cover">📚</div>
                <div class="card-body">
                    <h3 class="card-title">${escapeHtml(c.title || c.seriesTitle)}</h3>
                    <p class="card-subtitle">${escapeHtml(c.seriesTitle)}</p>
                    <div class="card-meta">
                        <span class="badge badge-primary">Vol. ${c.volume || '?'}</span>
                        ${c.formatName ? `<span class="badge">${escapeHtml(c.formatName)}</span>` : ''}
                    </div>
                </div>
            </a>
        `).join('');
    } catch (err) {
        renderError('latestComics', err.message);
    }
}

async function loadFeaturedAuthors() {
    const container = document.getElementById('featuredAuthors');
    try {
        const authors = await api.getAuthors();
        const featured = authors.slice(0, 4);

        if (featured.length === 0) {
            renderEmpty('featuredAuthors', 'No hay autores todavía');
            return;
        }

        container.innerHTML = featured.map(a => `
            <a href="author.html?id=${a.idAuthor}" class="card">
                <div class="card-cover">✍️</div>
                <div class="card-body">
                    <h3 class="card-title">${escapeHtml(a.nameAuthor)} ${escapeHtml(a.lastnameAuthor)}</h3>
                    <p class="card-subtitle">${escapeHtml(a.countryName || 'Sin país')}</p>
                    <div class="card-meta">
                        ${a.penName ? `<span class="badge">${escapeHtml(a.penName)}</span>` : ''}
                    </div>
                </div>
            </a>
        `).join('');
    } catch (err) {
        renderError('featuredAuthors', err.message);
    }
}

async function loadGenres() {
    const container = document.getElementById('genresList');
    try {
        const genres = await api.getGenres();
        if (genres.length === 0) {
            container.innerHTML = '<p class="empty-state">No hay géneros</p>';
            return;
        }
        container.innerHTML = genres.map(g => `
            <a href="index.html?genre=${g.idGenre}#comics-section" class="genre-chip">
                ${escapeHtml(g.nameGenre)}
            </a>
        `).join('');
    } catch (err) {
        container.innerHTML = `<div class="error-box">${escapeHtml(err.message)}</div>`;
    }
}