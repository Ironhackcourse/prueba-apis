// ==================================================
// MangaCo - Utilidades de Autenticación (Global)
// ==================================================

// Esta función se llama al cargar CUALQUIER página
function renderNavbarAuth() {
    const authContainer = document.getElementById('authContainer');
    if (!authContainer) return;

    const token = localStorage.getItem('token');
    const username = localStorage.getItem('username');

    if (token && username) {
        // Si hay sesión, mostramos el nombre y el botón de salir
        authContainer.innerHTML = `
            <span class="user-greeting">Hola, ${username}</span>
            <button class="btn btn-secondary" onclick="logoutGlobal()">Cerrar Sesión</button>
        `;
    } else {
        // Si no hay sesión, mostramos el botón de login
        // Solo lo mostramos si la página tiene el modal
        if (document.getElementById('loginModal')) {
            authContainer.innerHTML = `
                <button class="btn btn-primary" onclick="window.openLoginModal()">Iniciar Sesión</button>
            `;
        } else {
            // Si estamos en comic-list.html (que no tiene modal), mandamos a index para loguearse
            authContainer.innerHTML = `
                <a href="index.html" class="btn btn-primary">Iniciar Sesión</a>
            `;
        }
    }
}

// Función global para cerrar sesión desde cualquier página
function logoutGlobal() {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    window.location.href = 'index.html'; // Volver al inicio
}

// Ejecutar automáticamente al cargar el DOM
document.addEventListener('DOMContentLoaded', () => {
    renderNavbarAuth();
});