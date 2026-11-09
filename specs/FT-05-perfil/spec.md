# FT-05 — Pantalla Perfil

## 1. Contexto y objetivo
La sexta sección `PERFIL` del `MainScaffold` muestra `SectionPlaceholder` ("Pantalla en construcción"). Objetivo: darle una pantalla propia con estado vacío honesto, siguiendo el mismo lenguaje visual ya aprobado en FT-03 (Pedidos) y sin importar backend, sesión ni datos personales.

## 2. Requisitos funcionales
1. La sección Perfil (barra inferior) muestra la pantalla nueva: icono de perfil coherente con el diseño (`Icons.Outlined.PersonOutline`, ya usado en el proyecto) + tarjeta con "Todavía no tienes información de perfil".
2. Mensaje breve de que la información estará disponible cuando se integre el backend.
3. Sin nombre, correo, saldo, historial, sesión ni botones aparentemente funcionales.
4. Sin navegación ni datos nuevos; el resto de secciones sin cambios.

## 3. Requisitos no funcionales
- Reutilización del vocabulario visual existente (EnverCardBg/EnverBadgeBg/EnverTextMuted/EnverDarkGreen, RoundedCornerShape 16/24, tipografía 22/14/12).
- Cambio mínimo: 1 archivo nuevo + 1 línea de wiring + SDD.
- Gate: `assembleDebug` exit 0 + greps + diff auditado.

## 4. Arquitectura y contratos internos
- Sin rutas (sección interna del scaffold, decisión FT-03); el placeholder de ENVER desaparece al quedar las 4 secciones cubiertas (FT-03/FT-04/FT-05) — se elimina si solo usaba PERFIL; si NUEVO/otros lo persiguen, se conserva. Verificación: grep usos.
- Sin estado: pantalla estática sin datos (backend [POR DEFINIR]).

## 5. Criterios de aceptación
1. PERFIL abre la pantalla nueva; perfección visual equivalente a la tarjeta de Pedidos (icono + mensaje).
2. Ninguna otra sección cambia; `SectionPlaceholder` conserva su uso si algo más lo reclama (grep).
3. `assembleDebug` exit 0; diff limitado al plan.

## 6. Plan de pruebas
- Gate de compilación + greps (rama PERFIL → PerfilScreen; placeholder sin usos o retenido con razón documentada).
- Runtime device — [PEND] sin dispositivo (0 detectados en el checkpoint 4.4; no se crea uno).

## 7. Riesgos, límites y deuda
- Cuando el backend permita sesión/perfil, esta pantalla se integra a datos reales — [POR DEFINIR].

## Aprobación
| Campo | Valor |
|---|---|
| Estado | Aprobada por instrucción directa del propietario (Checkpoint 4.5: Exactamente el alcance recomendado y permitido en 4.4 §3) |
| Revisor | Propietario ENVER |
| Fecha | 2026-11-08 |
| Evidencia | Instrucción íntegra del Checkpoint §2 y §3 |
