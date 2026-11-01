# MangaCoBackend

Sistema de gestión de usuarios con autenticación JWT, roles (ADMIN/USER) y soft delete.
Backend desarrollado con **Spring Boot 3** y **Spring Security**. El frontend usa **HTML y JavaScript vanilla** (sin frameworks).

---

## 📋 Tabla de contenidos

- [Tecnologías](#tecnologías)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Cómo ejecutar el proyecto](#cómo-ejecutar-el-proyecto)
- [Endpoints de autenticación (públicos)](#endpoints-de-autenticación-públicos)
- [Endpoints de gestión de usuarios (protegidos)](#endpoints-de-gestión-de-usuario-protegidos)
- [Ejercicio paso a paso: Login y registro](#ejercicio-paso-a-paso-login-y-registro)
- [Cómo probar desde el frontend (HTML/JS vanilla)](#cómo-probar-desde-el-frontend-htmljs-vanilla)
- [Reglas de negocio](#reglas-de-negocio)
- [Documentación de los endpoints](#documentación-de-los-endpoints)
- [CORS](#cors)

---

## 🛠 Tecnologías

| Tecnología | Versión | Uso |
|------------|---------|-----|
| Java | 17+ | Lenguaje de programación |
| Spring Boot | 3.x | Framework backend |
| Spring Security | 6.x | Autenticación y autorización |
| Spring Data JPA | 3.x | Acceso a datos (Hibernate) |
| MariaDB / MySQL | - | Base de datos relacional |
| JWT (JJWT) | - | Tokens de acceso stateless |
| BCrypt | - | Encriptado de contraseñas |
| Lombok | - | Reducción de código boilerplate |

---

## 📁 Estructura del proyecto

```
prueba-apis-main/
├── MangaCoBackend/
│   ├── pom.xml
│   ├── src/
│   │   └── main/java/com/xavier/MangaCoBackend/
│   │       ├── config/
│   │       │   ├── SecurityConfig.java       # Configuración de seguridad y reglas @PreAuthorize
│   │       │   ├── CorsConfig.java            # Configuración CORS (WebMvcConfigurer)
│   │       │   └── JwtAuthenticationFilter.java  # Filtro de validación JWT
│   │       ├── controller/
│   │       │   ├── AuthController.java        # Login y registro público
│   │       │   └── UserController.java        # Gestión de usuarios (ADMIN / propio perfil)
│   │       ├── dto/
│   │       │   ├── request/
│   │       │   │   ├── LoginRequest.java
│   │       │   │   ├── RegisterRequest.java
│   │       │   │   └── ChangeRoleRequest.java
│   │       │   └── response/
│   │       │       ├── AuthResponse.java
│   │       │       └── UserResponse.java
│   │       ├── entity/
│   │       │   ├── User.java                  # Implementa UserDetails
│   │       │   └── Role.java
│   │       ├── exception/
│   │       │   ├── DuplicateResourceException.java
│   │       │   └── ResourceNotFoundException.java
│   │       ├── repository/
│   │       │   ├── UserRepository.java
│   │       │   └── RoleRepository.java
│   │       ├── services/
│   │       │   ├── UserService.java           # Interfaz del servicio
│   │       │   └── impl/
│   │       │       └── UserServiceImpl.java   # Implementación del servicio
│   │       └── MangaCoBackendApplication.java
│   └── target/                              # Archivos compilados (no subir al repositorio)
├── API_CONTEXT.md                           # Documentación técnica detallada de la API
└── README.md                                # Este archivo
```

---

## 🚀 Cómo ejecutar el proyecto

### Requisitos previos

- Java 17 o superior
- Maven 3.8+
- Base de datos MariaDB/MySQL corriendo

### Base de datos

Asegúrate de tener estas tablas y datos mínimos en tu base de datos:

```sql
-- Tabla de roles
INSERT INTO role (id_role, name_role) VALUES (1, 'ADMIN');
INSERT INTO role (id_role, name_role) VALUES (2, 'USER');

-- Usuario admin (password: admin123 encriptado con BCrypt)
INSERT INTO user (username, email, password, role_id, enabled)
VALUES ('admin', 'admin@mangaco.com', '$2a$10$NzLJlCvF/VzQwh73BfNQDeY8XoLe6Wd5RxYdPCsI5Ls1208wGtr3W', 1, true);
```

### Ejecutar la aplicación

Desde la carpeta `MangaCoBackend`:

```bash
./mvnw spring-boot:run
```

La aplicación arrancará en: `http://localhost:8080`

---

## 🔐 Endpoints de autenticación (públicos)

Estos endpoints no requieren token previo.

### POST /api/v1/auth/login
Autentica un usuario y devuelve un JWT válido por 24 horas.

**Request:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "admin"
}
```

### POST /api/v1/auth/register
Registro público. Cualquier persona puede registrarse. El rol se fuerza siempre a **USER (id=2)**.

**Request:**
```json
{
  "username": "juanperez",
  "email": "juan@example.com",
  "password": "juan1234"
}
```

**Response (201 Created):**
```json
{
  "id": 3,
  "username": "juanperez",
  "email": "juan@example.com",
  "roleName": "USER",
  "enabled": true
}
```

---

## 👥 Endpoints de gestión de usuarios (protegidos)

Todos los endpoints bajo `/api/v1/users` requieren autenticación excepto los indicados.

| Endpoint | Método | ¿Quién puede acceder? | Descripción |
|----------|--------|----------------------|-------------|
| `/api/v1/auth/register` | POST | Público | Registrar usuario como USER |
| `/api/v1/auth/login` | POST | Público | Obtener token JWT |
| `/api/v1/users` | GET | Solo ADMIN | Listar todos los usuarios (sin passwords) |
| `/api/v1/users` | POST | Solo ADMIN | Crear usuario con rol elegido por el admin |
| `/api/v1/users/me` | GET | Cualquier autenticado | Ver perfil propio |
| `/api/v1/users/{id}` | PUT | ADMIN o dueño del perfil | Editar username/email/password (nunca el rol) |
| `/api/v1/users/{id}/role` | PUT | Solo ADMIN | Cambiar el rol de un usuario |
| `/api/v1/users/{id}` | DELETE | Solo ADMIN | Soft delete (enabled = false) |

**Cómo enviar el token en peticiones protegidas:**

```
Authorization: Bearer <TU_TOKEN_JWT>
```

---

## 📝 Ejercicio paso a paso: Login y registro

Sigue estos pasos para probar el flujo completo con `curl` o Postman.

### Paso 1: Registrar un usuario normal (público)

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "juan",
    "email": "juan@test.com",
    "password": "juan1234"
  }'
```

> El `roleId` se ignora en el registro público; siempre se asigna USER (id=2).

### Paso 2: Loguearse como admin (obtener token)

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

**Respuesta esperada:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc5MzI3MTIzNCwiZXhwIjoxNzkzMzU3NjM0fQ.Cogb4BVqUelFzfh6K9Nrh5VpKO_uue10fxXkZLBMCNM",
  "username": "admin"
}
```

> **Copia el valor de `token`**. Lo necesitarás para los siguientes pasos.

### Paso 3: Ver perfil propio como admin

```bash
curl -X GET http://localhost:8080/api/v1/users/me \
  -H "Authorization: Bearer <TOKEN_DEL_PASO_2>"
```

### Paso 4: Crear un usuario ADMIN (solo admin)

```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{
    "username": "superadmin",
    "email": "super@test.com",
    "password": "super1234",
    "roleId": 1
  }'
```

### Paso 5: Cambiar el rol de un usuario (solo admin)

Sustituye `<ID_DE_USUARIO>` por el ID del usuario registrado (ej. `3`):

```bash
curl -X PUT http://localhost:8080/api/v1/users/<ID_DE_USUARIO>/role \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{
    "roleId": 1
  }'
```

> `roleId: 1` = ADMIN, `roleId: 2` = USER

### Paso 6: Un usuario normal edita su propio perfil

Primero logueate como `juan` para obtener su token:

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "juan",
    "password": "juan1234"
  }'
```

Luego edita su propio perfil:

```bash
curl -X PUT http://localhost:8080/api/v1/users/3 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN_DE_JUAN>" \
  -d '{
    "username": "juan_modificado",
    "email": "juan_nuevo@test.com",
    "password": "nueva1234"
  }'
```

---

## 🌐 Cómo probar desde el frontend (HTML/JS vanilla)

El proyecto frontend usa **HTML y JavaScript puro** (sin React, Vue, Angular, etc.).

### Ejemplo de login con fetch

```html
<!-- index.html -->
<form id="loginForm">
  <input type="text" id="username" placeholder="Usuario" required>
  <input type="password" id="password" placeholder="Contraseña" required>
  <button type="submit">Iniciar sesión</button>
</form>
<div id="mensaje"></div>

<script>
document.getElementById('loginForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  
  const username = document.getElementById('username').value;
  const password = document.getElementById('password').value;
  
  try {
    const respuesta = await fetch('http://localhost:8080/api/v1/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password })
    });
    
    if (!respuesta.ok) throw new Error('Credenciales incorrectas');
    
    const datos = await respuesta.json();
    
    // Guardar el token en localStorage
    localStorage.setItem('token', datos.token);
    localStorage.setItem('username', datos.username);
    
    document.getElementById('mensaje').textContent = 'Bienvenido, ' + datos.username;
    
    // Redirigir o mostrar contenido protegido
    window.location.href = 'perfil.html';
    
  } catch (error) {
    document.getElementById('mensaje').textContent = error.message;
  }
});
</script>
```

### Ejemplo de petición protegida con token

```javascript
async function obtenerPerfil() {
  const token = localStorage.getItem('token');
  
  const respuesta = await fetch('http://localhost:8080/api/v1/users/me', {
    method: 'GET',
    headers: {
      'Authorization': 'Bearer ' + token
    }
  });
  
  if (respuesta.ok) {
    const perfil = await respuesta.json();
    console.log('Perfil:', perfil);
  } else {
    console.error('Error al obtener perfil');
  }
}
```

### Ejemplo de registro desde el frontend

```javascript
async function registrarUsuario(username, email, password) {
  const respuesta = await fetch('http://localhost:8080/api/v1/auth/register', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, email, password })
  });
  
  if (respuesta.ok) {
    const usuario = await respuesta.json();
    console.log('Usuario registrado:', usuario);
  } else {
    console.error('Error al registrar');
  }
}
```

---

## 📖 Documentación de los endpoints

### Documentación JSDoc (recursos de manga)
Generada con Node.js + JSDoc. Cubre: autores, cómics, series, editoriales, categorías, clasificaciones, formatos, géneros, países, roles.

**Ruta:** `MangaCoBackend/docs/api/index.html`

Para regenerarla:
```bash
cd MangaCoBackend/docs
npm install
npm run docs
```

### Documentación de autenticación y gestión de usuarios
Los endpoints de `/api/v1/auth/*` (login, registro) y `/api/v1/users/*` (usuarios, roles, perfil propio) están documentados en:

**`API_CONTEXT.md`** (raíz del proyecto) — incluye flujo interno del login, reglas de negocio, DTOs y pruebas con curl.

También están documentados en este README en las secciones:
- [Endpoints de autenticación (públicos)](#endpoints-de-autenticación-públicos)
- [Endpoints de gestión de usuarios (protegidos)](#endpoints-de-gestión-de-usuario-protegidos)
- [Ejercicio paso a paso: Login y registro](#ejercicio-paso-a-paso-login-y-registro)

---

## 🌍 CORS

El CORS está configurado en:

**`MangaCoBackend/src/main/java/com/xavier/MangaCoBackend/config/CorsConfig.java`**

Orígenes permitidos actualmente:
- `http://localhost:5500` y `http://127.0.0.1:5500`
- `http://localhost:5173` y `http://127.0.0.1:5173`
- `http://localhost:3000` y `http://127.0.0.1:3000`

Si tu frontend corre en otro puerto, agrégalo a `allowedOrigins` en `CorsConfig.java`.

Métodos permitidos: `GET, POST, PUT, PATCH, DELETE, OPTIONS`

---

## 📌 Reglas de negocio resumidas

| Regla | Descripción |
|-------|-------------|
| Registro público | Rol forzado a USER (id=2), se ignora roleId del cliente |
| Registro por ADMIN | El admin puede elegir el rol (USER o ADMIN) |
| Actualizar perfil propio | USER puede editar su propio username/email/password |
| Actualizar cualquier usuario | Solo ADMIN puede editar cualquier usuario |
| Nunca se cambia el rol en actualización | El rol solo se cambia con `PUT /{id}/role` (solo ADMIN) |
| Eliminar usuario | Soft delete (`enabled = false`), no borra la fila |
| Password | Nunca se devuelve en las respuestas. Se encripta con BCrypt |
| Lectura (GET) | Pública, con o sin registro/login |
| Crear usuario | Solo ADMIN |

---

## 📦 ¿Qué subir al repositorio?

Subir:
```
src/
pom.xml
README.md
API_CONTEXT.md
```

No subir:
```
target/
.classpath
.project
.settings/
*.iml
.idea/
```

---

## ✅ Estado del proyecto

- Compilación: `./mvnw compile` → Éxito
- Tests: `./mvnw test` → BUILD SUCCESS
- Login: Funcionando (obtiene token JWT)
- Registro: Funcionando (usuario público como USER)
- CORS: Configurado en `CorsConfig.java`
- Gestión de usuarios: ADMIN y propio perfil funcionando
