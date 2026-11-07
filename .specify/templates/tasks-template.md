# Tareas — [FT-000 provisional] [título]

> Se elabora DESPUÉS de un plan aprobado. Cada tarea es pequeña, ordenada y verificable. Una tarea NO autoriza cambios en archivos protegidos (si los requisitos la empujan ahí, se detiene y se repite la revisión del plan).
> Regla de orden sugerida: datos/modelos → lógica verifiable → UI → cableado → verificación/diff/docs.

## 0. Datos de control

| Campo | Valor |
|---|---|
| Plan aprobado (v/ruta) | specs/…/plan.md — Aprobado el … |
| Estado | Borrador / Aprobado por humano (fecha, revisor) |
| Nº de tareas | |

## 1. Tareas

### T[n].1 — [Objetivo corto e imperativo]

| Campo | Contenido |
|---|---|
| Objetivo | |
| Requisitos del spec que cubre | R[?] (de la sección §4 del spec) |
| Dependencias | T[n].[?] |
| Archivos permitidos (lista exacta, no "…y afines") | |
| Pasos acotados | 1. … 2. … 3. … |
| Criterios de aceptación | (observable, referenciable a §6 del spec) |
| Verificación concreta | COMANDO real (p. ej. `.\gradlew.bat :app:assembleDebug`) o inspección específica (p. ej. "grep de X == n coincidencias, ruta Y") |
| Evidencia a presentar | (salida de comando, diff, captura, tabla) |
| Condición de finalización | (qué queda demostrado y qué documento señala la tarea como cerrada — tareas.md se marca [x] solo con evidencia) |

📌 Si durante la tarea aparece un error preexistente o excesivo: detenerse, registrar hallazgo por separado y pedir autorización (AGENTS.md §3).

### T[n].2 — [repitir estructura]

## 2. Registro de ejecución (marcar [x] SOLO con evidencia enlazada)

| Tarea | Estado | Verificación ejecutada (sí/no) | Evidencia (ruta/enlace) | Fecha |
|---|---|---|---|---|
| T1 | [ ] | | | |

## 3. Cuota de revisión humana

Cualquier cambio de alcance, de archivos autorizados o de un requisito → repetir la revisión del plan; no se reparametriza sobre la marcha.
