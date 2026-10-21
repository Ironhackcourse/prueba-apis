#!/usr/bin/env node
/**
 * @file Generador de referencias de API para el backend MangaCo.
 *
 * Lee los controladores REST de Spring Boot ubicados en
 * `src/main/java/com/xavier/MangaCoBackend/controller/`, extrae la ruta
 * base (`@RequestMapping`), el verbo HTTP y la ruta de cada endpoint
 * (`@GetMapping`, `@PostMapping`, ...), y genera el archivo
 * `docs/api-references.js` con bloques JSDoc listos para ser procesados
 * por el generador de documentación estándar.
 *
 * @author MangaCo Team
 * @since 2026-10-21
 * @version 1.0.0
 * @tutorial introduccion
 */

'use strict';

const fs = require('fs');
const path = require('path');

/** @type {string} Directorio raíz del backend (un nivel por encima de docs/). */
const BACKEND_ROOT = path.resolve(__dirname, '..');

/** @type {string} Directorio que contiene los controladores REST. */
const CONTROLLERS_DIR = path.join(
    BACKEND_ROOT,
    'src',
    'main',
    'java',
    'com',
    'xavier',
    'MangaCoBackend',
    'controller'
);

/** @type {string} Ruta del archivo JS generado con las referencias de la API. */
const OUTPUT_FILE = path.join(__dirname, 'api-references.js');

/**
 * Mapa entre la anotación de mapeo de Spring y el verbo HTTP asociado.
 *
 * @constant {Readonly<Object<string, string>>}
 */
const HTTP_MAPPINGS = Object.freeze({
    GetMapping: 'GET',
    PostMapping: 'POST',
    PutMapping: 'PUT',
    PatchMapping: 'PATCH',
    DeleteMapping: 'DELETE',
});

/**
 * Convierte el nombre de una clase Java en un nombre de módulo JSDoc
 * en camelCase (p. ej. `AuthorController` -> `authorController`).
 *
 * @param {string} className - Nombre de la clase del controlador.
 * @returns {string} Nombre de módulo en camelCase.
 */
function toModuleName(className) {
    return className.charAt(0).toLowerCase() + className.slice(1);
}

/**
 * Convierte una ruta de Spring (`/country/{countryId}`) en un estilo de
 * documento de API de Node (`/country/:countryId`).
 *
 * @param {string} route - Ruta con placeholders entre llaves.
 * @returns {string} Ruta con parámetros en formato `:param`.
 */
function toApiRoute(route) {
    const cleaned = route === '/' ? '' : route;
    return cleaned.replace(/\{(\w+)\}/g, ':$1');
}

/**
 * Divide la lista de parámetros de un método Java respetando los paréntesis
 * y las comillas, de modo que las comas internas de una anotación no
 * separen parámetros distintos.
 *
 * @param {string} raw - Texto crudo de la lista de parámetros.
 * @returns {string[]} Lista de parámetros individuales.
 */
function splitParameters(raw) {
    const parts = [];
    let depth = 0;
    let inString = false;
    let current = '';

    for (let i = 0; i < raw.length; i += 1) {
        const ch = raw[i];
        if (inString) {
            current += ch;
            if (ch === '"') inString = false;
            continue;
        }
        if (ch === '"') {
            inString = true;
            current += ch;
            continue;
        }
        if (ch === '(') depth += 1;
        if (ch === ')') depth -= 1;
        if (ch === ',' && depth === 0) {
            parts.push(current.trim());
            current = '';
            continue;
        }
        current += ch;
    }
    if (current.trim()) parts.push(current.trim());
    return parts;
}

/**
 * Obtiene el nombre de la variable Java al final de un parámetro,
 * descartando modificadores y anotaciones previas.
 *
 * @param {string} param - Parámetro Java completo (p. ej.
 *   `@RequestParam(required = false) Boolean enabled`).
 * @returns {string} Nombre de la variable (`enabled`).
 */
function extractVariableName(param) {
    const match = param.match(/(\w+)\s*$/);
    return match ? match[1] : '';
}

/**
 * Analiza los parámetros de un método y clasifica cada uno como
 * parámetro de ruta (`@PathVariable`), de consulta (`@RequestParam`)
 * o cuerpo de la petición (`@RequestBody`).
 *
 * @param {string} paramsRaw - Texto crudo de la lista de parámetros.
 * @returns {{pathParams: string[], queryParams: string[], hasBody: boolean}}
 *   Parámetros de ruta (prefijados con `:`), parámetros de consulta y si
 *   el método recibe un `@RequestBody`.
 */
function parseParameters(paramsRaw) {
    const pathParams = [];
    const queryParams = [];
    let hasBody = false;

    splitParameters(paramsRaw).forEach((param) => {
        const name = extractVariableName(param);
        if (!name) return;
        if (/@PathVariable\b/.test(param)) {
            pathParams.push(`:${name}`);
        } else if (/@RequestParam\b/.test(param)) {
            queryParams.push(name);
        } else if (/@RequestBody\b/.test(param)) {
            hasBody = true;
        }
    });

    return { pathParams, queryParams, hasBody };
}

/**
 * Extrae los metadatos de un controlador a partir de su código fuente.
 *
 * @param {string} source - Contenido completo del archivo `.java`.
 * @returns {{className: string, basePath: string, endpoints: Array<Object>}|null}
 *   Objeto con el nombre de la clase, la ruta base y la lista de endpoints;
 *   `null` si no se puede determinar la clase o la ruta base.
 */
function parseController(source) {
    const classMatch = source.match(/(?:public\s+)?class\s+(\w+)/);
    const baseMatch = source.match(/@RequestMapping\(\s*"([^"]+)"\s*\)/);
    if (!classMatch || !baseMatch) return null;

    const className = classMatch[1];
    const basePath = baseMatch[1];
    const endpoints = [];

    const methodRegex = new RegExp(
        '@(' + Object.keys(HTTP_MAPPINGS).join('|') + ')' +
            '(?:\\((?:\\s*(?:value\\s*=\\s*)?)"([^"]*)"\\s*\\))?' +
            '\\s*' +
            'public\\s+[\\w<>,.\\s\\[\\]]+?\\s+(\\w+)\\s*\\(([\\s\\S]*?)\\)\\s*\\{',
        'g'
    );

    let match;
    while ((match = methodRegex.exec(source)) !== null) {
        const [, annotation, subPath = '', methodName, paramsRaw = ''] = match;
        const httpMethod = HTTP_MAPPINGS[annotation];
        const fullPath = (basePath + toApiRoute(subPath)).replace(/\/+$/, '') || basePath;

        const { pathParams, queryParams, hasBody } = parseParameters(paramsRaw);

        endpoints.push({
            httpMethod,
            path: fullPath,
            pathParams,
            queryParams,
            hasBody,
            methodName,
        });
    }

    return { className, basePath, endpoints, moduleName: toModuleName(className) };
}

/**
 * Palabras reservadas de JavaScript que no pueden usarse como nombre de
 * variable en el archivo generado.
 *
 * @constant {Readonly<string[]>}
 */
const RESERVED_WORDS = Object.freeze([
    'break', 'case', 'catch', 'class', 'const', 'continue', 'debugger',
    'default', 'delete', 'do', 'else', 'export', 'extends', 'finally',
    'for', 'function', 'if', 'import', 'in', 'instanceof', 'new', 'return',
    'super', 'switch', 'this', 'throw', 'try', 'typeof', 'var', 'void',
    'while', 'with', 'yield', 'let', 'static', 'await',
]);

/**
 * Convierte un nombre de método Java en un identificador JavaScript válido,
 * evitando palabras reservadas.
 *
 * @param {string} name - Nombre del método (p. ej. `delete`).
 * @returns {string} Identificador seguro (p. ej. `delete_`).
 */
function sanitizeIdentifier(name) {
    return RESERVED_WORDS.includes(name) ? `${name}_` : name;
}

/**
 * Genera un bloque JSDoc documentando un único endpoint REST.
 *
 * @param {Object} endpoint - Metadatos del endpoint.
 * @param {string} className - Nombre de la clase controladora.
 * @returns {string} Bloque de comentario JSDoc listo para insertar.
 */
function buildEndpointDoc(endpoint, className) {
    const lines = [];
    lines.push('/**');
    lines.push(` * **\`${endpoint.httpMethod} ${endpoint.path}\`**`);
    lines.push(' *');
    lines.push(` * Controlador: \`${className}\`. Método Java: \`${endpoint.methodName}\`.`);
    lines.push(' *');
    lines.push(` * @function ${endpoint.methodName}`);

    if (endpoint.pathParams.length > 0) {
        lines.push(' *');
        endpoint.pathParams.forEach((p) => {
            lines.push(` * @param {string} ${p.slice(1)} - Parámetro de ruta.`);
        });
    }
    if (endpoint.queryParams.length > 0) {
        lines.push(' *');
        endpoint.queryParams.forEach((q) => {
            lines.push(` * @param {string} [${q}] - Parámetro de consulta (query string).`);
        });
    }
    if (endpoint.hasBody) {
        lines.push(' * @param {Object} body - Cuerpo de la petición en formato JSON.');
    }

    lines.push(' * @returns {Promise<Object|Object[]|null>} Respuesta JSON del backend.');
    lines.push(' * @throws {Error} Si la petición falla (4xx/5xx).');
    lines.push(' * @async');
    lines.push(' */');
    return lines.join('\n');
}

/**
 * Documenta un controlador completo como módulo JSDoc con todos sus
 * endpoints.
 *
 * @param {Object} controller - Estructura devuelta por {@link parseController}.
 * @returns {string} Bloque de módulo JSDoc con sus endpoints.
 */
function buildControllerModule(controller) {
    const { className, basePath, moduleName, endpoints } = controller;
    const lines = [];
    lines.push('/**');
    lines.push(` * @file Endpoints REST del controlador **${className}**.`);
    lines.push(' *');
    lines.push(` * Ruta base: \`${basePath}\`.`);
    lines.push(' *');
    lines.push(' * @module ' + moduleName);
    lines.push(' * @author MangaCo Team');
    lines.push(' * @since 2026-10-21');
    lines.push(' * @version 1.0.0');
    lines.push(' * @tutorial introduccion');
    lines.push(' */');
    lines.push('');

    endpoints.forEach((ep) => {
        lines.push(buildEndpointDoc(ep, className));
        lines.push(`const ${moduleName}_${sanitizeIdentifier(ep.methodName)} = () => {};`);
        lines.push('');
    });

    return lines.join('\n');
}

/**
 * Lee todos los controladores, genera la documentación JSDoc y escribe
 * el archivo `api-references.js`.
 *
 * @returns {void}
 */
function generate() {
    if (!fs.existsSync(CONTROLLERS_DIR)) {
        console.error(`[generate-api-references] No se encontró: ${CONTROLLERS_DIR}`);
        process.exit(1);
    }

    const files = fs
        .readdirSync(CONTROLLERS_DIR)
        .filter((f) => f.endsWith('Controller.java'))
        .sort();

    const header = [
        '/**',
        ' * @file Referencias de la API REST de MangaCo (autogenerado).',
        ' *',
        ' * Este archivo se genera automáticamente a partir de los controladores',
        ' * de Spring Boot mediante `npm run docs`. No editar a mano.',
        ' *',
        ' * @author MangaCo Team',
        ' * @since 2026-10-21',
        ' * @version 1.0.0',
        ' * @tutorial introduccion',
        ' */',
        '',
    ].join('\n');

    const blocks = files
        .map((file) => parseController(fs.readFileSync(path.join(CONTROLLERS_DIR, file), 'utf8')))
        .filter(Boolean)
        .map(buildControllerModule)
        .join('\n');

    fs.writeFileSync(OUTPUT_FILE, header + '\n' + blocks, 'utf8');
    console.log(
        `[generate-api-references] Generado ${path.relative(BACKEND_ROOT, OUTPUT_FILE)} ` +
            `con ${files.length} controladores.`
    );
}

if (require.main === module) {
    generate();
}

module.exports = { parseController, toApiRoute, toModuleName, generate };
