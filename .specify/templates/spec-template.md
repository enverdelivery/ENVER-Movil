# Especificación — [FT-000 provisional] [título corto y descriptivo]

> Usa este archivo COPIÁNDOLO a `specs/<slug>/spec.md`. El identificador FT-XX se asigna y confirma con el usuario ANTES de nombrar el directorio; esta plantilla usa "FT-000 provisional".
> Reglas de uso (AGENTS.md §3): la spec se escribe ANTES del plan. Estado inicia en Borrador. Nadie la aprueba a sí mismo.

---

## 0. Datos de control

| Campo | Valor |
|---|---|
| Identificador | FT-000 (provisional) |
| Título | |
| Estado | Borrador / En revisión / Aprobada / Implementada / Cancelada |
| Autor de la redacción | |
| Versión | 1 |

## 1. Problema y contexto

- Problema real observable (con evidencia por ruta/estado, no suposición):
- Situación actual relevante: enlaza `docs/ENVER_CURRENT_STATE_AUDIT.md` §correspondiente si existe.
- Por qué ahora (qué habilita o desbloquea):

## 2. Objetivo y resultados esperados

- Objetivo medible:
- Resultados esperados (lista de entregables observables):
- Cómo se sabrá que terminó (fuera de lo técnico: qué puede hacer el usuario o qué queda registrado):

## 3. Usuarios y escenarios

- Usuario principal:
- Escenario 1 (paso a paso):
- Escenario 2 (si aplica):

## 4. Requisitos funcionales

| ID | Requisito | Prioridad | Verificable por |
|---|---|---|---|
| R1 | | Alta/Media/Baja | |
| R2 | | | |

Reglas de negocio especiales (texto exacto, límites, validaciones) — lista cada mensaje de error EXACTO si se afecta validación.

## 5. Requisitos no funcionales

- Rendimiento (si medible; indicar cómo se medirá):
- Seguridad / privacidad (datos tratados, permisos, riesgo):
- Compatibilidad (versiones del stack afectadas — citar restricciones de compatibilidad del proyecto):
- Accesibilidad / textos (si hay textos visibles nuevos, cotizados):

## 6. Criterios de aceptación observables

Dado / Cuando / Entonces — cada uno comprobable por un humano:

1. Dado … cuando … entonces …
2. …

> Regla: cada criterio debe poder fallar. Si no puede fallar, no es un criterio, es un decorado.

## 7. Comportamiento actual que debe conservarse (preservación)

- De `docs/ENVER_UI_BASELINE.md`, elementos afectados por este cambio que NO deben cambiar:
- Comportamientos delicados (backstack, estado del wizard, permisos) que quedan congelados salvo los puntos del §4:
- Incertidumbres visuales (capturas pendientes de la baseline §7) que condicionan este spec:

## 8. Casos límite y errores

| Caso | Comportamiento esperado | Estado actual hoy |
|---|---|---|
| Campo vacío / permiso denegado / sin red / rotación / proceso nuevo / etc. | | |

## 9. Seguridad y privacidad

- Datos sensibles tocados:
- Permisos necesarios (si hay: lista exacta + flujo de solicitud/denegación):
- Almacenamiento/log del nuevo dato:

## 10. Exclusiones explícitas (fuera de alcance)

- [Lista; todo lo no listado pasa a deuda con registro en Memoria]:
- Exclusiones de archivos (protegidos que NO se tocarán):

## 11. Riesgos y dependencias

| Riesgo / dependencia | Impacto | Mitigación / condición |
|---|---|---|
| | | |

## 12. Decisiones pendientes

| # | Decisión | Quién la aprueba | Condición |
|---|---|---|---|
| | | Humano | |

## 13. Plan de pruebas (alto nivel; el detalle va en tasks/plan)

- Tipo de verificación (compilación/test manual/instrumentado):
- Comandos previstos:

## 14. Aprobación humana (OBLIGATORIO; no editar sin revisor)

| Campo | Valor |
|---|---|
| Estado de Aprobación | Borrador |
| Revisor humano | |
| Fecha de aceptación expresa | |
| CHECKLIST_PR **Fase A resuelta** (1.1, 4.3-A y 5.2-A marcados ✅/➖ con evidencia y justificación; los ítems de fases B/C quedan ◻ Reservado y NO bloquean esta aprobación) | |
| Sin conflicto con la constitución | |
