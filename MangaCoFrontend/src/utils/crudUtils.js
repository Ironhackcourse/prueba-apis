// ==================================================
// MangaCo - Utilidades CRUD (modal, selects, formularios)
// ==================================================

function openModal(modalId) {
    const modal = document.getElementById(modalId);
    if (!modal) return;
    modal.classList.remove('hidden');
    document.body.style.overflow = 'hidden';

    document.querySelectorAll(`#${modalId} [data-close-modal]`).forEach(btn => {
        btn.onclick = () => closeModal(modalId);
    });

    modal.onclick = (e) => {
        if (e.target === modal) closeModal(modalId);
    };
}

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (!modal) return;
    modal.classList.add('hidden');
    document.body.style.overflow = '';
}

document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') {
        document.querySelectorAll('.modal:not(.hidden)').forEach(m => m.classList.add('hidden'));
        document.body.style.overflow = '';
    }
});

function populateSelect(selectEl, items, valueKey, labelKey, placeholder = '— Selecciona —') {
    if (!selectEl) return;
    const current = selectEl.value;
    const options = [`<option value="">${escapeHtml(placeholder)}</option>`];
    (items || []).forEach(item => {
        const value = item[valueKey];
        const label = item[labelKey];
        options.push(`<option value="${escapeHtml(value)}">${escapeHtml(label)}</option>`);
    });
    selectEl.innerHTML = options.join('');
    if (current) selectEl.value = current;
}

function setFormValues(form, data) {
    if (!form || !data) return;
    Object.entries(data).forEach(([key, value]) => {
        const field = form.elements[key];
        if (!field) return;
        if (field.type === 'checkbox') {
            field.checked = Boolean(value);
        } else if (field.multiple) {
            const values = Array.isArray(value) ? value.map(String) : [];
            Array.from(field.options).forEach(opt => {
                opt.selected = values.includes(opt.value);
            });
        } else {
            field.value = value ?? '';
        }
    });
}

function getFormValues(form) {
    const data = {};
    Array.from(form.elements).forEach(field => {
        if (!field.name || field.type === 'submit' || field.type === 'button') return;
        if (field.type === 'checkbox') {
            data[field.name] = field.checked;
        } else if (field.multiple) {
            data[field.name] = Array.from(field.selectedOptions).map(opt => opt.value);
        } else if (field.value !== '') {
            data[field.name] = field.value;
        } else {
            data[field.name] = null;
        }
    });
    return data;
}

function resetForm(form) {
    if (!form) return;
    form.reset();
    const hidden = form.querySelector('input[type="hidden"]');
    if (hidden) hidden.value = '';
}

function toNumberOrNull(value) {
    if (value === null || value === undefined || value === '') return null;
    const n = Number(value);
    return Number.isNaN(n) ? null : n;
}

function toIdsOrNull(values) {
    if (!values) return null;
    const arr = Array.isArray(values) ? values : [values];
    const ids = arr.map(v => Number(v)).filter(n => !Number.isNaN(n));
    return ids.length > 0 ? ids : null;
}
