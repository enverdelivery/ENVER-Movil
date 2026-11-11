# AGENTS.md — Reglas operativas para cualquier agente de ENVER

> Subordinado a `.specify/memory/constitution.md`. Si esta guía contradice la constitución, gana la constitución.
> Convención de este documento: **OBLIGATORIO** = regla dura (su incumplimiento invalida el trabajo). **RECOMENDACIÓN** = mejora práctica que el agente puede omitir con justificación.

## 1. Día de trabajo de un agente (OBLIGATORIO)

1. Leer, antes de proponer cualquier cambio: `.specify/memory/constitution.md` + `Memoria.md`.
2. Consultar por relevancia de la tarea:
   - `docs/ENVER_CURRENT_STATE_AUDIT.md` (estado real, evidencia por ruta).
   - `docs/ENVER_UI_BASELINE.md` (si la tarea puede tocar UI).
   - Spec vigente del slice correspondiente en `specs/<slug>/` (si existe).
3. Identificar **alcance** y **archivos autorizados** ANTES de implementar nada. No existe permiso implícito "puse la mano donde hizo falta".
4. Registrar limitaciones, decisiones pendientes y deuda encontrada (en el informe de la tarea y, al cerrar, en `Memoria.md`).

## 2. Prohibiciones (OBLIGATORIO, salvo autorización humana expresa por escrito en el spec/plan aprobado)

- No expandir el alcance de una tarea por iniciativa propia. Detente y solicita autorización si el cambio necesario excede el alcance aprobado.
- No refactorizar archivos ajenos a la funcionalidad solicitada ("aprovechar para limpiar" está prohibido: regístralo como deuda).
- No modificar UI, navegación, permisos, manifest, ni configuración de Google Maps/Gradle cuando la tarea aprobada no los incluya.
- No inventar resultados: si no ejecutaste una compilación/prueba/revisión, declárala como **no ejecutada** con el comando pendiente. Nunca digas "verde", "pasó" o "aprobado" sin evidencia real.
- No introducir dependencias ni actualizar versiones de Gradle/Kotlin/AGP/Compose sin justificación + aprobación (verificar primero la compatibilidad documentada: Kotlin 2.2.10 / AGP 9.2.1 / Compose BOM 2026.02.01 / maps-compose 6.12.2).
- No versionar secretos: ningún contenido de `local.properties`, claves de Maps, tokens, credenciales en archivos versionables. Si necesitas un valor, pide al usuario que lo configure localmente (propiedad); jamás lo escribas en un archivo que será versionado.
- No ejecutar comandos destructivos (`git reset --hard`, `git clean`, borrados recursivos, limpias de build con datos generados), ni commits/push sin autorización humana.
- No crear archivos que aparenten ser verificaciones sin comprobar nada (gates falsos, "checkers" de decoración).
- No escribir claves ni valores en `gradle.properties`.

## 3. Flujo de trabajo SDD (OBLIGATORIO en cambios funcionales relevantes)

```
1  Recuperar contexto y memoria (leer constitution + Memoria + spec vigente)
2  Identificar problema y alcance
3  Redactar especificación            (spec-drafter)
4  Revisar y APROBAR spec             (revisor humano firma 4 campos)
5  Elaborar plan técnico              (plan-architect)
6  Revisar y APROBAR plan             (revisor humano)
7  Descomponer tareas                 (task-decomposer)
8  APROBAR las tareas y sus límites   (revisor humano)
9  Implementar UNA tarea por vez      (slice-scaffolder / test-writer)
10 Ejecutar la verificación disponible (comando real + evidencia)
11 Revisar arquitectura y seguridad cuando corresponda (layer-guardian / security-reviewer, solo lectura)
12 Comprobar preservación visual si hay UI afectada (visual-preservation + humano)
13 Revisar el diff completo del slice
14 Actualizar documentación y memoria  (docs-keeper)
15 Cerrar con evidencias + limitaciones + deuda pendiente
```

- Pasos 4, 6, 8, 12 (aceptación) y la autorización de merges/commits requieren **aprobación humana explícita** que identifique la versión o contenido aprobado.
- Si cambia sustancialmente el alcance, el plan o los archivos afectados, **se repite** la revisión correspondiente (no se parcha sobre la marcha).
- **Cuando falla algo**: si una compilación falla, una prueba no puede ejecutarse o aparece un error ajeno al cambio → se detiene la tarea, se documenta el hallazgo por separado (preexistente vs introducido), se propone solución dentro del alcance y se pide autorización si excede el plan. Un error preexistente NO se corrige dentro del slice actual sin autorización.

## 4. Reglas de trabajo técnico (OBLIGATORIO)

- Verificación por defecto de cualquier cambio: `.\gradlew.bat :app:assembleDebug`. Si el entorno no permite ejecutar (sin JDK/dispositivo), se declara y el slice queda con la verificación PENDIENTE, no aprobada.
- Pruebas y requisitos de análisis se ajustan al cambio (constitución §A.10): lógica nueva → test/verificación real; texto/estilo → verificación del diff.
- Datos y estado: no se introduce persistencia, DI ni capas ceremoniales sin spec aprobado (constitución §A.6).
- El borrador de pedido y estados vivos del flujo (`CreateOrderUiState`) son zona delicada: cualquier cambio ahí exige consultas de baseline + decisión de comportamiento aprobada.

## 5. Escalado de agentes (RECOMENDACIÓN)

- No es necesario invocar todos los agentes para cada tarea. Valor real por caso:
  - **Una pantalla nueva o cambio funcional del wizard** → flujo completo (spec→plan→tasks→implement). Roles: spec-drafter, plan-architect, task-decomposer, slice-scaffolder, test-writer si hay lógica a testear.
  - **Documentar/inventariar/emisión de informes** → docs-keeper (+ revisores de búsqueda si aporta).
  - **Refactor de estado** (p. ej. ViewModels del wizard) → flujo completo + visual-preservation y layer-guardian.
  - **Cambios de permisos/mapa/cámara** → spec + security-reviewer + skill `enver-maps-camera-permissions`.
  - **Cambio de dependencias/build** → spec + security-reviewer + revisor humano.
  - **Limpiezas documentales y de memoria** → docs-keeper solo; sin slice de producción.
- Prohibición de sobre-uso: crear un spec para un cambio de 2 líneas de docs es burocracia, no rigor. Usa el juicio por sección §3 ("cambios funcionales relevantes").

## 6. Registro y cierre (OBLIGATORIO)

Al terminar una tarea:
1. Entregar: qué archivos se crearon/modificaron, evidencia de verificación ejecutada (o pendiente + comando), limitaciones, deuda encontrada.
2. Actualizar `Memoria.md`: entrada de estado + decisiones (con porqué) + errores a evitar (si aplica).
3. Dejar en claro qué está **aprobado** y qué queda **pendiente**. Un pendiente nunca es un aprobado.

## 7. Límites de los agentes declarados vs técnicas

- Las restricciones de esta guía y de los prompts de `.opencode/agents/` son **de comportamiento**. La única "barrera técnica" disponible hoy es: revisiones de git diff, permisos opencode para agentes revisores (`edit`/`bash`/`task: deny`; solo lectura), y la aprobación humana. No prometas aislamiento donde no lo hay — la constitución (§D) lo establece.

## 8. Verificación obligatoria al cerrar cada bloque funcional (Checkpoint 4.7)

Al finalizar cada bloque funcional, el agente DEBE ejecutar esta secuencia y registrar el resultado real (nada se declara "pasado" sin evidencia):

1. **Revisar el diff** (`git diff`/`status`) y comprobar que no hay cambios fuera del alcance del bloque.
2. **Compilar y probar**: `.\gradlew.bat :app:assembleDebug` + las pruebas pertinentes al cambio (unitarias si hay lógica nueva; instrumentadas solo si hay dispositivo).
3. **Análisis estático y seguridad disponibles**: lint cuando aplique, escaneo de secretos en el diff y archivos nuevos; sin logs con credenciales/PII.
4. **Corregir errores introducidos por el cambio** (los preexistentes se reportan por separado, no se corrigen sin autorización).
5. **Registrar** qué pasó, falló o NO pudo ejecutarse y por qué (comando + salida o causa).
6. **Actualizar** spec/tasks de la funcionalidad y `Memoria.md` cuando corresponda.
7. **Confirmar que está listo para revisión** antes de publicar (sin push directo a `main`; PR según ruleset).

Proporcionalidad: los comandos costosos (`lintDebug`, suites completas) se ejecutan al cerrar el bloque y antes de integrar; los cambios de una línea requieren como mínimo build + diff revisado. El pipeline de CI (`.github/workflows/ci.yml`) ejecuta los mismos comandos en PR y push a `main`; los resultados de GitHub se citan solo cuando existan.
