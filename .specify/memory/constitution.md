---
título: Constitución de ENVER
versión: 1.0.0
fecha_ratificación: pendiente — requiere aprobación humana (Checkpoint 3)
canonical: esta ruta (.specify/memory/constitution.md) es la ÚNICA fuente de verdad
---

# Constitución de ENVER

> **Fuente canónica de verdad.** Este archivo es el único documento constitucional de ENVER. No existe espejo en raíz a propósito: una única copia evita divergencias (decisión registrada en `Memoria.md`). `AGENTS.md`, `Memoria.md`, `CHECKLIST_PR.md`, las plantillas y los agentes subordinan su contenido a este documento; ante contradicción, gana siempre la constitución (ver jerarquía en §G).
> Estado: BORRADOR en espera de ratificación humana. Se aplica como regla operativa desde el Checkpoint 3 en adelante; su ratificación formal se recoge en la tabla de Aprobación del fondo.

## A. Principios no negociables

1. **SDD obligatorio** — Todo cambio funcional relevante tiene especificación antes del plan, plan antes de tareas y tareas antes de implementación. "Relevante" = toca pantallas, navegación, estado compartido, permisos, configuración de build/mapa, o añade archivos nuevos de producción. Cambios de documentación y correcciones factuales menores no requieren flujo completo.
2. **Orden inalterable** — Spec → Plan → Tasks → Implementación. Ningún paso puede saltarse, ni "porque el cambio es sencillo".
3. **Aprobación humana previa** — Ninguna implementación funcional comienza sin una especificación con Aprobación firmada (revisor humano + fecha + `CHECKLIST_PR.md` con la **Fase A resuelta**: ítems obligatorios de la fase de especificación marcados ✅ o ➖ con justificación, y los ítems de fases posteriores marcados ◻ Reservado sin bloquear esta fase + sin conflicto con la constitución). La correspondencia ítem↔fase y las reglas de marcaje están definidas en `CHECKLIST_PR.md` §0; ninguna fase delega sus controles a la anterior.
4. **Línea base visual protegida** — `docs/ENVER_UI_BASELINE.md` es el inventario de lo que se preserva: colores, textos, geometrías, navegación, back behavior y flujos visibles. La preservación de la UI es un **criterio de aceptación verificable** de cualquier cambio que afecte UI, no una recomendación.
5. **Cambios pequeños y revisables** — 1 slice = 1 unidad liberable y revisable como diff; prohibido "aprovechar" un slice para reescribir código ajeno.
6. **Arquitectura mínima y adaptativa** — Se añade complejidad (ViewModels, capas de dominio/datos, DI, librerías) **solo** cuando un spec demuestra la necesidad con el estado real (aplicación de un solo módulo, etapa visual). No se importan estructuras de otros proyectos por prestigio.
7. **Stack respetado** — Kotlin y Jetpack Compose son las tecnologías existentes. Las restricciones documentadas de compatibilidad (entre otras: Kotlin 2.2.10 ↔ maps-compose 6.12.2) se consultan antes de tocar dependencias (`docs/ENVER_CURRENT_STATE_AUDIT.md` §2.2).
8. **Seguridad y privacidad desde diseño** — Mínimo privilegio en permisos; sin secretos en código, logs ni archivos versionables; datos de ubicación y fotos como datos sensibles; manejo de excepciones: prohibido tragarlas en silencio.
9. **Prohibición de secretos versionables** — Ninguna API key, token, contraseña o credencial en archivos que vayan a commit. `local.properties` y propiedades locales son el canal para valores sensibles; el estado actual de `MAPS_API_KEY` (fuera de `gradle.properties` versión) queda registrado en `Memoria.md` como decisión abierta.
10. **Pruebas proporcionales** — Cada tarea con lógica lleva una verificación real (comando de compilación/test/documentable). No se exige una cobertura importada de otros proyectos; se mide primero, se fija después.
11. **Trazabilidad completa** — Requisito → spec → plan → tarea → implementación → evidencia de verificación. Cada decisión relevante citA fuente o aprobación.
12. **Documentación actualizada** — Cuando el estado real cambia, se actualizan `Memoria.md` y los docs afectados en el mismo slice. Un doc que contradiga el código se corrige, no se discute.
13. **Dependencias y migraciones** — Toda dependencia nueva o actualización requiere: porqué, versión verificada compatible con el stack actual, y aprobación humana. Pasa por revisión de spec aunque sea "solo una línea en el build".
14. **Honestidad de verificación** — Ningún agente puede declarar aprobada una compilación, prueba, revisión de seguridad o verificación visual que no haya ejecutado. Los gates no ejecutados se marcan como pendientes con su comando pendiente, nunca como aprobados.
15. **Preservación verificable** — Cualquier cambio que renderice UI produce: la lista de pantallas/componentes visibles afectados, la evidencia (captura en dispositivo/emulador cuando exista dispositivo; en su defecto revisión de diff declarada como tal) y la aceptación humana si el comportamiento visible cambia.

## B. Reglas de código (vigentes desde hoy)

- Pantallas desacopladas del `NavHostController` (solo callbacks); navegación centralizada en `navigation/`.
- Paleta y tema desde `ui/theme/`; prohibido introducir colores inline nuevos salvo para replicar la línea base tal cual.
- Imágenes siempre vía `EnverImage`/`ImageSource` (a punto único de migración remota).
- El encabezado global y la barra inferior viven una sola vez (`MainScaffold`); no se duplican.
- Rutas nuevas: constantes en `NavRoutes.kt` + acciones en `NavigationActions.kt` (hasta que se apruebe una migración type-safe, decisión pendiente).

## C. Reglas de seguridad (vigentes desde hoy)

- Deny-list transversal para agentes y humanos en código: no leer/exponer valores de `local.properties`, `gradle.properties` (permiso de lectura de claves solo si la tarea lo exige y sin imprimir valores), `*.jks`, `*.keystore`, `keystore.properties`, `keys.properties`, `secrets.properties`, `.env*`.
- Logs sin PII ni datos de ubicación.
- `MAPS_API_KEY` y futuras claves solo vía propiedades locales/documentadas, jamás en el código fuente.
- Ante datos de emergencia/ubicación: negar por defecto, pedir al usarlo, explicar el uso al usuario.

## D. Reglas de gestión del agente

- Cualquier agente opera bajo la deny-list de §C y las prohibiciones de `AGENTS.md`.
- Un cambio que exceda el alcance aprobado implica **detenerse y pedir autorización** — no una ampliación discreta.
- Errores preexistentes descubiertos durante una tarea se registran por separado en `Memoria.md` (§Deuda/Errores a evitar), no se "arreglan de paso".
- El permiso declarado en prompts de agentes es un límite de comportamiento, **no** un mecánismo de seguridad técnico garantizado. La contención real disponible es: diff de Git revisado, gates ejecutados y aprobación humana.

## E. Preservación UI — protocolo

1. Antes de tocar un archivo que renderice UI: consultar `docs/ENVER_UI_BASELINE.md` (pantalla afectada + capturas pendientes §7).
2. Implementar sin alterar textos, hex, dp/sp, orden de layout, animaciones implícitas ni navegación que no estén en el spec aprobado.
3. Verificar: comparación de código contra baseline + capturas antes/después cuando exista dispositivo (si no las hay, documentar esa limitación como pendiente).
4. Reportar en el cierre del slice: "preservación visual: aplicada (evidencia X)" o "revisada solo estáticamente (limitación)".

## F. Gestión de deuda técnica

- La deuda se nombra con ubicación (archivo+línea) y se registra en `Memoria.md` §Deuda y/o spec §Riesgos.
- Cada elemento de deuda tiene dueño-condición de resolución (p. ej. "cuando exista diseño del Paso 4").
- Los placeholders vigentes (Paso 4, Pedidos/Perfil, ForgotPassword, Remote image, sin emulador) son deuda declarada, no silencios.

## G. Jerarquía documental

```
1. .specify/memory/constitution.md      ← máxima autoridad
2. AGENTS.md                            ← cómo se trabaja (procedimiento)
3. specs/<slug>/spec.md Aprobada        ← contrato del feature en curso
4. Memoria.md                           ← estado, decisiones con porqué, deuda
5. docs/*                               ← inventarios e informes técnicos
6. .opencode/skills/                    ← know-how técnico reutilizable
7. prompts de agentes (.opencode/agents)← definición de labores y límites
```
Ante conflicto: gana el de arriba. Si dos documentos están al mismo nivel, gana el más reciente por fecha/aprobación y se consolida en el mismo cambio. Modificación de esta constitución: solo mediante edición explícita aquí, incrementando `versión` y actualizando la tabla de Aprobación del fondo; los principios §A no se eliminan, se marcan como obsoletos con razón y fecha.

## H. Aprobación de la constitución (¿quién la ratifica?)

| Versión | Estado | Revisor | Fecha | Evidencia |
|---|---|---|---|---|
| 1.0.0 | Borrador (pendiente de ratificación humana) | — | — | Creada en Checkpoint 3 |
