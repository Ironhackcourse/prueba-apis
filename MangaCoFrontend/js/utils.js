// ==================================================
// MangaCo - Utilidades
// ==================================================

function formatDate(isoDate) {
    if (!isoDate) return '—';
    try {
        return new Date(isoDate).toLocaleDateString('es-ES', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
        });
    } catch {
        return isoDate;
    }
}

function statusLabel(status) {
    const map = {
        ONGOING: 'En curso',
        COMPLETED: 'Finalizada',
        CANCELLED: 'Cancelada',
        HIATUS: 'En pausa',
        ONESHOT: 'Tomo único',
    };
    return map[status] || status || '—';
}

function statusBadgeClass(status) {
    const map = {
        ONGOING: 'badge-success',
        COMPLETED: 'badge-info',
        CANCELLED: 'badge-danger',
        HIATUS: 'badge-warning',
        ONESHOT: 'badge-primary',
    };
    return map[status] || 'badge';
}

function authorRoleLabel(role) {
    const map = {
        writer: 'Guionista',
        artist: 'Dibujante',
        both: 'Guionista y dibujante',
        assistant: 'Asistente',
    };
    return map[role] || role || '';
}

function getQueryParam(name) {
    return new URLSearchParams(window.location.search).get(name);
}

function debounce(fn, delay = 300) {
    let timeoutId;
    return (...args) => {
        clearTimeout(timeoutId);
        timeoutId = setTimeout(() => fn(...args), delay);
    };
}

function escapeHtml(str) {
    if (str === null || str === undefined) return '';
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#039;');
}

function showLoading() {
    const el = document.getElementById('loading');
    if (el) el.classList.remove('hidden');
}

function hideLoading() {
    const el = document.getElementById('loading');
    if (el) el.classList.add('hidden');
}

function renderError(containerId, message) {
    const container = document.getElementById(containerId);
    if (!container) return;
    container.innerHTML = `
        <div class="error-box">
            <p>⚠️ ${escapeHtml(message)}</p>
        </div>
    `;
}

function renderEmpty(containerId, message = 'No hay resultados') {
    const container = document.getElementById(containerId);
    if (!container) return;
    container.innerHTML = `
        <div class="empty-state">
            <h3>${escapeHtml(message)}</h3>
            <p>Intenta con otros filtros o vuelve más tarde.</p>
        </div>
    `;
}