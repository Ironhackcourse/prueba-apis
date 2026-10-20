// ==================================================
// MangaCo - Editoriales (listado + CRUD)
// ==================================================

const MODAL_PUBLISHER = 'modalPublisher';

document.addEventListener('DOMContentLoaded', async () => {
    await initPublisherSelects();
    initPublisherEvents();
    await loadPublishers();
});

async function initPublisherSelects() {
    try {
        const countries = await metadataService.countries.findAll({ enabled: true });
        populateSelect(document.getElementById('countryId'), countries, 'idCountry', 'nameCountry', '— Selecciona un país —');
    } catch (err) {
        console.error('No se pudieron cargar los países', err);
    }
}

function initPublisherEvents() {
    document.getElementById('btnNewPublisher').addEventListener('click', () => openPublisherModal());
    document.getElementById('form-publisher').addEventListener('submit', onSubmitPublisher);
}

async function loadPublishers() {
    const countryId = getQueryParam('countryId');
    const container = document.getElementById('publishersList');

    try {
        const publishers = countryId
            ? await publisherService.findByCountry(countryId)
            : await publisherService.findAll();

        if (!publishers || publishers.length === 0) {
            renderEmpty('publishersList', 'No hay editoriales todavía');
            return;
        }

        container.innerHTML = publishers.map(renderPublisherCard).join('');
        bindPublisherActions();
    } catch (err) {
        renderError('publishersList', err.message);
    }
}

function renderPublisherCard(p) {
    return `
        <div class="card" data-id="${p.idPublisher}">
            <div class="card-cover">🏢</div>
            <div class="card-body">
                <h3 class="card-title">${escapeHtml(p.namePublisher)}</h3>
                <p class="card-subtitle">${escapeHtml(p.countryName || 'Sin país')}</p>
                <div class="card-meta">
                    ${p.website ? `<a class="badge badge-primary" href="${escapeHtml(p.website)}" target="_blank" rel="noopener">Sitio web</a>` : ''}
                    <span class="badge ${p.enabled ? 'badge-success' : 'badge-danger'}">${p.enabled ? 'Activa' : 'Inactiva'}</span>
                </div>
                <div class="card-actions">
                    <button type="button" class="btn btn-warning btn-sm btn-edit" data-id="${p.idPublisher}">✏️ Editar</button>
                    <button type="button" class="btn btn-danger btn-sm btn-delete" data-id="${p.idPublisher}">🗑️ Eliminar</button>
                    ${!p.enabled ? `<button type="button" class="btn btn-primary btn-sm btn-enable" data-id="${p.idPublisher}">✅ Habilitar</button>` : ''}
                </div>
            </div>
        </div>
    `;
}

function bindPublisherActions() {
    document.querySelectorAll('#publishersList .btn-edit').forEach(btn => {
        btn.addEventListener('click', () => editPublisher(btn.dataset.id));
    });
    document.querySelectorAll('#publishersList .btn-delete').forEach(btn => {
        btn.addEventListener('click', () => deletePublisher(btn.dataset.id));
    });
    document.querySelectorAll('#publishersList .btn-enable').forEach(btn => {
        btn.addEventListener('click', () => enablePublisher(btn.dataset.id));
    });
}

function openPublisherModal(publisher = null) {
    const form = document.getElementById('form-publisher');
    const title = document.getElementById('modalPublisherTitle');
    resetForm(form);

    if (publisher) {
        title.textContent = 'Editar Editorial';
        setFormValues(form, publisher);
    } else {
        title.textContent = 'Nueva Editorial';
    }

    openModal(MODAL_PUBLISHER);
}

async function editPublisher(id) {
    try {
        const publisher = await publisherService.findById(id, true);
        openPublisherModal(publisher);
    } catch (err) {
        alert(err.message);
    }
}

async function onSubmitPublisher(e) {
    e.preventDefault();
    const form = e.target;
    const values = getFormValues(form);
    const id = values.id;
    delete values.id;

    const body = {
        namePublisher: values.namePublisher,
        countryId: toNumberOrNull(values.countryId),
        website: values.website || null,
    };

    try {
        if (id) {
            await publisherService.update(id, body);
        } else {
            await publisherService.create(body);
        }
        closeModal(MODAL_PUBLISHER);
        await loadPublishers();
    } catch (err) {
        alert(err.message);
    }
}

async function deletePublisher(id) {
    if (!confirm('¿Seguro que deseas eliminar esta editorial?')) return;
    try {
        await publisherService.remove(id);
        await loadPublishers();
    } catch (err) {
        alert(err.message);
    }
}

async function enablePublisher(id) {
    try {
        await publisherService.enable(id);
        await loadPublishers();
    } catch (err) {
        alert(err.message);
    }
}

