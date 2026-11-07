# CHECKLIST_PR — Gate de revisión por fases para cambios en ENVER

> Obligatorio por constitución (§A.3, §A.14): cada fase (especificación, planificación, implementación) entra en Aprobación cuando los ítems **obligatorios de esa fase** están resueltos según el mapa de §0. Los ítems de fases futuras NO bloquean la fase actual.
> Marca por ítem:
> - ✅ **Aprobado** — resuelto con evidencia (requiere revisión; nadie se autoaprueba).
> - ➖ **No aplica** — con justificación breve escrita junto al ítem.
> - ⏳ **Pendiente** — bloquea la firma de la fase a la que pertenece el ítem.
> - ◻ **Reservado** — ítem de una fase futura; todavía no debe evaluarse. No bloquea la fase en curso y NO es pendiente.
>
> Reglas del marcaje:
> 1. Un ítem solo bloquea la aprobación de la fase a la que pertenece (mapa §0).
> 2. Los ítems de fases futuras quedan ◻ Reservado y no impiden aprobar la fase actual.
> 3. Un ítem no aplicable se marca ➖ con su porqué (no se borra ni se deja en blanco).
> 4. Al firmar una fase, sus ítems obligatorios no pueden quedar ⏳.
> 5. Prohibido convertir un ⏳ en ✅ sin evidencia nueva y revisión humana (no existe auto-aprobación).
> 6. Las verificaciones de fases futuras se conservan: no se eliminan para "limpiar" una fase.
> 7. Cada comprobación conserva su columna de evidencia; sin evidencia no hay ✅.

## 0. Mapa de fases: qué apartados aplican a cada aprobación

| Fase | Qué aprueba | Ítems obligatorios de esta fase (todos los demás quedan ◻ Reservado o ➖ según corresponda) |
|---|---|---|
| **A — Especificación** | `specs/<slug>/spec.md` pasa de Borrador a Aprobada (verificable leyendo la spec; no exige build ni diff) | 1.1 · 4.3-(A) · 5.2-(A) |
| **B — Planificación** | plan.md (y, re-verificado, tasks.md) aprobados | 1.2 · 2.1 · 3.2 · 10.1 · 10.2 |
| **C — Implementación y revisión** | Slice implementado y revisado; cierre con evidencia | 1.3 · 2.2 · 2.3 · 3.1 · 3.3 · 4.1 · 4.2 · 4.3-(C) · 5.1 · 5.3 · 6.1 · 6.2 · 7.1 · 7.2 · 7.3 · 8.1 · 8.2 · 9.1 · 9.2 · 11.1 · 11.2 |

Notas de aplicación:
- 4.3 es **doble**: en A se comprueba que la spec DECLARA los cambios de comportamiento visible; en C, que lo implementado COINCIDE con lo aprobado y nada visible cambió sin spec.
- 5.2 es **doble**: en A, la spec declara los permisos (si los hay); en C, el diff y el manifest confirman que los permisos coinciden (el ítem 5.2 se evalúa en A y de nuevo en C).
- 2.1 en B define/espera la lista de archivos afectados del plan; su verificación contra el diff real pertenece a 2.2 (fase C).
- Flujo ejecutable en orden: A (solo lectura de la spec) → B (plan+tareas, solo lectura de los artefactos) → implementación → C (captura run de build/diff/evidencia). Ninguna fase exige completar ítems de la fase siguiente.

## 1. Correspondencia con la especificación

| # | Comprobación | Marca | Evidencia |
|---|---|---|---|
| 1.1 Ⓐ | La propia spec contiene problema, alcance y fuera de alcance, requisitos, criterios de aceptación, exclusiones y comportamientos a preservar — documentados y revisables (constitución §A.3) | | |
| 1.2 Ⓑ | Cada requisito del spec mapea a tarea(s) y a archivos concretos (trazabilidad) | | |
| 1.3 Ⓒ | Lo entregado no implementa requisitos "extra" fuera del spec | | |

## 2. Alcance autorizado

| # | Comprobación | Marca | Evidencia |
|---|---|---|---|
| 2.1 Ⓑ | El plan lista los archivos afectados (permiso de cambio) y los protegidos; la tarea/slice se atiene a esa lista | | |
| 2.2 Ⓒ | Ningún archivo protegido (UI, navegación, manifest, build, recursos) cambió fuera de lo autorizado | | |
| 2.3 Ⓒ | Nada se renombró, movió o eliminó sin estar en el plan | | |

## 3. Calidad y coherencia arquitectónica

| # | Comprobación | Marca | Evidencia |
|---|---|---|---|
| 3.1 Ⓒ | Convenciones del repo respetadas (callbacks en pantallas, tema centralizado, NavRoutes/NavigationActions, `EnverImage`, header global/barra única) | | |
| 3.2 Ⓑ | El plan no introduce capas ceremoniales, DI, persistencia ni dependencias sin spec | | |
| 3.3 Ⓒ | Cambios mínimos y localizados (no reescrituras tangenciales) | | |

## 4. Preservación visual (solo si se afecta UI; ➖ si no aplica)

| # | Comprobación | Marca | Evidencia |
|---|---|---|---|
| 4.1 Ⓒ | Alteraciones comparadas 1:1 contra `docs/ENVER_UI_BASELINE.md` (colores hex, textos, dimensiones, navegación, back behavior) | | |
| 4.2 Ⓒ | Capturas antes/después cuando existe dispositivo/emulador; en su defecto, revisión estática declarada explícitamente como tal | | |
| 4.3 Ⓐ/Ⓒ | A: el spec DECLARA los cambios de comportamiento visible que propone. C: lo implementado coincide con lo aprobado y nada visible cambió sin spec (no es efecto colateral) | | |

## 5. Seguridad y privacidad

| # | Comprobación | Marca | Evidencia |
|---|---|---|---|
| 5.1 Ⓒ | Ningún secreto/token/clave en el diff ni en archivos versionables (`local.properties`, `gradle.properties`, keystores intactos) | | |
| 5.2 Ⓐ/Ⓒ | A: el spec declara los permisos que necesita (si hay). C: el diff confirma que no hay permisos extra (mínimo privilegio) | | |
| 5.3 Ⓒ | Sin logs con PII, tokens, ubicación ni contenido de credenciales | | |

## 6. Manejo de errores

| # | Comprobación | Marca | Evidencia |
|---|---|---|---|
| 6.1 Ⓒ | Los errores nuevos se manejan explícitamente (mensajes existentes conservados; sin excepciones tragadas en silencio) | | |
| 6.2 Ⓒ | Los errores preexistentes descubiertos quedaron documentados por separado (distinguidos de los introducidos) | | |

## 7. Pruebas ejecutadas y evidencia

| # | Comprobación | Marca | Evidencia |
|---|---|---|---|
| 7.1 Ⓒ | Comando base ejecutado con resultado real: `.\gradlew.bat :app:assembleDebug` (o declarado no-ejecutable con el bloqueo documentado) | | |
| 7.2 Ⓒ | Tests del slice ejecutados si existen (comando + salida) | | |
| 7.3 Ⓒ | Ninguna verificación declarada "aprobada" sin ejecución; pendientes listadas con su comando | | |

## 8. Documentación y memoria

| # | Comprobación | Marca | Evidencia |
|---|---|---|---|
| 8.1 Ⓒ | `Memoria.md` actualizado (estado, decisiones con porqué, deuda) | | |
| 8.2 Ⓒ | Docs técnicos afectados actualizados o anotados como drift | | |

## 9. Revisión del diff

| # | Comprobación | Marca | Evidencia |
|---|---|---|---|
| 9.1 Ⓒ | `git diff`/`git status` revisados por humano; archivos creados/modificados listados 1:1 | | |
| 9.2 Ⓒ | Sin cambios no explicados (whitespace/imports sueltos contados y justificados) | | |

## 10. Dependencias

| # | Comprobación | Marca | Evidencia |
|---|---|---|---|
| 10.1 Ⓑ | El plan aprueba explícitamente cualquier dependencia nueva/eliminada/actualizada (porqué + spec); en su defecto ➖ | | |
| 10.2 Ⓑ | Restricciones de compatibilidad del stack verificadas en el plan (Kotlin 2.2.10 / AGP 9.2.1 / Compose BOM 2026.02.01 / maps-compose 6.12.2) | | |

## 11. Limitaciones y deuda

| # | Comprobación | Marca | Evidencia |
|---|---|---|---|
| 11.1 Ⓒ | Limitaciones declaradas (qué NO se verificó y por qué) | | |
| 11.2 Ⓒ | Deuda nueva registrada en `Memoria.md` con ubicación y condición de resolución | | |

## 12. Registro de aprobación por fase

| Fase | Ítems obligatorios sin resolver (debe ser cero) | Acción al firmar | Revisor humano | Fecha | Versión del artefacto aprobado |
|---|---|---|---|---|---|
| A — Especificación | | ✅ Aprobada / ➖ Rechazada con motivo | | | |
| B — Planificación (plan y, re-verificado, tasks) | | ✅ Aprobados / ➖ Rechazados con motivo | | | |
| C — Implementación y revisión | | ✅ Cerrado con evidencia / ➖ Devuelto con motivo | | | |

Reglas del registro:
- Cada fase se firma por separado; una firma NO aprueba de paso las fases siguientes.
- Un ⏳ de la fase en curso impide firmarla (regla 4 del encabezado).
- Los ◻ Reservado pasan a evaluarse cuando llega su fase; un ◻ NUNCA se convierte en ✅ automáticamente (regla 5).
