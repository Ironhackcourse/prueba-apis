// ==================================================
// MangaCo - Detalle de Comic
// ==================================================

document.addEventListener('DOMContentLoaded', async () => {
    const id = getQueryParam('id');
    if (!id) {
        renderError('comicDetail', 'Falta el ID del comic');
        return;
    }

    try {
        const comic = await api.getComicById(id);
        renderComic(comic);
    } catch (err) {
        renderError('comicDetail', err.message);
    }
});

function renderComic(c) {
    const container = document.getElementById('comicDetail');

    const authorsHtml = (c.authors && c.authors.length > 0)
        ? c.authors.map(a => `
            <a href="author.html?id=${a.idAuthor}" class="author-line">
                <span class="author-name">${escapeHtml(a.nameAuthor)} ${escapeHtml(a.lastnameAuthor)}</span>
                <span class="author-role">${authorRoleLabel(a.authorRole)}</span>
            </a>
        `).join('')
        : '<p class="card-subtitle">Sin autores asignados</p>';

    const genresHtml = (c.genres && c.genres.length > 0)
        ? c.genres.map(g => `<span class="badge badge-primary">${escapeHtml(g.nameGenre)}</span>`).join('')
        : '<span class="badge">Sin géneros</span>';

    container.innerHTML = `
        <div class="breadcrumb">
            <a href="index.html">Inicio</a>
            <span>›</span>
            <a href="series.html?id=${c.seriesId}">${escapeHtml(c.seriesTitle || 'Serie')}</a>
            <span>›</span>
            <span>${escapeHtml(c.title || 'Comic')}</span>
        </div>

        <div class="detail-hero">
            <div class="detail-cover">📚</div>

            <div class="detail-info">
                <h1>${escapeHtml(c.title || c.seriesTitle)}</h1>
                <p class="detail-original-title">
                    Serie: <a href="series.html?id=${c.seriesId}">${escapeHtml(c.seriesTitle)}</a>
                    ${c.volume ? ` — Volumen ${c.volume}` : ''}
                </p>

                <div class="detail-tags">
                    ${genresHtml}
                    ${c.formatName ? `<span class="badge">${escapeHtml(c.formatName)}</span>` : ''}
                </div>

                <div class="detail-meta">
                    <div class="meta-item">
                        <span class="meta-label">Volumen</span>
                        <span class="meta-value">${c.volume ?? '—'}</span>
                    </div>
                    <div class="meta-item">
                        <span class="meta-label">ISBN</span>
                        <span class="meta-value">${escapeHtml(c.isbn || '—')}</span>
                    </div>
                    <div class="meta-item">
                        <span class="meta-label">Editorial</span>
                        <span class="meta-value">${escapeHtml(c.publisherName || '—')}</span>
                    </div>
                    <div class="meta-item">
                        <span class="meta-label">País</span>
                        <span class="meta-value">${escapeHtml(c.countryName || '—')}</span>
                    </div>
                    <div class="meta-item">
                        <span class="meta-label">Formato</span>
                        <span class="meta-value">${escapeHtml(c.formatName || '—')}</span>
                    </div>
                    <div class="meta-item">
                        <span class="meta-label">Páginas</span>
                        <span class="meta-value">${c.pageCount ?? '—'}</span>
                    </div>
                    <div class="meta-item">
                        <span class="meta-label">Publicación</span>
                        <span class="meta-value">${formatDate(c.releaseDate)}</span>
                    </div>
                </div>

                <h2>Autores</h2>
                <div class="authors-list">
                    ${authorsHtml}
                </div>

                <div style="margin-top: 2rem;">
                    <a href="series.html?id=${c.seriesId}" class="btn btn-primary">
                        Ver serie completa →
                    </a>
                </div>
            </div>
        </div>
    `;
}