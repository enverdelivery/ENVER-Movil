# CI-CD — Controles de integración continua para ENVER

> Alcance declarado por la instrucción del propietario (alineación 2026-11-08).
> Convención: **[CONFIG]** control realmente configurado y ejecutable hoy · **[PROP]** propuesto, NO configurado (no se afirma que exista).

## 1. Contexto

ENVER se verifica hoy manualmente con `.\gradlew.bat :app:assembleDebug` (gate real, ejecutado por checkpoint). El repository ruleset de GitHub exige PR para `main` (verificado: GH013, reject de push directo).

## 2. Controles requeridos para ENVER (mínimo viable)

| # | Control | Propósito | Estado |
|---|---|---|---|
| 1 | Compilación Android (`assembleDebug`) en cada PR | Bloquear merges que rompan compilación | [PROP] — el comando es real y probado, pero NO existe pipeline que lo ejecute |
| 2 | Revisión humana (PR) + CHECKLIST_PR por fase | Desviación de alcance / secretos | [CONFIG] — reglas del repo (ruleset) ya fuerzan PR; checklist existe y se aplica manualmente |
| 3 | Escaneo de secretos (grep básico) en pre-commit del slice | No publicar credenciales | [CONFIG] manual (memoria de cada checkpoint) · [PROP] automatizarlo en CI |
| 4 | Verificación de cambios con log real guardado (`%TEMP%\opencode\*.txt` + Memoria) | Evidencia por checkpoint | [CONFIG] (convención operativa) |
| 5 | Tests unitarios y de lint (lint/detekt/ktlint, kover) | Calidad automática | [PROP] — NO existen ni tests ni herramientas; requiere aprobar dependencias (constitución) |

## 3. Pasos mínimos propuestos para el primer pipeline (NO aprobado aún)

1. `.github/workflows/ci.yml` con UN job (Ubuntu, JDK 21, `gradlew assembleDebug --no-daemon`) sobre `pull_request` y `push` a `main`.
2. Habilitar la regla "Require status checks" del ruleset cuando el job exista.
3. Recién después: `testDebugUnitTest` cuando exista el primer test de proyecto (Fase P2 de la proposal).

**No se implementó nada de esto en esta tarea** (alcance excluido: sin errores, sin dependencias, sin herramientas).

## 4. Criterios de aceptación

- Este spec no afirma ningún pipeline inexistente; cualquier implementación futura parte del §3 con spec propia aprobada.

## 5. Riesgos y límites

- Actions requiere minutos de GitHub (organización `enverdelivery`); el costo es del propietario.
- Configuración de protection rules desde la UI de GitHub es acción humana.

## 6. Plan de pruebas

Del pipeline propuesto: PR de prueba con un cambio de compilación rota → debe quedar en rojo (verificación de los gates). NO ejecutado (no existe pipeline).

## 7. Aprobación

| Campo | Valor |
|---|---|
| Estado | Aprobada como documento de alcance/planificación NO implementada (paso siguiente mínimo documentado §3) |
| Revisor | Propietario ENVER |
| Fecha | 2026-11-08 |
| Evidencia | Reglas del repo verificadas por push rechazado GH013 (Checkpoints 4.1–4.2) |
