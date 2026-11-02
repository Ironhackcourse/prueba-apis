// ==================================================
// MangaCo - Controlador de Login (Modal)
// ==================================================

document.addEventListener('DOMContentLoaded', () => {
    const modal = document.getElementById('loginModal');
    const loginForm = document.getElementById('loginForm');
    const errorMsg = document.getElementById('loginError');
    const closeBtn = document.getElementById('closeLoginModal');
    
    // Si no existe el modal en esta página, no hacemos nada
    if (!modal) return;

    // Abrir modal
    window.openLoginModal = () => {
        modal.style.display = 'flex';
    };

    // Cerrar modal
    closeBtn.addEventListener('click', () => {
        modal.style.display = 'none';
        errorMsg.style.display = 'none';
    });

    // Enviar formulario
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        try {
            await authService.login(username, password);
            modal.style.display = 'none';
            
            // En lugar de updateNavbar(), recargamos la página para que el util global pinte todo
            window.location.reload(); 
            
        } catch (error) {
            errorMsg.textContent = error.message;
            errorMsg.style.display = 'block';
        }
    });
});