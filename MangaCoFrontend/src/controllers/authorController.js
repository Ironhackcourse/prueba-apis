// ==================================================
// MangaCo - Detalle de Autor
// ==================================================

document.addEventListener('DOMContentLoaded', async () => {
    const id = getQueryParam('id');
    if (!id) {
        renderError('authorDetail', 'Falta el ID del autor');
        return;
    }

    try {
        const author = await authorService.findById(id);
        renderAuthor(author);
    } catch (err) {
        renderError('authorDetail', err.message);
    }
});

function renderAuthor(a) {
    const container = document.getElementById('authorDetail');

    container.innerHTML = `
        <div class="breadcrumb">
            <a href="index.html">Inicio</a>
            <span>›</span>
            <span>${escapeHtml(a.nameAuthor)} ${escapeHtml(a.lastnameAuthor)}</span>
        </div>

        <div class="detail-hero">
            <div class="detail-cover">${coverImage(a.photoUrl, `${a.nameAuthor} ${a.lastnameAuthor}`, '✍️')}</div>

            <div class="detail-info">
                <h1>${escapeHtml(a.nameAuthor)} ${escapeHtml(a.lastnameAuthor)}</h1>
                ${a.penName ? `<p class="detail-original-title">Conocido como "${escapeHtml(a.penName)}"</p>` : ''}

                <div class="detail-tags">
                    ${a.countryName ? `<span class="badge badge-primary">${escapeHtml(a.countryName)}</span>` : ''}
                </div>

                <div class="detail-meta">
                    <div class="meta-item">
                        <span class="meta-label">País</span>
                        <span class="meta-value">${escapeHtml(a.countryName || '—')}</span>
                    </div>
                    <div class="meta-item">
                        <span class="meta-label">Nacimiento</span>
                        <span class="meta-value">${formatDate(a.birthDate)}</span>
                    </div>
                    <div class="meta-item">
                        <span class="meta-label">Seudónimo</span>
                        <span class="meta-value">${escapeHtml(a.penName || '—')}</span>
                    </div>
                </div>

                ${a.biography ? `<h2>Biografía</h2><p class="synopsis">${escapeHtml(a.biography)}</p>` : ''}
            </div>
        </div>
    `;
}
