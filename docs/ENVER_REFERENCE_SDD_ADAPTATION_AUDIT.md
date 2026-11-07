# Auditoría de adaptación SDD de referencia — ENVER (Checkpoint 3.4)

> **Estatus: BORRADOR (sin ratificación del propietario).** Este documento inventaria
> el sistema SDD de un repo de referencia real (`MovilSDD`) y lo contrasta contra el
> estado actual de ENVER, dejando una matriz de adaptación y la cobertura funcional
> verificada por evidencia directa. **Nada aquí está aprobado.**

- **Fecha:** 2026-11-05
- **Ejecutante:** agente OpenCode (Checkpoint 3.4)
- **Autorización:** definida en el Checkpoint 3.4 (auditoría en repos de referencia,
  correcciones menores de docs y preparación del repo; prohibido: ratificar la
  constitución, primer commit, staging, tocar código de app, inventar requisitos
  de producto, modificar el repo de referencia).
- **Método:** SOLO LECTURA sobre ENVER y sobre `MovilSDD`. Ningún script del repo
  de referencia fue ejecutado. Evidencia de comandos en §6.

## 0. Convención de estados

- `[READ]` — verificado leyendo el archivo en esta sesión (evidencia de lectura).
- `[EXEC]` — verificado ejecutando un comando en esta sesión (sección §6).
- `[PEND]` — pendiente; requiere acción humana u otra sesión (se cita el comando).
- `[INFO]` — observación sin acción inmediata.

---

## 1. Alcance

**En alcance:**
1. Inventariar el sistema SDD de `C:\AndroidProject\PruebaSSDMovilProfe\MovilSDD`
   (read-only): `specs/` (5 features + plantilla), `scripts/`, agents, `CHECKLIST_PR.md`,
   docs vivos, CI, stack. `[READ]`
2. Contrastar contra ENVER: `.specify/`, agents/skills, `CHECKLIST_PR.md`, ausencias,
   Git, tests. `[READ]`/`[EXEC]`
3. Producir matriz de adaptación (§4), cobertura funcional (§5), hallazgos (§7)
   y pendientes (§8).
4. No se modificó: código de app (Kotlin/XML/Gradle/recursos), constitución,
   agents/skills/templates existentes, Git (0 commits, 0 staged, verificado en 3.1–3.3),
   ni el repo de referencia.

**Fuera de alcance (deuda declarada):** crear `specs/` y `scripts/` en ENVER, redactar
specs FT (prohibido sin requisitos humanos), crear CI, ejecutar builds Gradle,
ratificación del checkpoint.

---

## 2. Inventario del repo de referencia (MovilSDD) — `[READ]`

### 2.1 Estructura SDD

| Elemento | ¿Presente? | Detalle verificado |
|---|---|---|
| `specs/_template.md` | Sí | Plantilla de 7 secciones: 1 Contexto y objetivo; 2 Requisitos funcionales; 3 Requisitos no funcionales; 4 Arquitectura y contratos internos; 5 Criterios de aceptación; 6 Plan de pruebas; 7 Riesgos, límites y deuda; + tabla Aprobación. |
| `specs/FT-00-base-build/` | Sí | Feature devops inicial; carpetas `spec/`, `plan/`, `tasks/`, `checklists/`. |
| `specs/FT-01-nucleo/` | Sí | Feature de negocio núcleo: `SealedResult`, `Page<T>`, `CorrelationId`, `Logger`, módulos Koin `core/data/feature`, navegación type-safe `@Serializable`. |
| `specs/FT-02-store-skeleton/` | Sí | Persistencia: DataStore + security-crypto, `backup/data-extraction rules`. |
| `specs/FT-03-gates-cierre/` | Sí | Gates de cierre: Kover ≥45%, CodeQL/Semgrep, build release. |
| `specs/FT-04-login/` | Sí | Feature auth completa (spec 13KB, plan 5.4KB, tasks 4KB; sin carpeta `checklists/`). |
| `scripts/` | Sí (solo stub) | **Únicamente `README.md`**: lista gates futuros (`critic-guardrails`, `check_endpoints-internos`, `coverage-merge`, `delta --min 80 --advisory`). **No existen scripts ejecutables.** |
| `CHECKLIST_PR.md` | Sí | 28 líneas; gate obligatorio: pre-vuelo (Memoria actualizada, spec conciliada, sin secretos), build (`assembleDebug`, `lint detekt ktlintCheck`, `testDebugUnitTest koverVerify ≥45%`, `compileDebugAndroidTestSources`), testing (unit 100%, contract `CAPTURE=1`, delta `--min 80 --advisory` no bloqueante), seguridad/SDD (`critic-guardrails` PASS, CodeQL/Semgrep 0 nuevos). |
| `AGENTS.md` | Sí | (7.1KB). |
| `Memoria.md` | Sí | (raíz). |
| `.specify/` | Sí | Solo `memory/constitution.md` + 4 templates (mismo esquema que ENVER). |
| `.opencode/agents/` | Sí | **13 agents**: 6 escritores (spec-drafter, plan-architect, task-decomposer, slice-scaffolder, test-writer, docs-keeper), 4 gates solo-lectura (layer-guardian, security-reviewer, endpoints-validator, traceability-clerk), 3 de soporte (coverage-delta-reporter, release-checker, readme-keeper alias de docs-keeper). |
| `.opencode/skills/` | No | No existen skills en la referencia. |
| `.github/workflows/ci.yml` | Sí | CI real (ver §2.3). |
| `.github/` | Sí | Únicamente `workflows/ci.yml` (nada más; no hay copilot-instructions ni templates). |

### 2.2 Enforcement de revisores en la referencia

El frontmatter de los agents de referencia solo declara `description` + `mode`:
la restricción "SOLO LECTURA" es **meramente textual en el cuerpo** (p. ej.
layer-guardian: "SOLO LECTURA: no edita código, reporta PASS/FAIL"),
sin bloques técnicos `permission`. ENVER, en cambio, tras el Checkpoint 3.2
declara `permission: {edit: deny, bash: deny, task: deny}` en sus 4 revisores
más la mención de "barrera técnica" en el cuerpo.

### 2.3 CI de referencia (`ci.yml`, leído tal cual) — `[READ]`

Pipeline (Ubuntu, JDK 21, `--no-daemon`):
`validate-wrapper` → `static-analysis` (`lint`) → `unit-test` (`testDebugUnitTest koverVerify`) →
`instrumentation-compile` (`compileDebugAndroidTestSources`) → `security-scan`
(CodeQL java-kotlin con build explícito `assembleDebug` para trazado de compilación,
.CodeQL escalado de `permissions: security-events: write`) → `build`
(solo `push` a `main`: `assembleDebug`).

### 2.4 Stack de referencia (NO trasladable)

Kotlin 2.3.20 / AGP 9.4.1 / Gradle 9.8.0 / JDK 21 / Koin 4.2.2 /
Kover cobertura mínima ≥45% global y ≥80% en código nuevo; `SealedResult`,
`rowVersion` opaco, `LoginUiState/UiEvent` (StateFlow/SharedFlow), tests con
JUnit5+MockK+Turbine. ENVER no copia versiones (la constitución fija las
propias: Kotlin 2.2.10 / AGP 9.2.1 / Gradle 9.4.1 / Compose BOM 2026.02.01 /
maps-compose 6.12.2 / JDK 21) — `[INFO]`, sin conflicto: la constitución prevalece.

---

## 3. Estado ENVER (línea base del contraste)

- `.specify/memory/constitution.md` y `.specify/templates/` (4 plantillas) — presentes `[READ]` (sesiones 3.0–3.3; reread parcial 3.4).

- `AGENTS.md`, `Memoria.md`, `CHECKLIST_PR.md` raíz — presentes `[READ]` (3.2/3.3).
- `.opencode/agents/` — **10 agents**: 6 escritores + 4 revisores con permisos técnicos deny; `.opencode/skills/` — 2 skills (`enver-maps-camera-permissions`, `enver-ui-preservation`). `[READ]` (3.1–3.3).

- `specs/` y `scripts/` — **no existen** en ENVER. `[EXEC]` (`Test-Path` 3.4 → `False`).
- Tests — **0** (no existe `app/src/test/`).

- Git — `main` sin commits, 0 staged, 87 untracked (86 previos + 1 = este informe, verificado en §6); `local.properties`, `.idea/*`, `build/`, `.kotlin/` ignorados; `gradle.properties` NO ignorado (requiere revisión humana antes del primer commit). `[EXEC]` en 3.1–3.4.
- Build `.\gradlew.bat :app:assembleDebug` — **no ejecutado** en toda la serie de checkpoints. `[PEND]`
- `AppNavHost.kt:34-37` redirige a registro (login/register sin validación) — `[INFO]`.

---

## 4. Matriz de compatibilidad de adaptación

D = decisión propuesta (no aprobada). ADOPT = adoptar tal cual; ADAPT = adaptar; DEFER = diferir; NA = no aplica.

### 4.1 Infraestructura y docs raíz

| Elemento de referencia | Estado ENVER | D | Justificación |

|---|---|---|---|
| `specs/_template.md` (7 secciones) | `.specify/templates/spec-template.md` (14 secciones incl. la de Aprobación, superconjunto) | ADOPT | ENVER ya es superconjunto; crear `specs/` solo cuando exista un primer requisito humano. |
| 5 features FT-00..FT-04 con spec/plan/tasks/checklists | No existen | DEFER | Regla dura: no se inventan requisitos. Requiere insumo humano (Checkpoint 4). |
| `scripts/README.md` (stub de gates) | `scripts/` no existe | DEFER→ADAPT | Ver §4.3; la referencia tampoco tiene scripts ejecutables, así que el gap es simétrico. |
| `CHECKLIST_PR.md` | Existe (alineado en 3.2) | ADOPT — cobertura ya implementada | |

| `AGENTS.md` / `Memoria.md` / `.specify/memory/constitution.md` | Existen (3.x) | ADOPT | |
| `.opencode/agents/*` (13 ref; 10 ENVER) | Parcial | INFO | Mapeo 1:1 en §4.2; agentes sobrantes del ref = DEFER. |
| `.opencode/skills/*` (2 en ENVER) | Existen (extra, no en ref) | INFO | ENVER va más allá por ser UI-intensiva; sin conflicto con la referencia. |
| `.github/workflows/ci.yml` (ref SÍ) | ENVER no tiene CI | DEFER | Requiere: primero build verde `[PEND]` + remoto Git + aprobación del stage appropriate. El ref entró en CI cuando ya tenía features/pipeline integrados. |
| `.gitignore` de referencia | Presente en ENVER (más completo) | INFO | El ref ignora `keys.properties`, patrones de firma (`*.jks`, `*.keystore`, `*.p12`, `*.pem`, `*.key`, `signing/`), `.env*`, `keystore.properties` — mismas familias que ENVER ya cubre (`local.properties`, keystores, `.idea/*`, `build/`, `.kotlin/`); sin acción. |

### 4.2 Agents — mapeo funcional referencia ↔ ENVER

- escritores spec-drafter ✅ / plan-architect ✅ / task-decomposer ✅ / slice-scaffolder ✅ / test-writer ✅ / docs-keeper ✅ — **6/6 en paridad 1:1**.

- revisores layer-guardian ✅ / security-reviewer ✅ / traceability-clerk↔traceability ✅ / endpoints-validator ❌ (DEFER a Fase P4+, cuando exista API) — 3/4 cubiertos; el que falta es de gates de backend.
- soporte coverage-delta-reporter ❌ (DEFER a P2, tras primer test), release-checker ❌ (DEFER a Fase P6), readme-keeper ≡ alias de docs-keeper (NA en ENVER).

- extra ENVER `visual-preservation` (NA — bien, extra de ENVER; el ref no es uno)。

**Enforcement:** referencia = textual (sin frontmatter técnico); ENVER = bloque técnico `permission:{edit,bash,task}: deny` en los 4 revisores + cuerpo. ENVER está en paridad o mejor.

### 4.3 Gates de referencia — propuesta de mapa

| Gate de la referencia (`README` de scripts + `ci.yml`) | Aplica a ENVER hoy | D | Dónde caería (propuesta, no aprobada) |
|---|---|---|---|
| `validate-wrapper` | No aplica (no hay remoto/CI) | DEFER | Con el primer CI, luego del primer commit |
| `lint` / `detekt` / `ktlintCheck` | No ejecutado; ENVER no tiene detekt/ktlint declarados | DEFER→ADAPT | Fase P2, con spec que aprueba dependencias de análisis |
| `testDebugUnitTest koverVerify` | 0 tests, kover no configurado | DEFER | Fase P2/P3, después de primer test |
| `compileDebugAndroidTestSources` | No aplicable (0 instrumented tests) | DEFER | P3+ |
| `critic-guardrails` / `check_endpoints-internos` (stubs, no implementados en ref) | No | DEFER | P4+ (requiere endpoints reales) |
| CodeQL/Semgrep | No | DEFER | P6 / hardening |
| `assembleRelease bundleRelease` + firma env | No | DEFER | P6 (hardening release) |

---

## 5. Cobertura funcional ENVER (superficie existente vs. ausente)

**Método:** greps reales 3.4 sobre `app/src/main/java/com/uttt.enverdelivery/`
(20 archivos Kotlin) + docs de audit/baseline/patrones revisados en
checkpoints previos. **Esto NO define "requisitos oficiales"** (aún no hay);
solo declara superficie existente vs. ausente, sin inventar.

| Área funcional | ¿Existe hoy en código? | Evidencia (grep 3.4, conteo real) | Riesgo/pendiente |
|---|---|---|---|
| Registro (nombre, email, teléfono…) | Sí, pantalla | `RegisterScreen.kt` (términos `entrega`×1, `tel.fono`×2) | Sin validación real (`AppNavHost.kt:34-37` solo navega a registro). |
| Login | Sí, pantalla | `LoginScreen.kt` (`repartir`×1) | Sin validación real. |
| Home (mapa/búsqueda/listado, "quiero repartir"×6, "rescatista"×3, "entrega"×2) | Sí, pantalla | | Solo mock data — sin estado. |
| Crear pedido: paso 0/1 (foto×9, direcciones, entrega×2, tarifa×17) | Sí | `CreateOrderFlowScreen.kt` | Estado del wizard en `remember` (no persiste; reset en `CreateOrderFlowScreen.kt:107`). |
| Crear pedido: paso 2 (detalle/descripcion, repartidor×2, rescatista×1, entrega×3) | Sí | `CreateOrderStep2Screen.kt` |  |
| Crear pedido: paso 3 (firma/entrega×2, "urgente"×1, "confirmaci"×2) | Sí | `CreateOrderStep3Screen.kt` | Sin backend: datos solo en memoria. |
| Wizard persist rotation/cancelar / draft | NO | — | Pendiente decisión humana #1 (rotation) y #2 (persistencia draft/sesión). |
| Notificaciones / tracking en tiempo real / llamado-notificación | NO | grep `Notifica`/`notifica`/`llamada`/`llamar`/`progreso` = 0 hits | No existe superficie; requiere requisito humano para spec. |
| Cancelar pedido / estados de pedido (cancelación) | NO | grep `cancel`/`Cancel` = 0 hits | No existe. Requiere spec. |
| Pago / checkout real | NO | grep `pago`/`Pago` = 0 hits | No existe. |
| Entregador app (rol distinto de cliente) | NO (solo textos) | grep `rescatista`/`Rescatista` en Home/Step2; `repartidor` en Step2/WelcomeSuccess | Solo terminología en pantallas, no flujo real de entregador. |
| Calificación / sanciones | NO | grep `Calificaci`/`Sanci` = 0 hits | No existe. |
| Perfil / ajustes | NO | Sin archivos de perfil. | |
| Mapa/cámara/permisos | Sí (composables/base) | ver `ENVER_CURRENT_STATE_AUDIT.md` / skills propias | Revisión real de permisos: pendiente, no ejecutada. |
| Tema/branding | Sí | `theme/Color.kt`, `Theme.kt`, `Type.kt` | Estable según audit 3.x. |

### 5.1 Disonancia terminológica detectada (informativo)
La app usa dos palabras para el mismo/similar concepto sin un requisito que
las distinga: **`Rescatista`** (HomeScreen, `CreateOrderStep2Screen`,
WelcomeSuccessScreen) y **`repartidor`** (`CreateOrderStep2Screen`,
WelcomeSuccessScreen). No existe documento oficial de dominio; el checkpoint NO
decide terminología — queda como decisión humana abierta (se registra en §7).

---

## 6. Evidencia de la ejecución (comandos reales, Bash 3.4)

```powershell
# 3.4 — Test-Path de estructura (ENVER)
Test-Path .specify\specs, specs, scripts
# -> False, False, False

# 3.4 — grep de términos funcionales (conteos reales por archivo)
# dentro de app/src/main/java/com/uttt.enverdelivery/**
'entrega' -> RegisterScreen.kt:1, CreateOrderFlowScreen.kt:2, CreateOrderStep2Screen.kt:3, CreateOrderStep3Screen.kt:2, HomeScreen.kt:2, WelcomeSuccessScreen.kt:1
'foto/Foto'    -> CreateOrderFlowScreen.kt:9
'tarifa'       -> CreateOrderFlowScreen.kt:17
'rescatista'   -> CreateOrderStep2Screen.kt:1, HomeScreen.kt:3 (¡y 'Rescatista' igual!)
'repartidor'   -> CreateOrderStep2Screen.kt:2, WelcomeSuccessScreen.kt:1
'repartir'     -> LoginScreen.kt:1, HomeScreen.kt:6
'firma'        -> CreateOrderStep3Screen.kt:2
'urgente'      -> CreateOrderStep3Screen.kt:1
'tel.fono'     -> RegisterScreen.kt:2
'confirmaci'   -> CreateOrderStep3Screen.kt:2
'cancel','Notifica','Notifica','pago','Pago'      -> 0 hits
'califica','sanci','encuesta','presupuesto','llama.r' -> 0 hits

# 3.4 — inventario de fuentes Kotlin (20 archivos listados)
Get-ChildItem app\src\main\java\com\uttt.enverdelivery -Recurse -Filter *.kt
# -> 20 rutas listadas (MainActivity, navigation 3x, components 5x, screens 8x, theme 3x)

# 3.4 — reads de la referencia (SOLO LECTURA, scripts NO ejecutados)
# specs/_template.md ; FT-04 tasks.md ; CHECKLIST_PR.md ; ci.yml ; frontmatter de agents
```

**NO se ejecutó**:
- `.\gradlew.bat :app:assembleDebug` — pendiente de autorización de sesión (no se
  necesita para esta auditoría de inventario).

- Cualquier comando en MovilSDD (regla dura del checkpoint: repos de referencia
  SOLO LECTURA, scripts nunca ejecutados).

- Nada de git add/commit/push. `git status`/`git check-ignore` sí se ejecutaron
   en checkpoints 3.1–3.3 (sus evidencias están en sus respectivos registros).
   Git verificado en 3.4: 0 commits, 0 staged, 87 untracked
   (86 previos + 1 por la creación de este informe; estable al cierre).

---

## 7. Hallazgos

1. **[PENDIENTE-HUM] Requisitos funcionales de ENVER no existen** — hasta que un
   humano los escriba, NO se crea ningún `specs/FT-*` (regla dura "no inventar").

2. **[HALLAZGO-TERMINOLOGIA] `Rescatista`/`repartidor` coexisten** (§5.1) sin
   documento de dominio que las distinga; el checkpoint no decide. Se requiere
   decisión humana antes de redactar cualquier spec del dominio.
3. **[PENDIENTE-HUM] La ratificación del Checkpoint 3.x continua pendiente**, de
   lo que depende toda la serie P0–P6 de fases propuestas (no ratificadas).

4. **[INFO] `scripts/script_sdd.sh` del ref:** en realidad **no existe** (la
   carpeta solo contiene `README.md` con gates previstos, no implementados). El
   "flujo completo de script de gates" del ref es un diseño sin implementar:
   ENVER está en paridad real (ninguno tiene gates ejecutables), y la
   adaptación de gates quedaría limitada a idear los mismos stubs.

   Nota: el ref **sí** implementó el pipeline de gates en CI (`ci.yml`); la
   diferencia es que sus gates corren en GitHub Actions, no vía script local.
5. **[INFO] Adoption de agents:** mapeo ENVER↔ref es paridad o mejor en los
   10 agents locales; los agentes que ENVER no tiene (endpoints-validator,
   coverage-delta-reporter, release-checker, readme-keeper) no son de bloqueo
   para el estado actual de ENVER (sin backend, sin tests, sin release).

6. **[INFO] ENVER no puede ejecutar **aún** el flujo del ref al 100%** con los
   artefactos actuales: faltaría el pipeline CI real (remoto + build verde) y
   un primer requisito humano para specs. No es un defecto, es el estado
   natural de un repo pre-primer-commit.
7. **[INFO] Stack:** la referencia usa stack más nuevo (Kotlin 2.3.20/AGP
   9.4.1/Gradle 9.8.0), incompatible con las fixaciones de la constitución de
   ENVER (Kotlin 2.2.10/AGP 9.2.1/Gradle 9.4.1). NO se propone alinear
   versiones; la constitución manda (salvo decisión humana explícita).

---

## 8. Pendientes y próximos pasos (Checkpoint 4 / humano)

1. **[HUM]** Ratificar el Checkpoint 3 (incl. esta doc §4/§7 y su trazabilidad con
   `Memoria.md`).
2. **[HUM]** Definir requisitos funcionales de ENVER (post-checkpoint-4) para
   poder abrir `specs/FT-XX` sin inventar (con la disonancia terminológica de
   §5.1 resuelta o al menos decidida).
3. **[HUM]** Autorizar y ejecutar `.\gradlew.bat :app:assembleDebug` una vez, y
   registrar la evidencia (log) en `Memoria.md` — primera evidencia de build en
   la serie. NO ejecutado en 3.4.
4. **[HUM decide]** Si se quiere, sesiones dedicadas para: (a) `scripts/` con
   stubs de gates (equivalente al ref), y/o (b) CI con el pipeline del ref —
   ambas `DEFER` y requieren build verde + remoto + aprobación.
5. **[OPCIONAL]** Archivo de referencia de lectura para el revisor: esta doc ya
   inventorya la referencia; si se desea extracto tal cual de un artefacto del ref,
   se necesita permiso humano explícito (el checkpoint anterior NO lo copió).

---
*Fin del informe del Checkpoint 3.4. Estatus: BORRADOR — la ratificación
corresponde al propietario; nada de esto modifica código, agents, plantillas ni el repo de referencia.*
