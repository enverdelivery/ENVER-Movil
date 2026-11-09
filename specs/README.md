# specs/ — Índice SDD (spec maestra · CI/CD · APIs · features FT-01..FT-0X)

> Rector: `.specify/memory/constitution.md` + `AGENTS.md`. Referencia de formato: repo MovilSDD (solo lectura, `specs/README.md` + `specs/_template.md`).

## Mapa de specs

| Carpeta | Qué contiene | Estado |
|---|---|---|
| `ENVER-SISTEMA/` | **Spec maestra** del sistema completo (superficies [IMPL]/[PEND]/[POR DEFINIR], arquitectura, integraciones, criterios) | Aprobada como referencia viva (2026-11-08); no sustituye specs por feature |
| `CI-CD/` | Controles de integración ([CONFIG] vs [PROP]) y paso mínimo para primer pipeline | Documento de alcance; NADA de pipeline implementado |
| `APIs/` | Estructura/listado para specs por contrato de backend | **PENDIENTE de URL AWS con los contratos reales** (no se inventan endpoints) |
| `FT-01..FT-05/` | Features concretos (spec/plan/tasks + evidencia por tarea) | FT-01–FT-05 implementados y verificados por compilación (runtime pendiente de dispositivo) |

## Estructura por feature (vigente desde FT-02)

```text
specs/FT-0X-nombre/
├── spec.md        (spec canónica: 7 secciones + Aprobación)
├── plan.md        (diseño puntual: archivos afectados, wiring, gates ENVER, docs vivos)
└── tasks.md       (checklist por fases con `[x]` + "verif:" real por tarea)
```

Canónica de spec: `specs/_template.md`. Cada task marca verificación real (`assembleDebug`, greps, diff) y distingue verificación de compilación de las pruebas que requieren dispositivo/emulador.

## Convenciones adoptadas del profesor (Fase 1, Checkpoint 4.1)

- Spec de 7 secciones + tabla `Aprobación` (Estado/Revisor/Fecha/Evidencia) — reemplaza a la spec de 14 secciones para features NUEVOS.
- `plan.md` pequeño orientado a archivos y gates, sin capas que ENVER no usa (sin DI/Koin/detekt/CI).
- `tasks.md` en estilo checklist plano con verificación explícita por tarea.
- Aprobación solo se marca firmada con evidencia real; al cerrar el slice se concilia spec/plan con lo implementado.
- LOS checkpoints (carpetas `checklists/` por feature) NO se adoptan: el formato del profesor mismo los abandonó a partir de FT-04 y ENVER no corre gates CI.

## Deuda/migración (mínima)

- `FT-01-recuperar-compilacion/` conserva su formato histórico de 14 secciones (v2, aprobada e implementada); no se reescribe. Se migra solo si algo futuro necesita conciliarla.
- Las plantillas de `.specify/templates/` siguen existiendo para trazabilidad del pasado del proyecto; la canónica operativa para features nuevos es `specs/_template.md`.
