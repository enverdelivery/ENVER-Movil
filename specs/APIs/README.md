# APIs — Estructura de specs de backend (PENDIENTE de contratos reales)

> Estado: **PENDIENTE** — el profesor anunciará una URL AWS con los contratos del backend.
> Hasta recibir esa URL NO se crean specs de API (regla: no inventar endpoints).

## Convención para cada spec de API (cuando lleguen los contratos)

Una carpeta por contrato: `specs/API-<nombre>/spec.md` con el formato del profesor (`specs/_template.md`) describiendo, basándose SOLO en la información real disponible:

1. **Método y ruta** (verb + path).
2. **Autenticación** (sólo si el contrato la define; NADA de JWT/sesiones inventadas).
3. **Parámetros** (path/query/header).
4. **Cuerpo de request** (campos + tipos + validaciones declaradas).
5. **Respuestas y errores** (códigos del contrato, qué devuelve).
6. **Criterios de aceptación** y verificación ejecutable disponibles.

Si el contrato real está incompleto, la spec lo declara ([CAPEND]) en la sección correspondiente — no se rellena con supuestos.

## Qué NO hacer (reglas duras de la constitución)

- No invocar HTTP, no añadir dependencias (Retrofit/OkHttp), no cambiar pantallas de autenticación hasta que exista spec aprobada para el endpoint concreto.
- Los screens que fingen datos (`SampleArticles`, wizard) NO se conectan a nada sin spec FT.

## Entrada pendiente

| Insumo | Estado |
|---|---|
| URL AWS con documentación del backend | Pendiente de entrega por el profesor |
| Decisión de qué endpoint primero | Pendiente (sugerencia operativa: el que habilite registro/login real o el listado real de artículos) |
