# Tareas — FT-01 Recuperar compilación (identidad de paquetes Kotlin)

> Se elabora DESPUÉS de un plan aprobado. Cada tarea es pequeña, ordenada y verificable. NINGUNA tarea autoriza cambiar archivos protegidos (§4 del plan). No ejecutar hasta DoD §11 del plan completo.

## 0. Datos de control

| Campo | Valor |
|---|---|
| Plan | specs/FT-01-recuperar-compilacion/plan.md — **v2 APROBADO** (2026-11-06, propietario) |
| Estado | **v2 APROBADO** (2026-11-06, propietario; D-B APROBADA y R6 INCLUIDA → T1.5 APLICA) |
| Nº de tareas | 7 (T1.5 aplica: R6 quedó INCLUIDA en la aprobación formal) |

## 1. Tareas

### T1.1 — Preparar respaldo y congelar el estado de partida

| Campo | Contenido |
|---|---|
| Objetivo | Copiar los archivos a tocar a respaldo externo y registrar el estado Git de partida |
| Requisitos cubre | §10 del plan |
| Dependencias | — |
| Archivos permitidos | escritura SOLO en `%TEMP%\opencode\ft01-backup\` (fuera del repo); lectura del repo |
| Pasos | 1. Crear carpeta de respaldo. 2. Copiar los 14 archivos de la tabla §3-2 del plan (los 13 `.kt` main + `ExampleInstrumentedTest.kt`) y, si R6 quedó INCLUIDA, también `AndroidManifest.xml`. 3. Registrar `git status`/`git log` de partida (0 commits) en la evidencia |
| Criterios | respaldo con listado exacto; conteo de copias == 14 (o 15 con R6) |
| Verificación | comparación de conteo copiado contra la tabla §3-2 del plan |
| Evidencia | listado de copias + tamaños |
| Cierre | tarea marcada [x] solo con ese listado en el registro §2 |

### T1.2 — Completar los imports internos y el package del header (R1/R2)

| Campo | Contenido |
|---|---|
| Objetivo | Reemplazar los 58 `import com.example.app.*` restantes por `com.uttt.enverdelivery.app.*` (mismos símbolos/subpaquetes) y corregir el package duplicado de `EnverGlobalHeader.kt:1` (`com.uttt.enverdelivery.app.app.ui.components.common` → `com.uttt.enverdelivery.app.ui.components.common`) |
| Requisitos | R1 (residual: header), R2, R5 (solo package/import) |
| Dependencias | T1.1 |
| Archivos permitidos | exactamente los 13 `.kt` de la tabla §3-2 del plan (rutas completas ahí): MainActivity, EnverImage, EnverBottomNavigationBar, MainScaffold, EnverGlobalHeader, LoginScreen, RegisterScreen, WelcomeScreen, WelcomeSuccessScreen, CreateOrderFlowScreen, CreateOrderStep2Screen, CreateOrderStep3Screen, HomeScreen |
| Pasos | 1. Reemplazar `com.example.app` en imports internos de cada archivo (NUNCA en imports `androidx`/`kotlinx`/`java.*`). 2. Corregir la línea package del header. 3. NO alterar ninguna otra línea |
| Criterios | grep `import com\.example\.app` en `app/src/main/**` = 0; grep `package com\.uttt\.enverdelivery\.app\.app\.` = 0; diff del slice = solo esas líneas |
| Verificación | grep por archivo + revisión de diff del paso |
| Evidencia | salidas de grep + resumen de diff |
| Cierre | [x] solo con grep y revisión de diff de los 13 archivos |

### T1.3 — Añadir el import de `R` en `HomeScreen` (R3, residual)

| Campo | Contenido |
|---|---|
| Objetivo | `HomeScreen.kt` añade `import com.uttt.enverdelivery.app.R` (el caso `ImageSource.kt` ya quedó resuelto por el propietario) |
| Requisitos | R3, R5 |
| Dependencias | T1.1 |
| Archivos permitidos | solo `app/src/main/java/com/uttt.enverdelivery/app/ui/screens/home/HomeScreen.kt` |
| Pasos | 1. Añadir 1 línea de import colocada con el resto de imports `com.*`. 2. Nada más |
| Criterios | grep `^import com\.uttt\.enverdelivery\.app\.R$` = 1 en HomeScreen; `R.drawable` de L62/73 sin cambios |
| Verificación | grep + build del T1.6 |
| Evidencia | salida de grep |
| Cierre | [x] con grep adjuntado (la confirmación total llega con el build del T1.6) |

### T1.4 — Literal del test instrumentado (R4, residual)

| Campo | Contenido |
|---|---|
| Objetivo | `ExampleInstrumentedTest.kt:22`: literal `"com.example.enver"` → `"com.uttt.enverdelivery.app"` (el package de ambas plantillas ya está migrado por el propietario — NO repetirlo) |
| Requisitos | R4 |
| Dependencias | T1.1 |
| Archivos permitidos | solo `app/src/androidTest/java/com/uttt.enverdelivery/app/ExampleInstrumentedTest.kt` |
| Pasos | 1. Cambiar el literal del `assertEquals` únicamente. 2. Nada más |
| Criterios | grep `com\.example\.` en `app/src/test/**` y `app/src/androidTest/**` = 0 |
| Verificación | grep; opcional `.\gradlew.bat :app:compileDebugAndroidTestSources --no-daemon` |
| Evidencia | salida de grep (y del compile opcional si corre) |
| Cierre | [x] solo con grep/compile adjuntado |

### T1.5 — CONDICIONAL: alinear autoridad del FileProvider (R6) — solo si R6 quedó INCLUIDA

| Campo | Contenido |
|---|---|
| Objetivo | Uniformar la autoridad a `com.uttt.enverdelivery.app.fileprovider` en las 3 únicas líneas que la contienen |
| Requisitos | R6 (inclusión decidida por el propietario EN la aprobación formal) |
| Dependencias | T1.1 |
| Archivos permitidos | solo 3 líneas: `AndroidManifest.xml:39`; 2 strings en `CreateOrderFlowScreen.kt:478,618` (solo el string, sin lógica) |
| Pasos | 1. Manifest L39: `android:authorities="com.example.app.fileprovider"` → `"com.uttt.enverdelivery.app.fileprovider"`. 2. Los 2 literales de `getUriForFile`: `"com.example.enver.fileprovider"` → la misma autoridad nueva. 3. Nada más (NO tocar `file_paths.xml` ni la creación de archivos en `cacheDir`) |
| Criterios | grep `\.fileprovider` sobre `app/**` (excluyendo `build/`) = exactamente 3 coincidencias idénticas con el valor nuevo |
| Verificación | grep de autoridad + diff de exactamente 3 líneas |
| Evidencia | salida del grep + las 3 líneas del diff |
| Cierre | [x] solo con grep+diff adjuntados. Limitación SIEMPRE declarada: ejecución runtime de la cámara PENDIENTE (requiere dispositivo) |
| Nota | si el propietario marcó R6 EXCLUIDA, esta tarea salta y se registra como decisión tomada (no como cancelada) |

### T1.6 — Verificación de compilación (DoD del spec)

| Campo | Contenido |
|---|---|
| Objetivo | Ejecutar y registrar el comando autorizado |
| Requisitos | Critério §6.1 del spec |
| Dependencias | T1.2, T1.3, T1.4 (y T1.5 si R6 incluida) |
| Archivos permitidos | ninguno (ejecución + registro) |
| Pasos | 1. `.\gradlew.bat :app:assembleDebug` desde la raíz. 2. Si exit 0, confirmar `app/build/outputs/apk/debug/` con APK reciente. 3. Registrar salida en `Memoria.md` §1 |
| Criterios | `BUILD SUCCESSFUL`, exit 0; APK generado; registro en Memoria con el resultado REAL |
| Verificación | comando; si falla: STOP, hallazgo nuevo, se documenta y pide autorización (AGENTS §3) — sin reparaciones fuera de alcance |
| Evidencia | log (resumen de cola) + exit code + ruta del APK |
| Cierre | [x] solo con build verde real registrado |

### T1.7 — Greps de cierre, docs y memoria (cierre del slice)

| Campo | Contenido |
|---|---|
| Objetivo | Evidencia de limpieza + docs actualizadas sin ratificar nada |
| Requisitos | §6.2, §6.5 (y §6.6 si R6) del spec |
| Dependencias | T1.6 |
| Archivos permitidos | `Memoria.md`; `specs/FT-01-recuperar-compilacion/tasks.md` (tabla §2); `CHECKLIST_PR.md` fase aplicable (marcar lo resuelto con evidencia) |
| Pasos | 1. Greps finales: `com\.example\.(app|enver)` en `app/**` (excluyendo `build/`/`.gradle/`) = 0; `\.app\.app\.` = 0; `\.fileprovider` = 3 idénticas si R6 incluida. 2. Registrar resultados y resumen de diff en `Memoria.md` (con limitaciones: sin capturas/device). 3. Marcar tareas [x] con enlaces a evidencias. 4. Reportar al propietario |
| Criterios | todos los [x] con evidencia; memoria contiene build real + limitaciones; NO se declara ratificación ni commit |
| Verificación | inspección de archivos + greps |
| Evidencia | listados en el informe del slice |
| Cierre | [x] al tener memoria/greps presentados |

📌 Si durante una tarea surge un error preexistente o ajeno al cambio: detenerse, registrar hallazgo separado y pedir autorización.

## 2. Registro de ejecución (marcar [x] SOLO con evidencia enlazada)

| Tarea | Estado | Verificación ejecutada | Evidencia | Fecha |
|---|---|---|---|---|
| T1.1 | [x] | Copia de 15 archivos con estructura relativa | `%TEMP%\opencode\ft01-backup\` (listado 15/15 no vacíos); git de partida: `main`, 0 commits, 90 untracked | 2026-11-06 |
| T1.2 | [x] | 58 imports reemplazados (conteo por script) + package del header corregido (1) | salidas del script + grep cierre `com\.example\.` = 0 y `\.app\.app\.` = 0 | 2026-11-06 |
| T1.3 | [x] | `import com.uttt.enverdelivery.app.R` añadida en HomeScreen (colocada tras `androidx.compose.ui.unit.sp`) | diff de HomeScreen en `%TEMP%\opencode\ft01-implementation.diff` | 2026-11-06 |
| T1.4 | [x] | literal L22 reemplazado | grep `com\.example\.` en test/androidTest = 0 | 2026-11-06 |
| T1.5 (R6) | [x] | 3 líneas de autoridad alineadas (manifest L39 + Flow:478,618) | grep `\.fileprovider` = incluida 1× `android:name` del provider + 3× authority idéntica al valor nuevo; diff de 3 líneas | 2026-11-06 |
| T1.6 | [x] | `.\gradlew.bat :app:assembleDebug` → **BUILD SUCCESSFUL in 51s**, exit 0, 36 tareas (11 ejecutadas/25 up-to-date); APK `app-debug.apk` 21298637 B @18:19:27 | log `%TEMP%\opencode\enver_assembleDebug_ft01.txt` | 2026-11-06 |
| T1.7 | [x] | greps de cierre + docs actualizadas (Memoria; esta tabla) | ver evento FT-01 v2 en `Memoria.md` §1; diff completo `%TEMP%\opencode\ft01-implementation.diff` | 2026-11-06 |

> Nota del build (preexistente, NO del slice): advertencias de deprecación de Material Icons/Divider y aviso compileSdk 37.1 vs AGP (suprimible) — se registran como deuda, sin tocar.

## 3. Cuota de revisión humana

Cualquier cambio de alcance, de archivos autorizados o de un requisito → repetir la revisión del plan; no se re-parametriza sobre la marcha.
