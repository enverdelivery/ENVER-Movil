# Spec Template - FT-0X (canónica, adaptada a ENVER)

> Copia física en `specs/_template.md` (Checkpoint 4.1); adaptada de `specs/_template.md` del repo MovilSDD (solo lectura).
> Rector: `.specify/memory/constitution.md` + `AGENTS.md` + `Memoria.md`.
> 7 secciones + Aprobación. Sin cambio de contrato interno sin spec.
> ENVER verifica con `assembleDebug` + greps (sin lint/detekt/kover/CI); las pruebas que requieren dispositivo/emulador se declaran PENDIENTES si no se corren.

## 1. Contexto y objetivo

<!-- Problema, alcance, fuera de alcance, justificación del incremento -->

## 2. Requisitos funcionales

<!-- Pantallas/composables, textos exactos aprobados, wiring de navegación reutilizando los componentes existentes -->

## 3. Requisitos no funcionales

<!-- Preservación visual (baseline docs/ENVER_UI_BASELINE.md), accesibilidad, build verde, rendimiento/sizes si aplica con evidencia en Memoria.md -->

## 4. Arquitectura y contratos internos

<!-- Solo lo que existe: composables, NavRoutes (routes string), NavigationActions, componentes compartidos (EnverImage/tema), estado hoisted. NO se presupone Model->Repository->UseCase->ViewModel: ENVER no tiene esas capas todavía -->

## 5. Criterios de aceptación

<!-- Given/When/Then verificables -->

## 6. Plan de pruebas

<!-- assembleDebug, greps de cierre, diff de alcance, y pruebas device/emulador listadas como PENDIENTES si no se ejecutan -->

## 7. Riesgos, límites y deuda

<!-- Riesgos de navegación/back-stack, textos pendientes de revisión, límites de ausencia de dispositivo -->

## Aprobación

| Estado | Revisor | Fecha | Evidencia |
|---|---|---|---|
| Borrador | - | - | - |

<!-- Firmado solo con aceptación expresa del propietario. Al cerrar el slice, conciliar spec/plan con lo implementado. -->
