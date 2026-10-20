// ==================================================
// MangaCo - Autores (listado + CRUD)
// ==================================================

const MODAL_AUTHOR = 'modalAuthor';

document.addEventListener('DOMContentLoaded', async () => {
    await initAuthorSelects();
    initAuthorEvents();
    await loadAuthors();
});

async function initAuthorSelects() {
    try {
        const countries = await metadataService.countries.findAll({ enabled: true });
        populateSelect(document.getElementById('countryId'), countries, 'idCountry', 'nameCountry', '— Selecciona un país —');
    } catch (err) {
        console.error('No se pudieron cargar los países', err);
    }
}

function initAuthorEvents() {
    document.getElementById('btnNewAuthor').addEventListener('click', () => openAuthorModal());
    document.getElementById('form-author').addEventListener('submit', onSubmitAuthor);
}

async function loadAuthors() {
    const countryId = getQueryParam('countryId');

    try {
        const authors = countryId
            ? await authorService.findByCountry(countryId)
            : await authorService.findAll();

        if (!authors || authors.length === 0) {
            renderEmpty('authorsList', 'No hay autores todavía');
            return;
        }

        const container = document.getElementById('authorsList');
        container.innerHTML = authors.map(renderAuthorCard).join('');
        bindAuthorActions();
    } catch (err) {
        renderError('authorsList', err.message);
    }
}

function renderAuthorCard(a) {
    const fullName = `${escapeHtml(a.nameAuthor)} ${escapeHtml(a.lastnameAuthor)}`;
    return `
        <div class="card" data-id="${a.idAuthor}">
            <a href="author.html?id=${a.idAuthor}" class="card-link">
                <div class="card-cover">${coverImage(a.photoUrl, `${a.nameAuthor} ${a.lastnameAuthor}`, '✍️')}</div>
            </a>
            <div class="card-body">
                <a href="author.html?id=${a.idAuthor}">
                    <h3 class="card-title">${fullName}</h3>
                </a>
                <p class="card-subtitle">${escapeHtml(a.countryName || 'Sin país')}</p>
                <div class="card-meta">
                    ${a.penName ? `<span class="badge">${escapeHtml(a.penName)}</span>` : ''}
                    <span class="badge ${a.enabled ? 'badge-success' : 'badge-danger'}">${a.enabled ? 'Activo' : 'Inactivo'}</span>
                </div>
                <div class="card-actions">
                    <button type="button" class="btn btn-warning btn-sm btn-edit" data-id="${a.idAuthor}">✏️ Editar</button>
                    <button type="button" class="btn btn-danger btn-sm btn-delete" data-id="${a.idAuthor}">🗑️ Eliminar</button>
                    ${!a.enabled ? `<button type="button" class="btn btn-primary btn-sm btn-enable" data-id="${a.idAuthor}">✅ Habilitar</button>` : ''}
                </div>
            </div>
        </div>
    `;
}

function bindAuthorActions() {
    document.querySelectorAll('#authorsList .btn-edit').forEach(btn => {
        btn.addEventListener('click', () => editAuthor(btn.dataset.id));
    });
    document.querySelectorAll('#authorsList .btn-delete').forEach(btn => {
        btn.addEventListener('click', () => deleteAuthor(btn.dataset.id));
    });
    document.querySelectorAll('#authorsList .btn-enable').forEach(btn => {
        btn.addEventListener('click', () => enableAuthor(btn.dataset.id));
    });
}

function openAuthorModal(author = null) {
    const form = document.getElementById('form-author');
    const title = document.getElementById('modalAuthorTitle');
    resetForm(form);

    if (author) {
        title.textContent = 'Editar Autor';
        setFormValues(form, {
            id: author.idAuthor,
            nameAuthor: author.nameAuthor,
            lastnameAuthor: author.lastnameAuthor,
            penName: author.penName,
            birthDate: author.birthDate,
            countryId: author.countryId,
            biography: author.biography,
            photoUrl: author.photoUrl,
        });
    } else {
        title.textContent = 'Nuevo Autor';
    }

    openModal(MODAL_AUTHOR);
}

async function editAuthor(id) {
    try {
        const author = await authorService.findById(id, true);
        openAuthorModal(author);
    } catch (err) {
        alert(err.message);
    }
}

async function onSubmitAuthor(e) {
    e.preventDefault();
    const form = e.target;
    const values = getFormValues(form);
    const id = values.id;

    const body = {
        nameAuthor: values.nameAuthor,
        lastnameAuthor: values.lastnameAuthor,
        penName: values.penName || null,
        birthDate: values.birthDate || null,
        countryId: toNumberOrNull(values.countryId),
        biography: values.biography || null,
        photoUrl: values.photoUrl || null,
    };

    try {
        if (id) {
            await authorService.update(id, body);
        } else {
            await authorService.create(body);
        }
        closeModal(MODAL_AUTHOR);
        await loadAuthors();
    } catch (err) {
        alert(err.message);
    }
}

async function deleteAuthor(id) {
    if (!confirm('¿Seguro que deseas eliminar este autor?')) return;
    try {
        await authorService.remove(id);
        await loadAuthors();
    } catch (err) {
        alert(err.message);
    }
}

async function enableAuthor(id) {
    try {
        await authorService.enable(id);
        await loadAuthors();
    } catch (err) {
        alert(err.message);
    }
}
