# ENVER — Propuesta de migración SDD (Checkpoint 2: diseño pendiente de aprobación)

> Fecha: 2026-11-05
> Estado: PROPUESTA. **Nada aquí está aprobado ni implementado.** La constitución definitiva, los agentes, skills, plantillas y specs se crean SOLO tras la revisión del usuario.
> Contraste basado en: `docs/ENVER_CURRENT_STATE_AUDIT.md` (estado real ENVER) y `ENVER_SDD_REPOSITORY_AUDIT.md` (referencia MovilSDD, copia en `C:\AndroidProject\PruebaSSDMovilProfe\MovilSDD\docs\`).
> Principio rector de la propuesta: **adaptar la gobernanza de MovilSDD, no copiar sus decisiones de stack** (Koin, Retrofit, kover ≥45%, estructura de paquetes Clean 3 capas NO se consideran requisitos para ENVER).

---

## 1. Matriz comparativa ENVER vs MovilSDD (Fase C)

Vías de evidencia abreviadas: [A]=`docs/ENVER_CURRENT_STATE_AUDIT.md` + ruta citada; [R]=`ENVER_SDD_REPOSITORY_AUDIT.md` (sección).

| # | Área | Estado actual de ENVER | Referencia MovilSDD | Brecha comprobada | Propuesta | Riesgo | Evidencia |
|---|---|---|---|---|---|---|---|
| 1 | Constitución | No existe | `.specify/memory/constitution.md` + espejo raíz; manda ante conflicto | Confirmada: inexistente | Crear constitución ENVER (principios en §3.2), corta, con espejo | Bajo | [A §2.9]; [R §5] |
| 2 | AGENTS.md | No existe | Manual operativo de 12 secciones; lo invocan todos los agentes | Confirmada | Crear AGENTS.md (cómo trabajar), separado de la constitución | Bajo | [A §2.9]; [R §4], [R §6] |
| 3 | Memoria | `docs/CHANGELOG.md` actúa como memoria informal (historial de etapas con porqués parciales) | `Memoria.md` con Estado/Decisiones/Errores a evitar | Parcial: hay historial, falta estructura de decisiones y lecciones | Crear `Memoria.md` y decidir el rol de CHANGELOG (mantener como bitácora de releases; Memoria como fuente de decisiones) — evitar duplicar fuentes | Medio (confusión de fuentes) | [A §2.10]; [R §7] |
| 4 | Plantillas SDD | Ninguna | 4 plantillas (spec 7 secciones + Aprobación 4 campos, plan, tasks, checklist) | Confirmada | Adaptar plantillas al dominio ENVER (incl. wizard de pedidos, preservación visual y mapa/cámara) | Bajo | [R §8] |
| 5 | Organización de specs | Sin specs; solo CHANGELOG por etapas | `specs/FT-0X/{spec,plan,tasks,checklists}`; "sin cambio de contrato interno sin spec" | Confirmada | Establecer `specs/` con la misma convención (nombra features propias; documentar retroactivamente la línea base como primer spec) | Bajo | [A §2.10]; [R §8], [R §9] |
| 6 | Agentes | Sin `.opencode/` | 13 agentes (4 SDD, 1 test, 5 gates solo-lectura, 3 cierre), deny-list común | Confirmada | Equipo reducido (§4): ~7 agentes; declarar límites honestos (prompt-only, ver §4.3) | Bajo si se evita sobre-ingeniería | [A §2.9]; [R §6], [R §11.3] |
| 7 | Skills | Ninguna | 10 skills (clean, compose-mvvm, koin, testing, devsecops, ui-design, etc.) | Confirmada | Solo 3-5 skills propias de ENVER (compose-básico, preservación-visual, mapa-cámara-permisos, etc. — §4.4) — no copiar skills de Koin/Retrofit | Bajo | [R §6] |
| 8 | Arquitectura Android | Single module; UI monolítica por funciones; **sin** ViewModel/DI/dominio/datos; estado con `remember` | Clean 3 capas + Koin + stateless UI | Confirmada (aunque no "incorrecta": es coherente con etapa visual) | Arquitectura objetivo ENVER: UI + stateholders (ViewModel) + modelo de pedido; domain/datos solo cuando exista backend real — ver §2.2 | Medio (refactor de estado) | [A §7]; [R §10] |
| 9 | Navegación | 5 rutas string + NavigationActions; secciones/pasos por estado interno | Rutas type-safe `@Serializable` + NavHost centralizado | Parcial: funciona pero no type-safe; las transiciones/popUpTo correctos ya documentados | Mantener el grafo actual; decisión abierta sobre migrar rutas type-safe y/o convertir secciones en rutas (cambia back behavior) — requiere aprobación + validación visual | Alto (riesgo visual/back) | [A §4]; [R §10] |
| 10 | Estado compartido (wizard) | `CreateOrderUiState` mutable hoisted a `MainScaffold` con `remember` (pierde rotación/proceso); `currentStep` se reinicia | Stateless UI + DI del estado compartido | Confirmada: comportamiento frágil documentado (A §7 problema 1) | Normalizar en ViewModel compartido (scope de MainScaffold/app) preservando el comportamiento visible; decisión sobre persistencia del borrador → humana | Alto por dependencia de flujo | [A §5, §7]; [R §18.3] |
| 11 | Seguridad | Permisos correctos y mnímicos; MAPS_API_KEY vacía; release sin minify; allowBackup=true; sin network config | Hardening total (R8, allowBackup=false, cleartext=false, logs sanitizados, store cifrado) | Confirmada con matices: ENVER está en etapa visual; endurecer es nueva regla, no defecto urgente | Principios de seguridad en constitución (mínimo privilegio, sin secretos en código, Map key via gradle.properties) + gates a definir según endurecimiento aprobado | Medio | [A §8]; [R §11] |
| 12 | Pruebas | 0 tests propios (solo plantillas JUnit4/Compose sí configuradas en deps) | 21 archivos, 88 tests, fixtures de contrato, kover ≥45% | Confirmada | Estrategia incremental: primero composición/estructura mínima (p. ej. tests de validadores y estado del wizard); los umbrales de cobertura se definen tras medir, NO copiar ≥45%/80% sin evidencia | Medio (coste de arranque) | [A §2.7, §6]; [R §12] |
| 13 | Gates y CI | Solo `assembleDebug`/`compileDebugKotlin` (del historial). Sin lint config, sin detekt/ktlint, sin CI, **sin Git** | 6 jobs CI + gates locales ordenados + CHECKLIST_PR + tags | Confirmada | Gates progresivos por etapa (§6); el orden mínimo es assembleDebug → lint (default) → tests cuando existan. CI requiere Git primero | Alto si no se secuencia bien | [A §2.7-§2.8]; [R §9], [R §14] |
| 14 | Documentación | 3 docs (CHANGELOG, FRONTEND_ARCHITECTURE — desincronizada —, NAVIGATION_FLOW) | README + docs vivos (endpoints, rate-matrix, ASVS) + specs + memoria | Parcial: buena semilla, con drift | Congelar fuentes: una sola documentación viva por tema en `docs/`; FRONTEND_ARCHITECTURE se actualiza al refactor (no antes) | Bajo | [A §2.10] |
| 15 | Preservación visual | Línea base recién creada: `docs/ENVER_UI_BASELINE.md` | N/A (MovilSDD no lo necesita como regla) | Nueva en ENVER: regla propia | Práctica obligatoria: spec de refactor → lista de elementos a conservar → validación (capturas o code-review) → memoria | Alto si se omite | [B docs/ENVER_UI_BASELINE.md] |
| 16 | Mapa y cámara | Maps Compose real + Geocoder + FusedLocation; cámara FileProvider. Mapa no funcional por falta de API key. **Por determinar**: el mapa en runtime | N/A | Por determinar (runtime no probado) | Decisión pendiente de usuario: proveer MAPS_API_KEY y validar en dispositivo antes del primer refactor del wizard | Alto | [A §6, §8]; [R §18.3] |
| 17 | Trazabilidad de decisiones | CHANGELOG con "problemas y soluciones" por etapa; sin Git | Memoria (Decisiones con porqué) + specs con Aprobación + tags git | Confirmada | Aprobar la adopción/regla de Git (init+commits por slice) como requisito de trazabilidad; cada decisión con porqué ira a Memoria.md | Alto (rollback) | [A §2.8]; [R §7, §13] |

Brechas marcadas "por determinar" según instrucción: legalidad del mapa en runtime (#16), comportamiento real del backstack al convertir secciones a rutas (#9), comportamiento del Geocoder por dispositivo (#16).

---

## 2. Propuesta de arquitectura adaptada a ENVER (resumen, sin implementar)

### 2.1 Lo que NO se propone
- No copiar Clean 3 capas con `domain/` vacías ni módulo DI con Koin "porque MovilSDD": ENVER no tiene backend ni acoplamiento que lo exija hoy.
- No introducir Retrofit/API client/Koin/Turbine/MockK sin necesidad demostrada en un spec.
- No cambiar dependencias/versiones del build en la primera etapa (la restricción de compatibilidad Kotlin 2.2.10 ↔ maps-compose 6.12.2 está documentada; romperla rompe la compilación — A §2.2).

### 2.2 Dirección propuesta (basadas en el estado real)
1. **Módulo único** se mantiene; la separación se hace por paquetes.
2. **Stateholders**: los `remember`-clusters con lógica (MainScaffold section+order state) evolucionan a **ViewModel** (androidx.lifecycle; dependencia ya viene con lifecycle-runtime) cuando se toque cada pantalla por slice. Se conserva la convención de pantallas con callbacks.
3. **Modelos de UI** (`OrderItem`, borrador de pedido, etc.) van a un paquete `model/` cuando el primer refactor los comparta entre pantallas — no antes.
4. **Datos/persistencia**: cuando se apruebe un backend o el borrador del pedido deba sobrevivir al proceso, se abrirá el spec formal primero (FR de pedidos), evitando dar por buena cualquier lib de storage tercerizada sin necesidad.
5. **Tema**: se decidirá entre (a) `dynamicColor=false` con esquema estático de ENVER o (b) convertir el colorScheme Material a la paleta — decisión del usuario en la constitución; mientras tanto se prohíbe introducir más colores inline nuevos.

Es deliberadamente mínima: la arquitectura madura vendrá de los specs de cada slice, no de una decisión estructural única inicial.

---

## 3. Propuesta de jerarquía documental y constitución

### 3.1 Jerarquía (D.1)

| Documento | Responsabilidad | Autoridad | Fuente única de... |
|---|---|---|---|
| `.specify/memory/constitution.md` (+ espejo en raíz `constitution.md`) | Principios no negociables de ENVER | **Máxima**; gana a todo lo demás | Principios |
| `AGENTS.md` | Manual operativo: cómo se trabaja, qué no se hace, gates | Media-alta (subordinada a constitución) | Procedimientos |
| `Memoria.md` | Estado actual, Decisiones con porqué, Errores a evitar | Contexto; se lee al empezar/actualiza al terminar | Historial de decisiones |
| `CHECKLIST_PR.md` | Checklist de gate antes de merge/final de slice | Obligatorio por constitución | Checklist de PR |
| `.specify/templates/` | Plantillas spec/plan/tasks/checklist | Definición de forma | Estructura de artefactos |
| `specs/<slice>/` | Requisitos, plan y tareas por feature (con tabla Aprobación) | Contratos por feature | Requisitos por feature |
| `docs/` | Documentación técnica viva (incl. los 3 docs de auditoría; CHANGELOG como bitácora) | Descriptivo | Diversos (arquitectura, navegación, línea base visual) |
| `.opencode/agents/` | Sub-agentes con límites declarados | Procedimiento | Delegación |
| `.opencode/skills/` | Conocimiento técnico reutilizable del dominio ENVER | Consulta; pierde ante constitución | Know-how |

**Resolución de contradicciones** (de mayor a menor): constitución > AGENTS.md > spec de feature vigente > Memoria/CHANGELOG (historial) > docs técnicos > skills > prompts de agentes. Si docs/ y Memoria discrepan entre sí, gana Memoria (fecha más reciente) y se corrige el doc en el mismo slice. Si los 3 documentos de auditoría se desajustan con el código, gana el código y se anota el drift en Memoria.

### 3.2 Constitución propuesta (D.2) — principios, NO texto final

1. **Preservación visual obligatoria**: todo refactor con UI a la vista debe producir el render idéntico; las fuentes de verdad son el código previo + `docs/ENVER_UI_BASELINE.md` + capturas cuando existan. Cambios visuales solo vía spec con aprobación.
2. **Arquitectura y dependencias:** módulo único; estado visible vía ViewModel cuando el slice lo toque; pantallas con callbacks (sin NavController en pantallas); paleta desde `ui.theme` con colores inline legacy permitidos **solo para preservar**; prohibido añadir dependencias sin justificación en spec (nota la restricción Kotlin 2.2.10 ↔ maps-compose 6.12.2).
3. **Responsabilidad UI/estado/dominio/datos según necesidad real**: sin capas ceremoniales; `model/` y capa de datos nacen cuando existan datos reales.
4. **Estado compartido del wizard**: una sola fuente del borrador de pedido; definición aprobada de qué sobrevive (sección activa, paso actual, campos, foto, tiempo) a rotación/proceso, ahora duración "vida de la app" naturalizada explícitamente.
5. **Navegación y errores**: rutas nuevas solo en NavRoutes/NavigationActions; comportamiento de backstack documentado es contrato visual; errores de validación con los mensajes existentes; prohibido tragar excepciones en silencio (regla nueva frente a Geocoder actual).
6. **Seguridad y privacidad**: mínimo privilegio en permisos; sin secretos en código/logs (MAPS_API_KEY solo por propiedad Gradle); ubicación como dato sensible; sin PII en logs; endurecimiento (minify/allowBackup) decidido por el usuario y registrado, no por defecto.
7. **Pruebas y calidad**: gates mínimos por slice (compilar + lint) y tests nuevos cuando toque lógica; cobertura definida tras primera medida, no threshold importado.
8. **Dependencias**: cada dependencia nueva con porqué de negocio, versión compatible verificada y registro en Memoria; regla de excepción contenida en un solo spec (como FT-00 de la referencia).
9. **Deuda técnica**: la deuda se nombra, se registra (Memoria/spec §Riesgos) y se planifica; cada placeholder (Paso 4, Pedidos, Perfil, recuperación de contraseña, Remote image) es deuda declarada con dueño.
10. **Aprobación humana**: un spec entra en vigencia con: revisor humano + fecha + CHECKLIST_PR OK + sin conflicto con constitución (igual que la referencia). Cambios de alcance requieren nueva revisión, no inclusión silenciosa.
11. (Añadida ENVER) **Prohibido arreglar de más**: los slices no "aprovechan" para reescribir pantallas no contempladas ni modernizar estilos paralelamente al refactor.

### 3.3 Decisión abierta sobre el espejo de la constitución
La referencia usa espejo bit-a-bit (`.specify/memory/` + raíz). En ENVER, ¿espejo o solo raíz? (Menor; recomendable espejo por visibilidad, misma práctica verificada en la referencia.)

## 4. Agentes y skills (D.3)

### 4.1 Evaluación de roles (¿agente independiente o skill/instrucción?)

| Rol | ¿Agente separado? | Justificación |
|---|---|---|
| Coordinación | No (agente principal) | El agente central ya rectora con constitución+AGENTS; añadir coordinador redundante |
| Redacción de specs (spec-drafter) | **Sí** | Escritura disciplinada de un artefacto específico; evita que el implementador reescriba requisitos sobre la marcha |
| Arquitectura/planes (plan-architect) | **Sí** | Decide archivos/contratos afectados y validación vs constitución; beneficio claro separarlo del autor del spec |
| Descomposición de tareas (task-decomposer) | **Sí** | Tareas atómicas con comando de verificación; es lo que habilita slices verificables |
| Implementación (slice-scaffolder) | **Sí** | Debe ser el único que toca código de producción, con alcance cerrado al plan |
| Pruebas (test-writer) | **Sí, desde la etapa en que existan tests** | En ENVER nace junto con la primera suite; antes sería un agente innecesario |
| Revisión de arquitectura (layer-guardian) | Sí, **solo-lectura** | Gate de consistencia con AGENTS (convención de paquetes, estado, tema); reporta, no edita |
| Seguridad (security-reviewer) | Sí, **solo-lectura** | Revisión de permisos/secreto/datos; indispensable una vez haya red real o persistencia |
| Trazabilidad (traceability-clerk) | Sí, **solo-lectura** (o fusionado con docs-keeper) | Verificar requisitos↔código↔memoria; puede combinarse con el docs-keeper si el equipo es pequeño |
| Documentación (docs-keeper) | **Sí** (edita solo `**/*.md`) | Actualizar docs/Memoria/CHANGELOG sin tocar código |

### 4.2 Matriz de agentes propuesta (7 total)

| Agente | Responsabilidad | Archivos que puede editar | Acciones prohibidas | Entradas | Salidas | Verificación |
|---|---|---|---|---|---|---|
| (principal, implícito) | Coordinar, leer contexto, decidir | Cualquier md de fecha (no código) | Implementar sin plan aprobado | Todo | Dirección + orden | Regla en AGENTS |
| `spec-drafter` | Escribir spec de slice | `specs/<slice>/spec.md` (nuevo) | Código; declarar Aprobado por sí mismo | Plantilla + solicitud usuario + línea base | Spec en Borrador | Sección Aprobación pendiente de humano |
| `plan-architect` | Diseño técnico, archivos afectados, gates | `specs/<slice>/plan.md` | Código; ampliar alcance | Spec aprobado | Plan | Cross-check contra constitución y listas de archivos reales |
| `task-decomposer` | Tareas atómicas + comando de verificación | `specs/<slice>/tasks.md` | Código; tareas sin verificación | Plan | tasks.md | Cada tarea tiene comando/PoC verificable |
| `slice-scaffolder` | Implementar según plan aprobado | Código Kotlin/res relevantes de su slice (lista en plan) | Fuera del alcance; dependencias nuevas sin spec; `local.properties`/`gradle.properties`; secretos | Plan + tasks aprobados | Código + entradas en Memoria | Gates locales en verde + preservación visual |
| `test-writer` (etapa 2+) | Escribir tests del slice | `app/src/test/...`, fixtures de test | Tests falsos/always-pass; código de producción | Plan + código | Tests verdes | `gradlew :app:testDebugUnitTest` |
| Gates solo-lectura (`layer-guardian`, `security-reviewer`, `visual-preservation-checker`, `traceability-clerk`) | Reportar PASS/FAIL con evidencia | nada | editar; ejecutar builds destructivos | Repo (lectura) | Informe con paths | Evidencia por ruta en cada reporte |
| `docs-keeper` | Actualizar docs/Memoria/CHANGELOG | `**/*.md` | Código; recursos | Resultado del slice | Docs conciliados | Diff Git de md only |

Deny-list transversal (igual que referencia): nunca editar/leer-valores de `local.properties`, `gradle.properties` (permitido apéndice de propiedad MAPS_API_KEY solo por instrucción exprés del usuario), claves/keystore; no ejecutar acciones destructivas (`clean`, borrar datos generados) sin permiso.

### 4.3 Límite honesto de aislamiento
Igual que en la referencia, **los permisos de agentes son declarativos (prompt)** — no existe enforcement técnico en OpenCode más allá del modo subagent. El AGENTS.md y los prompts deben decirlo: la contención real es el proceso (diff de Git revisado + gates). Con Git aprobado (Etapa P0), el flujo de diff revisado es el correctivo principal.

### 4.4 Skills propuestas (3-5, propias de ENVER)

| Skill | Contenido | Justificación |
|---|---|---|
| `compose-state-basics` | Estado con `remember/rememberSaveable`, state hoisting, ViewModel básico, errores habituales del codebase | Base transversal reutilizable en todos los slices de UI |
| `enver-ui-preservation` | Cómo verificar preservación visual con la línea base doc; checklist por pantalla; qué no tocar | Skill ENVER específica — no existe ni en la referencia |
| `maps-camera-location` | Descripción precisa del uso en ENVER: maps-compose 6.12.2 + permisos + Geocoder + FileProvider; restricción de compatibilidad Kotlin/maps; qué rompe el build | El dominio de mapa/cámara/permisos es único de ENVER (identificado en [R §18.2]) |
| `build-and-gates` | Comandos reales por etapa, orden de gates, qué significa cada resultado | Reduce dependencia de memoria de agentes |
| (opcional) `test-basics-compose` | Cómo escribir primer test con la suite ya declarada (JUnit4/compose-junit4 deps ya existentes) | Solo desde la etapa de tests |

No se crearán skills de Koin/Retrofit/security-crypto (no aplican a ENVER hoy).

## 5. Flujo SDD propuesto (D.4)

| Paso | Qué | Automatizable | Aprobación humana |
|---|---|---|---|
| 1. Contexto | Leer constitución+AGENTS+Memoria (+spec relacionado) | Auto (regla) | No |
| 2. Spec | Alcance + FUERA de alcance; requisitos con evidencia por ruta | spec-drafter | No (queda Borrador) |
| 3. Criterios de aceptación | Given/When/Then verificables por pantalla funcional | spec-drafter | En conjunto con 4 |
| 4. Plan | Arquitectura, archivos afectados y NO afectados, docs a actualizar | plan-architect | **Sí** (aprobación de alcance+plan antes de tocar algo) |
| 5. Tasks | Tareas pequeñas con su comando de verificación | task-decomposer | Revisión rápida (no bloqueante) |
| 6. Aprobación | Firma del spec (4 campos) | — | **Sí, obligatoria** |
| 7. Implementación | Solo tareas del plan; registro en Memoria al terminar | slice-scaffolder | No (pero diff revisable) |
| 8. Pruebas y gates | assembleDebug → lint → tests (si hay) → agentes gates | Auto concertado | No |
| 9. Revisión seguridad/arquitectura | security-reviewer + layer-guardian con reportes | Auto con humano si FAIL | Sí cuando se introduzcan permisos/red/persistencia |
| 10. Preservación visual | Checklist por pantalla según ENVER_UI_BASELINE (+ capturas cuando existan) | Auto (evidencia listada) | Sí: comparación y aceptación |
| 11. Memoria/docs | Memoria + CHANGELOG + docs afectados conciliados | docs-keeper | No |
| 12. Cierre | Registro de deuda/pendientes; estado en Memoria | — | Rol del usuario al revisar el slice |

Redacción mínima: **todo cambio de comportamiento visible pasa por 10; todo spec nuevo pasa por 6.**

## 6. Estrategia de pruebas y gates

- **Gates disponibles hoy (sin tocar nada):** `.\gradlew.bat :app:assembleDebug`, `.\gradlew.bat :app:compileDebugKotlin`, `.\gradlew.bat :app:lint` (lint Android por defecto), `.\gradlew.bat :app:testDebugUnitTest` (no hará nada sin tests: gate de estructura).
- **Gates propuestos por etapa:** P0 (solo assembleDebug + init Git). P1 (añadir lint como gate local). P2+ (tests unitarios/compose por slice; gates de agentes solo-lectura con reporte textual, no tooling nuevo). Después (CI opcional cuando Git esté activo).
- **Prohibido so far:** copiar kover threshold de MovilSDD (el propio reference considera el gate vacuo por incompatibilidad Kover/AGP — [R §16.4]); copiar su CI con detekt/ktlint si ENVER no tiene la configuración equivalente; activar CI antes de Git.
- Las verificaciones visuales se documentan en cada PR con la checklist de `docs/ENVER_UI_BASELINE.md §6` + capturas listadas en §7 de esa doc cuando hayan sido tomadas.

## 7. Estrategia de migración por etapas (D.5)

**Principio:** etapas pequeñas; línea base funcional/visual primero; el comportamiento visible NO cambia salvo que se apruebe. Nombres de features FT-XX definitivos solo tras aprobación; aquí se usan códigos provisionales P0…P6.

### P0 — Base de trazabilidad (prerequisito de todo)
- Objetivo: poder ver qué cambió nunca. Iniciar Git (+ primer commit del estado actual), .gitignore compatible (ya incluye local.properties); decidir si `gradle.properties` se ignora (hoy no está en .gitignore → riesgo de commit accidental de MAPS_API_KEY).
- Alcance: solo repositorio; sin tocar código.
- Afectados: `.git/`, `.gitignore` (apéndice), posible `.gitattributes`.
- NO tocar: todo el código y recursos.
- Riesgos: .idea/ parcialmente trackeable (ignora o ignore parcial — decisión); Git LFS no necesario.
- Dependencias: ninguna.
- Criterios de aceptación: `git status` limpio tras commit inicial; historial con fecha.
- Verificación: `git log --oneline`; `git check-ignore local.properties`.
- Evidencia: el propio commit inicial como estado congelado.
- Rollback: eliminar `.git/` (no afecta archivos).

### P1 — Instalación metodológica (docs + plantillas)
- Objetivo: métodos sobre la base real, sin código.
- Alcance: constitución (aprobada), AGENTS.md, Memoria.md, CHECKLIST_PR.md, `.specify/templates/`, primer spec retroactivo "línea base ENVER" (documenta el estado actual como slice 0). Copia del informe MovilSDD a `docs/ENVER_SDD_REPOSITORY_AUDIT.md` (la ruta está libre; si el usuario prefiere mantener la copia externa, se referencia en Memoria).
- NO tocar: código, recursos.
- Riesgos: sobre-documentación (limitar a lo aprobado).
- Verificación: revisión humana de docs; sin cambios en `git diff --stat` fuera de `docs/` y plantillas.
- Rollback: revertir commits de docs.

### P2 — Endurecimiento mínimo de build + primeros tests estructurales
- Objetivo: red de seguridad mínima antes de refactor.
- Alcance: (opcional, requiere aprobación) activar `lint` como gate en text; eliminar plantillas `ExampleUnitTest/ExampleInstrumentedTest` (deuda trivial declarada); primer test real (p. ej. sanitizador de tarifa extraído a utility con test) — este segundo punto ES un cambio de código pequeño y se aprueba en su spec.
- Afectados: `app/src/test/`, (paquete nuevo `util/` solo si se aprueba extraer).
- NO tocar: pantallas, navegación, tema, manifest.
- Riesgos: scope creep ("aprovechar para refactorizar") — explícitamente prohibido.
- Verificación: `testDebugUnitTest` verde; assembleDebug verde.
- Rollback: revertir commit del slice (trivial con Git de P0).
- Evidencia: salida del test + diff.

### P3 — Estado del wizard (refactor con preservación visual)
- Objetivo: eliminar los problemas de estado (sección/step perdidos en rotación; reinicio del paso al volver a la sección) preservando la UI.
- Alcance: MainScaffold + CreateOrderFlow + Step2/Step3 → ViewModel compartido del wizard; decidir con el usuario: ¿el paso actual y los datos deberían sobrevivir a rotación? (cambio de comportamiento POR DETERMINAR — ver §9).
- Afectados: `MainScaffold.kt`, `CreateOrderFlowScreen.kt`, `CreateOrderStep2Screen.kt`, `CreateOrderStep3Screen.kt`.
- NO tocar: Login/Register/Home/Welcome, tema, manifest, build files.
- Riesgos: regresión visual del flujo; requiere capturas (baseline §7.5-7.8) antes y después.
- Dependencias: P0-P2; capturas de la UI baseline.
- Verificación: assembleDebug + lint + tests existentes + checklist §6 de baseline.
- Rollback: revert commit del slice.

### P4 — Login/Registro: representar estado y validación
- Objetivo: solo cuando se apruebe introducir flujos reales de cuenta: ViewModel de auth + validación compartida; si no hay backend, dejar el flujo de demostración documentado — NO romper el flujo actual.
- Se define según la decisión del usuario (ver §9).

### P5 — Pedidos secciones, perfil y orden real
- Objetivo: reemplazar placeholders por pantallas reales cuando existan diseños (dependiente del usuario).
- Cada una es un slice propio con spec; nada aquí se planifica sin diseño entregado.

### P6 — Endurecimiento de seguridad y release
- Endurecimiento (minify, allowBackup, network config, CI) según las decisiones de constitución §3.2.6; solo con dispositivo real y verificación reproduzible.

Cada etapa se desglosa en un futuro spec aprobado por el usuario; NINGUNA de ellas declara FT-nombre definitivo ni arranca de forma autónoma.

---

## 8. Riesgos agregados

| Riesgo | Impacto | Mitigación |
|---|---|---|
| Refactor de estado (P3) cambia comportamiento visible (paso/sección al rotar) | Alto | Decisión previa del usuario + capturas antes/después + diff revisado en Git |
| Mapa sin API key: las regresiones del mapa no son detectables sin dispositivo | Alto | Proveer API key antes de tocar el wizard; capturas obligatorias |
| Sin Git hasta P0: rollback imposible | Alto | P0 debe ejecutarse primero; commits por slice |
| Dependencias nuevas rompen build (Kotlin 2.2.10 vs libs) | Medio | Regla de compatibilidad en constitución + versiones "candado" documentadas en CHANGELOG/Memoria |
| Sobre-ingeniería metodológica (demasiados agentes/skills) | Medio | Equipo reducido §4.2; ningún agente sin función comprobada |
| Documentación duplicada (CHANGELOG vs Memoria) | Bajo | Roles definidos: CHANGELOG = bitácora; Memoria = decisiones/estado |

## 9. Dudas y decisiones pendientes de aprobación humana

1. **Comportamiento de estado**: ¿debería el borrador del pedido (y el paso actual/sección activa) sobrevivir a rotación y/o muerte de proceso? Cambia el comportamiento visible actual (A §7.1) — requiere decisión.
2. **Persistencia**: ¿incluir ahora el almacenamiento del borrador/sesión (DataStore/Encrypted...) en el alcance, o seguir sin persistencia hasta backend?
3. **MAPS_API_KEY**: ¿existe una clave real para validar el mapa en dispositivo antes de tocar el wizard?
4. **Endurecimiento**: ¿minify en release, `allowBackup=false`, network security config, CI? (constitución §3.2.6).
5. **Rutas type-safe**: ¿migrar NavRoutes a Navigation type-safe y convertir las secciones/pasos en rutas reales, o mantener el modo actual por estado? (cambia el backstack).
6. **Git**: ¿confirmar el init de Git como primera etapa (P0)?
7. **Espejo de constitución**: ¿espejo bit-a-bit + raíz, o solo un archivo?
8. **Equipo de agentes**: aprobar los 7 agentes §4.2 y deny-list transversal.
9. **Nombre de features**: aprobar el uso de convención FT-XX y la numeración; los nombres P0-P6 son provisionales.
10. **Informe de referencia**: ¿copiar `ENVER_SDD_REPOSITORY_AUDIT.md` a `docs/` de ENVER, o dejar la referencia externa citada?

## 10. Cierre para el usuario (checklist antes de aprobar la propuesta)

- ¿El diagnóstico (doc 1) coincide con su experiencia con la app?
- ¿La línea base visual (doc 2) describe los comportamientos que no se deben romper? ¿Falta alguno?
- ¿La arquitectura mínima §2.2 respeta sus planes de backend?
- ¿Está de acuerdo con reducir el equipo de agentes (§4.2)?
- ¿Orden de etapas §7 correcto (P0 Git primero)?
