// ==================================================
// MangaCo - Series (listado + CRUD)
// ==================================================

const MODAL_SERIES = 'modalSeries';

document.addEventListener('DOMContentLoaded', async () => {
    await initSeriesSelects();
    initSeriesEvents();
    await loadSeries();
});

async function initSeriesSelects() {
    try {
        const [categories, classifications] = await Promise.all([
            metadataService.categories.findAll({ enabled: true }),
            metadataService.classifications.findAll({ enabled: true }),
        ]);

        populateSelect(document.getElementById('categoryId'), categories, 'idCategory', 'nameCategory', '— Selecciona una categoría —');
        populateSelect(document.getElementById('classificationId'), classifications, 'idClassification', 'nameClassification', '— Selecciona una clasificación —');
    } catch (err) {
        console.error('No se pudieron cargar los catálogos de la serie', err);
    }
}

function initSeriesEvents() {
    document.getElementById('btnNewSeries').addEventListener('click', () => openSeriesModal());
    document.getElementById('form-series').addEventListener('submit', onSubmitSeries);
}

async function loadSeries() {
    const categoryId = getQueryParam('categoryId');
    const status = getQueryParam('status');

    try {
        const params = {};
        if (categoryId) params.categoryId = categoryId;
        if (status) params.status = status;

        const series = await seriesService.findAll(params);

        if (!series || series.length === 0) {
            renderEmpty('seriesList', 'No hay series todavía');
            return;
        }

        const container = document.getElementById('seriesList');
        container.innerHTML = series.map(renderSeriesCard).join('');
        bindSeriesActions();
    } catch (err) {
        renderError('seriesList', err.message);
    }
}

function renderSeriesCard(s) {
    return `
        <div class="card" data-id="${s.idSeries}">
            <a href="series.html?id=${s.idSeries}" class="card-link">
                <div class="card-cover">${coverImage(s.coverUrl, s.title, '📖')}</div>
            </a>
            <div class="card-body">
                <a href="series.html?id=${s.idSeries}">
                    <h3 class="card-title">${escapeHtml(s.title)}</h3>
                </a>
                <p class="card-subtitle">${escapeHtml(s.categoryName || 'Sin categoría')}</p>
                <div class="card-meta">
                    <span class="badge ${statusBadgeClass(s.status)}">${statusLabel(s.status)}</span>
                    <span class="badge">${s.totalVolumes || '?'} tomos</span>
                    <span class="badge ${s.enabled ? 'badge-success' : 'badge-danger'}">${s.enabled ? 'Activa' : 'Inactiva'}</span>
                </div>
                <div class="card-actions">
                    <button type="button" class="btn btn-warning btn-sm btn-edit" data-id="${s.idSeries}">✏️ Editar</button>
                    <button type="button" class="btn btn-danger btn-sm btn-delete" data-id="${s.idSeries}">🗑️ Eliminar</button>
                    ${!s.enabled ? `<button type="button" class="btn btn-primary btn-sm btn-enable" data-id="${s.idSeries}">✅ Habilitar</button>` : ''}
                </div>
            </div>
        </div>
    `;
}

function bindSeriesActions() {
    document.querySelectorAll('#seriesList .btn-edit').forEach(btn => {
        btn.addEventListener('click', () => editSeries(btn.dataset.id));
    });
    document.querySelectorAll('#seriesList .btn-delete').forEach(btn => {
        btn.addEventListener('click', () => deleteSeries(btn.dataset.id));
    });
    document.querySelectorAll('#seriesList .btn-enable').forEach(btn => {
        btn.addEventListener('click', () => enableSeries(btn.dataset.id));
    });
}

function openSeriesModal(series = null) {
    const form = document.getElementById('form-series');
    const title = document.getElementById('modalSeriesTitle');
    resetForm(form);

    if (series) {
        title.textContent = 'Editar Serie';
        setFormValues(form, {
            id: series.idSeries,
            title: series.title,
            originalTitle: series.originalTitle,
            categoryId: series.categoryId,
            classificationId: series.classificationId,
            totalVolumes: series.totalVolumes,
            status: series.status,
            startDate: series.startDate,
            endDate: series.endDate,
            coverUrl: series.coverUrl,
            synopsis: series.synopsis,
        });
    } else {
        title.textContent = 'Nueva Serie';
    }

    openModal(MODAL_SERIES);
}

async function editSeries(id) {
    try {
        const series = await seriesService.findById(id, true);
        openSeriesModal(series);
    } catch (err) {
        alert(err.message);
    }
}

async function onSubmitSeries(e) {
    e.preventDefault();
    const form = e.target;
    const values = getFormValues(form);
    const id = values.id;

    const body = {
        title: values.title,
        originalTitle: values.originalTitle || null,
        synopsis: values.synopsis || null,
        categoryId: toNumberOrNull(values.categoryId),
        classificationId: toNumberOrNull(values.classificationId),
        totalVolumes: toNumberOrNull(values.totalVolumes),
        status: values.status || null,
        coverUrl: values.coverUrl || null,
        startDate: values.startDate || null,
        endDate: values.endDate || null,
    };

    try {
        if (id) {
            await seriesService.update(id, body);
        } else {
            await seriesService.create(body);
        }
        closeModal(MODAL_SERIES);
        await loadSeries();
    } catch (err) {
        alert(err.message);
    }
}

async function deleteSeries(id) {
    if (!confirm('¿Seguro que deseas eliminar esta serie?')) return;
    try {
        await seriesService.remove(id);
        await loadSeries();
    } catch (err) {
        alert(err.message);
    }
}

async function enableSeries(id) {
    try {
        await seriesService.enable(id);
        await loadSeries();
    } catch (err) {
        alert(err.message);
    }
}
