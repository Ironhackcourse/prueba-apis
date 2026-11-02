// ==================================================
// MangaCo - Servicio de Autenticación
// ==================================================

const API_URL = 'http://localhost:8080/api/v1';

const authService = {
    login: async (username, password) => {
        const response = await fetch(`${API_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });

        if (!response.ok) {
            throw new Error('Usuario o contraseña incorrectos');
        }

        const data = await response.json();
        localStorage.setItem('token', data.token);
        localStorage.setItem('username', data.username);
        return data;
    },

    logout: () => {
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        window.location.reload();
    },

    isLoggedIn: () => {
        return localStorage.getItem('token') !== null;
    },

    getUsername: () => {
        return localStorage.getItem('username');
    }
};