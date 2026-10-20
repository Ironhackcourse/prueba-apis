// ==================================================
// MangaCo - Cómics (listado + CRUD)
// ==================================================

const MODAL_COMIC = 'modalComic';

document.addEventListener('DOMContentLoaded', async () => {
    await initComicSelects();
    initComicEvents();
    await loadComics();
});

async function initComicSelects() {
    try {
        const [series, publishers, countries, formats, genres] = await Promise.all([
            seriesService.findAll({ enabled: true }),
            publisherService.findAll({ enabled: true }),
            metadataService.countries.findAll({ enabled: true }),
            metadataService.formats.findAll({ enabled: true }),
            metadataService.genres.findAll({ enabled: true }),
        ]);

        populateSelect(document.getElementById('seriesId'), series, 'idSeries', 'title', '— Selecciona una serie —');
        populateSelect(document.getElementById('publisherId'), publishers, 'idPublisher', 'namePublisher', '— Selecciona una editorial —');
        populateSelect(document.getElementById('countryId'), countries, 'idCountry', 'nameCountry', '— Selecciona un país —');
        populateSelect(document.getElementById('formatId'), formats, 'idFormat', 'nameFormat', '— Selecciona un formato —');

        const genreSelect = document.getElementById('genreIds');
        if (genreSelect) {
            genreSelect.innerHTML = genres
                .map(g => `<option value="${g.idGenre}">${escapeHtml(g.nameGenre)}</option>`)
                .join('');
        }
    } catch (err) {
        console.error('No se pudieron cargar los catálogos del cómic', err);
    }
}

function initComicEvents() {
    document.getElementById('btnNewComic').addEventListener('click', () => openComicModal());
    document.getElementById('form-comic').addEventListener('submit', onSubmitComic);
}

async function loadComics() {
    const seriesId = getQueryParam('seriesId');

    try {
        const comics = seriesId
            ? await comicService.findBySeries(seriesId)
            : await comicService.findAll();

        if (!comics || comics.length === 0) {
            renderEmpty('comicsList', 'No hay cómics todavía');
            return;
        }

        const container = document.getElementById('comicsList');
        container.innerHTML = comics.map(renderComicCard).join('');
        bindComicActions();
    } catch (err) {
        renderError('comicsList', err.message);
    }
}

function renderComicCard(c) {
    return `
        <div class="card" data-id="${c.idComic}">
            <a href="comic.html?id=${c.idComic}" class="card-link">
                <div class="card-cover">${coverImage(c.coverUrl, c.title || c.seriesTitle, '📚')}</div>
            </a>
            <div class="card-body">
                <a href="comic.html?id=${c.idComic}">
                    <h3 class="card-title">${escapeHtml(c.title || c.seriesTitle)}</h3>
                </a>
                <p class="card-subtitle">${escapeHtml(c.seriesTitle || '')}${c.volume ? ` — Vol. ${c.volume}` : ''}</p>
                <div class="card-meta">
                    ${c.formatName ? `<span class="badge">${escapeHtml(c.formatName)}</span>` : ''}
                    <span class="badge ${c.enabled ? 'badge-success' : 'badge-danger'}">${c.enabled ? 'Activo' : 'Inactivo'}</span>
                </div>
                <div class="card-actions">
                    <button type="button" class="btn btn-warning btn-sm btn-edit" data-id="${c.idComic}">✏️ Editar</button>
                    <button type="button" class="btn btn-danger btn-sm btn-delete" data-id="${c.idComic}">🗑️ Eliminar</button>
                    ${!c.enabled ? `<button type="button" class="btn btn-primary btn-sm btn-enable" data-id="${c.idComic}">✅ Habilitar</button>` : ''}
                </div>
            </div>
        </div>
    `;
}

function bindComicActions() {
    document.querySelectorAll('#comicsList .btn-edit').forEach(btn => {
        btn.addEventListener('click', () => editComic(btn.dataset.id));
    });
    document.querySelectorAll('#comicsList .btn-delete').forEach(btn => {
        btn.addEventListener('click', () => deleteComic(btn.dataset.id));
    });
    document.querySelectorAll('#comicsList .btn-enable').forEach(btn => {
        btn.addEventListener('click', () => enableComic(btn.dataset.id));
    });
}

function openComicModal(comic = null) {
    const form = document.getElementById('form-comic');
    const title = document.getElementById('modalComicTitle');
    resetForm(form);

    if (comic) {
        title.textContent = 'Editar Cómic';
        setFormValues(form, {
            id: comic.idComic,
            seriesId: comic.seriesId,
            title: comic.title,
            volume: comic.volume,
            isbn: comic.isbn,
            publisherId: comic.publisherId,
            countryId: comic.countryId,
            formatId: comic.formatId,
            pageCount: comic.pageCount,
            releaseDate: comic.releaseDate,
            coverUrl: comic.coverUrl,
            genreIds: comic.genres ? comic.genres.map(g => String(g.idGenre)) : [],
        });
    } else {
        title.textContent = 'Nuevo Cómic';
    }

    openModal(MODAL_COMIC);
}

async function editComic(id) {
    try {
        const comic = await comicService.findById(id, true);
        openComicModal(comic);
    } catch (err) {
        alert(err.message);
    }
}

async function onSubmitComic(e) {
    e.preventDefault();
    const form = e.target;
    const values = getFormValues(form);
    const id = values.id;

    const body = {
        seriesId: toNumberOrNull(values.seriesId),
        title: values.title || null,
        volume: toNumberOrNull(values.volume),
        isbn: values.isbn || null,
        publisherId: toNumberOrNull(values.publisherId),
        countryId: toNumberOrNull(values.countryId),
        formatId: toNumberOrNull(values.formatId),
        pageCount: toNumberOrNull(values.pageCount),
        releaseDate: values.releaseDate || null,
        coverUrl: values.coverUrl || null,
        genreIds: toIdsOrNull(values.genreIds),
        authors: null,
    };

    try {
        if (id) {
            await comicService.update(id, body);
        } else {
            await comicService.create(body);
        }
        closeModal(MODAL_COMIC);
        await loadComics();
    } catch (err) {
        alert(err.message);
    }
}

async function deleteComic(id) {
    if (!confirm('¿Seguro que deseas eliminar este cómic?')) return;
    try {
        await comicService.remove(id);
        await loadComics();
    } catch (err) {
        alert(err.message);
    }
}

async function enableComic(id) {
    try {
        await comicService.enable(id);
        await loadComics();
    } catch (err) {
        alert(err.message);
    }
}
