# Plan técnico — FT-01 Recuperar compilación (identidad de paquetes Kotlin)

## 0. Datos de control

| Campo | Valor |
|---|---|
| Especificación | specs/FT-01-recuperar-compilacion/spec.md — **v2 APRUEBA** (D-B APROBADA, R6 INCLUIDA, 2026-11-06, revisor: propietario) |
| Estado del plan | **v2 APROBADO** (2026-11-06, propietario; fecha+revisor registradas aquí y en spec §14) |
| Autor | Agente OpenCode (Checkpoints 3.6–3.8) |

> Regla constitucional: NO IMPLEMENTAR hasta que §11 esté completo (spec firmada + plan aprobado + tasks aprobadas). Este plan no toca archivos; solo propone.

## 1. Arquitectura actual relevante (verificada post-Checkpoint 3.7, 2026-11-06)

- Namespace/applicationId: `com.uttt.enverdelivery.app` (`app/build.gradle.kts:7,15`). `R` se genera en ese namespace.
- Árbol de fuentes: `app/src/main/java/com/uttt.enverdelivery/app/**` (MainActivity; navigation/{AppNavHost,NavRoutes,NavigationActions}; ui/components/common|media|navigation; ui/screens/auth|createorder|home|onboarding|welcome; ui/theme).
- Estado de los paquetes tras el cambio manual del propietario (13:24–13:37 del 2026-11-06, 23 archivos):
  - ALL: las 21 clases Kotlin + 2 plantillas de test YA declaran `package com.uttt.enverdelivery.app.*` — salvo `EnverGlobalHeader.kt:1` que declara `com.uttt.enverdelivery.app.app.ui.components.common` (segmento `.app.` duplicado).
  - Completos (package + imports internos YA migrados): `AppNavHost.kt`, `NavRoutes.kt`, `NavigationActions.kt`, `ImageSource.kt` (incl. `import com.uttt.enverdelivery.app.R`), `ui/theme/{Color,Theme,Type}.kt`, `ExampleUnitTest.kt`.
  - Parciales (package migrado, imports internos aún en `com.example.app.*`) — grep literal del 2026-11-06: `HomeScreen.kt` (9), `MainScaffold.kt` (6), `LoginScreen.kt` (6), `WelcomeSuccessScreen.kt` (6), `RegisterScreen.kt` (6), `CreateOrderFlowScreen.kt` (5), `CreateOrderStep2Screen.kt` (5), `CreateOrderStep3Screen.kt` (5), `WelcomeScreen.kt` (3), `EnverGlobalHeader.kt` (2), `MainActivity.kt` (2), `EnverBottomNavigationBar.kt` (2), `EnverImage.kt` (1) → **58 imports pendientes en 13 archivos**.
- Usos de `R`: `ImageSource.kt` RESUELTO (import correcto); `HomeScreen.kt:62,73` PENDIENTE (sin import; usa `R.drawable.articulo_*`).
- Build actual: `:app:compileDebugKotlin` FAIL (exit 1, 2026-11-06 23 s; ~250 "Unresolved reference", log `%TEMP%\opencode\enver_assembleDebug_37.txt`).
- FileProvider: manifest L39 = `com.example.app.fileprovider`; código `CreateOrderFlowScreen.kt:478,618` = `"com.example.enver.fileprovider"` → cámara ROTA en runtime hoy (no afecta build). Mapeo `res/xml/file_paths.xml` (`cache-path order_photos/`) COINCIDE con `File(context.cacheDir, "order_photos")` (`CreateOrderFlowScreen.kt:470`) — sin cambios necesarios ahí. Grep total de `FileProvider|fileprovider`: solo las 3 líneas citadas.
- Zona delicada alcanzada: solo líneas `package`/`import` (y los 2 strings de autoridad si R6 incluida); lógica intacta.

## 2. Diseño propuesto (dirección D-B autorizada por el propietario el 2026-11-06; formalización pendiente)

**D-B (alineación completa):** completar lo que el cambio manual del propietario dejó a medias:
1. Reemplazar los 58 `import com.example.app.*` restantes por `com.uttt.enverdelivery.app.*` (mismos subpaquetes/símbolos — ver tabla §3-2).
2. Corregir el package duplicado de `EnverGlobalHeader.kt:1` (`...app.app.ui...` → `...app.ui...`).
3. Añadir `import com.uttt.enverdelivery.app.R` en `HomeScreen.kt` (única clase pending de R).
4. Literal del test instrumentado `ExampleInstrumentedTest.kt:22`: `"com.example.enver"` → `"com.uttt.enverdelivery.app"` (el package de ambas plantillas ya está migrado).
5. CONDICIONAL R6: alinear la autoridad del FileProvider en las 3 únicas líneas que la contienen (manifest L39 + `CreateOrderFlowScreen.kt:478,618`) a `com.uttt.enverdelivery.app.fileprovider`; incluye un solo valor, sin lógica.
- Convención resultante: `package == namespace*` coherente con el árbol de directorios y el renombrado del 2026-11-05 (dir names `uttt.enverdelivery` corresponden al segmento con punto del package).
- Inserta sin reescribir: solo cambia dominios de nombres y 3 strings de autoridad; ninguna clase/miembro se renombra; ningún archivo se mueve.
- Antigua "Opción D-A" del v1: DESCARTADA — el propietario ya migró 23 archivos; revertir exigiría editar más clases de las que quedan por avanzar, y rompería los imports corregidos de `AppNavHost`/`ImageSource`.
- Abstinencia deliberada: gradle files, recursos (incluido `file_paths.xml`, verificado correcto), temas visuales, lógica/navegación, imágenes de `java/.../images/`, resto del manifest (permisos nunca cambian).

## 3. Archivos del slice (exactos y verificados, 2026-11-06)

### 3-1. Ya editados por el propietario el 2026-11-06 (fuera del alcance del agente; referencia)

`AppNavHost.kt`, `NavRoutes.kt`, `NavigationActions.kt`, `ImageSource.kt`, `ui/theme/{Color,Theme,Type}.kt`, `ExampleUnitTest.kt` — package (+ imports internos) correctos; el agente NO los toca.

### 3-2. Archivos pendientes de corrección en el slice (14 + 1 condicional)

| # | Archivo (rutas reales desde raíz) | Residual actual | Cambio del slice | Req(s) | Riesgo |
|---|---|---|---|---|---|
| 1 | `app/src/main/java/com/uttt.enverdelivery/app/MainActivity.kt` | 2 imports | reemplazo 2 imports | R2 | Bajo |
| 2 | `…/ui/components/common/EnverGlobalHeader.kt` | package L1 con `.app.app.` + 2 imports | fix package L1 + reemplazo 2 imports | R1/R2 | Bajo |
| 3 | `…/ui/components/media/EnverImage.kt` | 1 import | reemplazo 1 import | R2 | Bajo |
| 4 | `…/ui/components/navigation/EnverBottomNavigationBar.kt` | 2 imports | reemplazo 2 imports | R2 | Bajo |
| 5 | `…/ui/components/navigation/MainScaffold.kt` | 6 imports | reemplazo 6 imports | R2 | Bajo |
| 6 | `…/ui/screens/auth/LoginScreen.kt` | 6 imports | reemplazo 6 imports | R2 | Bajo |
| 7 | `…/ui/screens/auth/RegisterScreen.kt` | 6 imports | reemplazo 6 imports | R2 | Bajo |
| 8 | `…/ui/screens/welcome/WelcomeScreen.kt` | 3 imports | reemplazo 3 imports | R2 | Bajo |
| 9 | `…/ui/screens/onboarding/WelcomeSuccessScreen.kt` | 6 imports | reemplazo 6 imports | R2 | Bajo |
| 10 | `…/ui/screens/createorder/CreateOrderFlowScreen.kt` | 5 imports | reemplazo 5 imports (+ 2 literales de autoridad L478/618 SOLO si R6 incluida) | R2/R6 | Bajo (Medio si R6) |
| 11 | `…/ui/screens/createorder/CreateOrderStep2Screen.kt` | 5 imports | reemplazo 5 imports | R2 | Bajo |
| 12 | `…/ui/screens/createorder/CreateOrderStep3Screen.kt` | 5 imports | reemplazo 5 imports | R2 | Bajo |
| 13 | `…/ui/screens/home/HomeScreen.kt` | 9 imports + sin import de R | reemplazo 9 imports + añadir `import com.uttt.enverdelivery.app.R` | R2/R3 | Bajo |
| 14 | `app/src/androidTest/java/com/uttt.enverdelivery/app/ExampleInstrumentedTest.kt` | literal L22 `"com.example.enver"` | reemplazo del literal (el package ya está migrado) | R4 | Bajo |
| 15 | `app/src/main/AndroidManifest.xml` **(SOLO si R6 incluida)** | L39 autoridad vieja | reemplazar autoridad (1 línea: `com.example.app.fileprovider` → `com.uttt.enverdelivery.app.fileprovider`) | R6 | Medio |

Total residual: 58 imports en 13 archivos (grep literal del 2026-11-06) + package duplicado del header + 1 import de R + 1 literal de test + 3 líneas de autoridad si R6 incluida.

## 4. Archivos protegidos (prohibidos en este slice)

- `app/build.gradle.kts`, raíz `build.gradle.kts`, `settings.gradle.kts`, `gradle/**`, `gradlew*`, `gradle.properties`, `local.properties`.
- `AndroidManifest.xml` (RESTO aparte de L39 si R6 fue aprobado explícitamente; permisos e iconos nunca cambian).
- `app/src/main/res/**` (strings.xml, xml/, mipmaps, temas).
- `app/src/main/keepRules/rules.keep` e imágenes `java/…/app/images/*`.
- Cualquier componente de negocio/lógica (wizard states, callbacks, navegación semántica, colores).

## 5. Flujo de datos y estados afectados

- Ningún estado nuevo; se enumeran solo los enums/objetos ya existentes (no conocen dos nombres). El cambio es nominal/no semántico (nivel de relación Kotlin package↔namespace), no genera flujo.

## 6. Compatibilidad y migraciones

- Kotlin 2.2.10: tolera carpeta≠package, pero `R` desde un subpaquete exige import absoluto (regla aplicada en `ImageSource`; la que falta por añadir en `HomeScreen`).
- AGP 9.2.1: `namespace` fija el FQCN de `R`; applicationId separado del paquete, sin permisos para renombrar.
- FileProvider: la autoridad registrada (manifest) se resuelve DENTRO de la misma app; el código que llama (`getUriForFile`) debe usar EXACTAMENTE ese valor. Mapeo de rutas (`file_paths.xml`) ya coincide con `context.cacheDir` — no requiere cambios.
- Migraciones de datos: no existen.

## 7. Riesgos y mitigaciones

| Riesgo | Afecta a | Mitigación |
|---|---|---|
| Grep imperfecto de referencias cortas/`import as` | build posterior | DoD: build real + grep total antes de reportar verde |
| Cambios visuales no intencionados | baseline | revisión de diff por humano (solo package/import + literales) |
| Cache config/daemon | reporte inexacto | `--rerun-tasks` si el resultado up-to-date no es plausible |
| R6/manifest por conveniencia | alcance | gate: la inclusión de R6 se marca solo UNA vez, en la aprobación formal de spec/plan/tasks |

## 8. Estrategia de pruebas

- Unit: no se añade tests; la lógica no cambia y este slice no introduce ninguna.
- Instrumentadas: NO ejecutables (sin dispositivo; constitución §E declara limitación). Opcional `.\gradlew.bat :app:compileDebugAndroidTestSources --no-daemon` para validar coherencia de las plantillas editadas.
- Sin dispositivo (comandos disponibles): `.\gradlew.bat :app:assembleDebug` (DoD principal) y greps:
  - `import com\.example\.app` sobre `app/src/main/**` → 0 (critério 2).
  - `package com\.uttt\.enverdelivery\.app\.app\.` → 0 (fix del header).
  - `com\.example\.(app|enver)` sobre `app/**` (excluyendo `build/`, `.gradle/`) → 0 total.
  - `\.fileprovider` → 3 líneas idénticas si R6 incluida.
- NO verificable en este slice (limitación declarada): ejecución runtime de la cámara (R6) y del test instrumentado — requieren dispositivo; quedan PENDIENTES, no aprobadas.

## 9. Comprobaciones de preservación visual

- Elementos visibles afectados: NINGUNO (solo líneas nominales). Baseline de Home y splash congeladas.
- Evidencia exigida: diff completo del slice revisado por humano (sin capturas por falta de dispositivo; registro en `Memoria.md`).

## 10. Plan de reversión

- Sin commits aprobados aún: **copia previa** de los 14 (+1 condicional R6) archivos de la tabla §3-2 a un respaldo externo (e.g. `%TEMP%\opencode\ft01-backup\`, misma estructura relativa) ANTES de editar; restaurar con copy-back si el rollback se activa.
- Criterio de activación: build FAIL tras el slice con error NO trazable a otra causa nueva, o diff fuera de alcance detectado.

## 11. Condiciones para iniciar la implementación (DoD del plan)

- [ ] Spec con Aprobación (4 campos §14 completos) — incluyendo si R6 queda INCLUIDA o EXCLUIDA del alcance.
- [ ] Este plan APROBADO por humano (fecha+revisor en §0).
- [ ] tasks.md aprobadas (límites por tarea).
- [ ] Back-up preparado (§10) y evidencia de su existencia listada antes del primer edit.

> Nota sobre el estado del baseline: la consistencia del árbol (namespace/dirs/packages) ya la estableció el propio propietario el 2026-11-06; este slice completa el trabajo a medias, no redefine arquitectura.
