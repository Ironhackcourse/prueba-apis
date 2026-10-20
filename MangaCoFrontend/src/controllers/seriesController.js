// ==================================================
// MangaCo - Detalle de Serie
// ==================================================

document.addEventListener('DOMContentLoaded', async () => {
    const id = getQueryParam('id');
    if (!id) {
        renderError('seriesDetail', 'Falta el ID de la serie');
        return;
    }

    try {
        const [series, comics] = await Promise.all([
            seriesService.findById(id),
            comicService.findBySeries(id),
        ]);
        renderSeries(series, comics);
    } catch (err) {
        renderError('seriesDetail', err.message);
    }
});

function renderSeries(s, comics) {
    const container = document.getElementById('seriesDetail');

    const comicsHtml = (comics && comics.length > 0)
        ? comics.map(c => `
            <a href="comic.html?id=${c.idComic}" class="card">
                <div class="card-cover">${coverImage(c.coverUrl, c.title || 'Tomo', '📚')}</div>
                <div class="card-body">
                    <h3 class="card-title">Vol. ${c.volume} — ${escapeHtml(c.title || 'Tomo')}</h3>
                    <div class="card-meta">
                        ${c.pageCount ? `<span class="badge">${c.pageCount} pág.</span>` : ''}
                        ${c.publisherName ? `<span class="badge">${escapeHtml(c.publisherName)}</span>` : ''}
                    </div>
                </div>
            </a>
        `).join('')
        : '<div class="empty-state"><h3>Aún no hay tomos registrados</h3></div>';

    container.innerHTML = `
        <div class="breadcrumb">
            <a href="index.html">Inicio</a>
            <span>›</span>
            <span>${escapeHtml(s.title)}</span>
        </div>

        <div class="detail-hero">
            <div class="detail-cover">${coverImage(s.coverUrl, s.title, '📖')}</div>

            <div class="detail-info">
                <h1>${escapeHtml(s.title)}</h1>
                ${s.originalTitle ? `<p class="detail-original-title">${escapeHtml(s.originalTitle)}</p>` : ''}

                <div class="detail-tags">
                    ${s.categoryName ? `<span class="badge badge-primary">${escapeHtml(s.categoryName)}</span>` : ''}
                    ${s.classificationName ? `<span class="badge badge-info">${escapeHtml(s.classificationName)}</span>` : ''}
                    <span class="badge ${statusBadgeClass(s.status)}">${statusLabel(s.status)}</span>
                </div>

                <div class="detail-meta">
                    <div class="meta-item">
                        <span class="meta-label">Total tomos</span>
                        <span class="meta-value">${s.totalVolumes ?? '—'}</span>
                    </div>
                    <div class="meta-item">
                        <span class="meta-label">Inicio</span>
                        <span class="meta-value">${formatDate(s.startDate)}</span>
                    </div>
                    <div class="meta-item">
                        <span class="meta-label">Fin</span>
                        <span class="meta-value">${formatDate(s.endDate)}</span>
                    </div>
                    <div class="meta-item">
                        <span class="meta-label">Estado</span>
                        <span class="meta-value">${statusLabel(s.status)}</span>
                    </div>
                </div>

                ${s.synopsis ? `<h2>Sinopsis</h2><p class="synopsis">${escapeHtml(s.synopsis)}</p>` : ''}
            </div>
        </div>

        <section>
            <h2 class="section-title" style="margin-bottom: 1rem;">Tomos publicados (${comics.length})</h2>
            <div class="grid grid-4">
                ${comicsHtml}
            </div>
        </section>
    `;
}
