# Especificación — FT-01 Recuperar compilación (identidad de paquetes Kotlin)

## 0. Datos de control

| Campo | Valor |
|---|---|
| Identificador | FT-01 |
| Título | Recuperar la compilación alineando los paquetes Kotlin al namespace |
| Estado | Borrador (v2 — actualización del estado real post-Checkpoint 3.7; dirección técnica D-B autorizada por el propietario el 2026-11-06; Aprobación formal PENDIENTE) |
| Autor de la redacción | Agente OpenCode (Checkpoints 3.6–3.8) |
| Versión | 2 |
| Convenio de nombre | Directorio `specs/FT-01-recuperar-compilacion/` siguiendo el patrón del repo de referencia (`specs/FT-XX-slug/`) y `AGENTS.md` (`specs/<slug>/`). Identifier `FT-01` ya introducido en `Memoria.md` pendiente #11 |

## 1. Problema y contexto

- Problema real observable: `.\gradlew.bat :app:assembleDebug` FALLA (`:app:compileDebugKotlin`, exit 1; re-producción 2026-11-06 en Checkpoint 3.7, log `'%TEMP%\opencode\enver_assembleDebug_37.txt'`, ~250 ocurrencias "Unresolved reference"). Evidencia registrada en `Memoria.md` §1 (eventos 3.5 y 3.7).
- Estado post-3.7 (parte del propietario, 2026-11-06 13:24–13:37): las 21 clases Kotlin + 2 plantillas de test YA declaran `package com.uttt.enverdelivery.app.*`; `ImageSource.kt:3` ya importa `com.uttt.enverdelivery.app.R`; los 3 archivos de navegación están completos. Quedó pendiente (causa del fallo actual):
  - 58 imports internos aún en `com.example.app.*` (grep literal del 2026-11-06) en 13 archivos main (Home 9, MainScaffold 6, Login 6, WelcomeSuccess 6, Register 6, Flow 5, Step2 5, Step3 5, Welcome 3, Header 2, MainActivity 2, BottomNav 2, EnverImage 1): tema `EnverGreen/EnverTextMuted/EnverDarkGreen/EnverCardBg/EnverInputBg/EnverBadgeBg/EnverBackground/…` + objetos compartidos (`ImageSource/EnverImage/HomeScreen/CreateOrderUiState/CreateOrderFlowScreen/EnverGlobalHeader`).
  - `EnverGlobalHeader.kt:1` declara package con segmento DUPLICADO: `com.uttt.enverdelivery.app.app.ui.components.common` (correcto: `com.uttt.enverdelivery.app.ui.components.common`).
  - `HomeScreen.kt` usa `R.drawable.*` (líneas 62, 73) y NO tiene `import com.uttt.enverdelivery.app.R`.
  - `ExampleInstrumentedTest.kt:22` mantiene literal `"com.example.enver"` (compila; fallaría en runtime).
  - Autoridad del FileProvider DESALINEADA hoy: manifest `AndroidManifest.xml:39` = `com.example.app.fileprovider` vs código `CreateOrderFlowScreen.kt:478,618` = `"com.example.enver.fileprovider"` → la captura de cámara está rota EN RUNTIME con el código actual (ver §8). No bloquea la compilación.
- Por qué ahora: el fallo bloquea TODA verificación de compilación del flujo SDD; el propietario autorizó la dirección técnica D-B el 2026-11-06.

## 2. Objetivo y resultados esperados

- Objetivo medible: `.\gradlew.bat :app:assembleDebug` termina `BUILD SUCCESSFUL` (exit 0) sin modificar applicationId, recursos, navegación, pantalla alguna ni configuración de build.
- Resultados esperados: convención de identidad coherente y única. Al cierre: 0 referencias Kotlin rotas; documento `Memoria.md` §1 actualizado con la evidencia del build resultante.
- Cómo se sabe que terminó: build verde con evidencia registrada + greps de cierre del §13 (`com\.example\.` ≡ 0; autoridad FileProvider uniforme si R6 incluida).

## 3. Usuarios y escenarios

- Usuario principal: el propietario/desarrollo del proyecto.
- Escenario 1: tras aprobar spec/plan/tasks, el agente ejecuta el cambio, corre `assembleDebug`, registra verde o fallo real en `Memoria.md`, y prepara las evidencias para el primer commit.
- Escenario 2: el propietario hace el primer commit del inventario aprobado (Checkpoint 3.5 §4) con el estado ya compilable.

## 4. Requisitos funcionales (del slice de mantenimiento; NO son requisitos de producto)

| ID | Requisito | Prioridad | Verificable por | Estado al 2026-11-06 |
|---|---|---|---|---|
| R1 | Declaraciones `package` de las clases Kotlin pasan a `com.uttt.enverdelivery.app.*` (subpaquetes idénticos a los directorios actuales) | Alta | grep (0 `package com.example.app`; 0 segmento `.app.app.`) | **CUMPLIDO por el propietario 2026-11-06** (21 clases + 2 plantillas), salvo el segmento duplicado de `EnverGlobalHeader.kt:1` |
| R2 | Imports internos del proyecto actualizados a las nuevas rutas; ningún import de `com.example.app.*` restante en `app/**` main | Alta | grep + build | PENDIENTE: 58 imports en 13 archivos |
| R3 | Resolución de `R`: cada clase que usa `R.*` tiene `import com.uttt.enverdelivery.app.R` | Alta | build + grep | `ImageSource.kt` CUMPLIDO (propietario); `HomeScreen.kt:62,73` PENDIENTE |
| R4 | Plantillas de test en `com.uttt.enverdelivery.app`; literal `com.example.enver` del instrumentado → `com.uttt.enverdelivery.app` (applicationId real) | Media | grep | Paquete CUMPLIDO (propietario); literal `ExampleInstrumentedTest.kt:22` PENDIENTE |
| R5 | Preservación: ninguna otra línea cambia (ni textos, ni hex, ni dp/sp, ni lógica, ni navegación, ni UI) | Alta | revisión de diff | — |
| R6 | Alinear la autoridad del FileProvider a `com.uttt.enverdelivery.app.fileprovider` en las 3 únicas líneas que la contienen: `AndroidManifest.xml:39`, `CreateOrderFlowScreen.kt:478`, `CreateOrderFlowScreen.kt:618` (el mapeo `file_paths.xml` de `cache-path order_photos` ya coincide con `context.cacheDir` — verificado; no se toca). CONDICIONAL: solo si el propietario la incluye al aprobar el alcance | Baja | grep `\.fileprovider` (3 coincidencias idénticas) + inspección de diff | Recomendación SÍ fundada (ver §8): hoy la cámara está ROTA en runtime por la triple desalineación |

Regla de negocio: no aplica (no hay comportamiento de usuario afectado).

## 5. Requisitos no funcionales

- Rendimiento: no medible relevante (solo compilación).
- Seguridad/privacidad: no se tocan secretos ni `gradle.properties`/`local.properties`; no hay datos nuevos.
- Compatibilidad: Kotlin 2.2.10 / AGP 9.2.1 / Gradle 9.4.1 / JDK 21 (constitución). Kotlin NO exige coincidencia carpeta↔package; Android exige `R` bajo `namespace` (por eso falla hoy). D-B (dirección autorizada por el propietario el 2026-11-06) alinea los paquetes con la estructura de directorios y el namespace ya existentes, sin tocar configuración de build.
- Accesibilidad/textos: sin textos nuevos; `res/values/strings.xml` intacto.

## 6. Criterios de aceptación observables (cada uno puede FALLAR)

1. Dado el comando `.\gradlew.bat :app:assembleDebug`, cuando se ejecuta desde la raíz tras el slice, entonces termina `BUILD SUCCESSFUL` (exit 0) y genera `app/build/outputs/apk/debug/`.
2. Dado el grep `com\.example\.(app|enver)` sobre `app/**` (excluyendo `build/`, `.gradle/`), cuando se ejecuta al cierre, entonces devuelve 0 coincidencias (los dos patrones: `app` y `enver`, incluido el literal del test instrumentado y las autoridades si R6 incluida).
3. Dado el diff completo del slice, cuando lo revisa el propietario, entonces todas las líneas modificadas son de `package`/`import`/literal-de-test/autoridad-FileProvider (solo las 3 líneas de R6 si incluida); ninguna línea de UI/lógica queda modificada.
4. Dado el build resultante, cuando se inspeccionan `app/build.gradle.kts`, `gradle/libs.versions.toml`, `res/**` y `gradle.properties`, entonces quedan SIN modificaciones (namespace/applicationId/permisos/recursos intactos; el manifest queda estable EXCEPTO L39 si R6 incluida).
5. Dado el grep `\.fileprovider` sobre `app/**` (excluyendo `build/`), cuando se ejecuta al cierre del slice con R6 incluida, entonces devuelve exactamente 3 coincidencias, todas con el valor `com.uttt.enverdelivery.app.fileprovider` (manifest L39 + las 2 en `CreateOrderFlowScreen.kt`).
6. Dado `Memoria.md`, cuando se cierra el slice, entonces contiene la evidencia del comando y su resultado real (verde o fallo, sin afirmaciones no ejecutadas) y la declaración de las verificaciones NO ejecutadas (p. ej. runtime de cámara sin dispositivo).

## 7. Comportamiento actual que debe conservarse (preservación)

- Baseline (`docs/ENVER_UI_BASELINE.md`): TODAS las pantallas quedan congeladas; el slice no toca líneas visuales. Énfasis en §1.4 Home (`HomeScreen.kt` usa los drawables `articulo_*` que deben seguir resolviendo iguales).
- `MainScaffold.kt:43,46` y `CreateOrderFlowScreen.kt:107`: solo cambia la línea `package`/`import`; lógica y estado intactos.
- `AppNavHost.kt:34–37`: rutas/navegación intactas (solo package/imports).
- Capturas: NO disponibles (sin dispositivo); la preservación se comprobará por revisión de Diff (constitución §E).

## 8. Casos límite y errores

| Caso | Comportamiento esperado | Estado actual hoy |
|---|---|---|
| Carpeta ≠ package en Kotlin | Kotlin compila sin exigir coincidencia | Válido (no es la causa del fallo) |
| Referencia `R` desde subpaquete | Requiere `import` del R del namespace | FALLA hoy en `HomeScreen` (`ImageSource` ya corregido) |
| Package con segmento duplicado (`...app.app....`) | Compila si NINGÚN otro archivo importa ese FQN; rompe cualquier import por la ruta esperada | `EnverGlobalHeader.kt:1` declara `com.uttt.enverdelivery.app.app.ui.components.common`; `MainScaffold.kt:19` aún lo importa desde `com.example.app.*` |
| Instalado en dispositivo: packageName | = applicationId `com.uttt.enverdelivery.app` | test instrumentado aserciona literal viejo (solo corre con dispositivo) |
| FileProvider authority ≠ autoridad usada por el código | `FileProvider.getUriForFile` resuelve la autoridad registrada en el manifest de ESTA app | ROTO en runtime: manifest L39 = `com.example.app.fileprovider` vs código `CreateOrderFlowScreen.kt:478,618` = `com.example.enver.fileprovider` → IllegalArgumentException al tomar foto de pedido; el build NO se ve afectado |
| Rutas del FileProvider (`file_paths.xml` vs código) | `<cache-path name="order_photos" path="order_photos/"/>` debe contener el directorio usado | OK verificado: el código crea `File(context.cacheDir, "order_photos")` (`CreateOrderFlowScreen.kt:470`) y el archivo de la foto dentro de ese directorio |
| Autoridad fija como literal en 2 puntos del código | Cambiarla exige tocar ambos + manifest sincronizados | Deuda encontrada; queda registrada como candidata a constante compartida (pendiente futura, FUERA de este slice) |

## 9. Seguridad y privacidad

- Datos sensibles: ninguno tocar; sin secretos; sin logs.
- Permisos: sin cambios (CAMERA/location quedan intactos).
- Almacenamiento/log: sin cambios.

## 10. Exclusiones explícitas (fuera de alcance)

- NO cambia: `app/build.gradle.kts`, `gradle/libs.versions.toml`, `gradle.properties`, `local.properties`, wrapper; NO renombra directorios ni mueve imágenes; NO toca `res/**` (incluido `res/xml/file_paths.xml`, ya verificado correcto), ni `keepRules`; NO resuelve terminología `Rescatista`/`repartidor`; NO toca lógica de wizard/rotación; NO crea tests nuevos; NO toca navegación semántica.
- Excepciones explícitas al "NO tocar manifest/código" SOLO si R6 queda incluida en la aprobación: `AndroidManifest.xml:39` (1 línea) y los literales de autoridad `CreateOrderFlowScreen.kt:478` y `:618` (solo el string, sin lógica).
- Alternativa descartada: cambiar namespace a `com.example.app` (rompería coherencia con dirs/renombrado previo y toca build config = prohibido). Antiguo "opción D-A" del v1: descartado — el propietario ya migró 21 clases + navegación + plantillas; revertir exigiría editar más archivos que avanzar.

## 11. Riesgos y dependencias

| Riesgo / dependencia | Impacto | Mitigación / condición |
|---|---|---|
| Referencias R no detectadas con grep | build roto post-slice | grep `com\.example\.` total + build real como DoD |
| Config cache de Gradle stale | falsa confianza | correr `assembleDebug` limpio de caché sospechosa (`--rerun-tasks` si el optimista up-to-date no es plausible) |
| R6 sin resolver | runtime: captura de foto de pedido sigue ROTA (hoy, authority manifiestamente desalineada con el código) | decisión del propietario en la aprobación formal; recomendación fundada: INCLUIR (3 líneas) |
| Runtime de cámara no verificable | sin dispositivo no se puede ejecutar la captura | limitación declarada: verificación de R6 por grep + diff + lectura de código; prueba en device queda PENDIENTE (no aprobada) |
| Instrumented test requiere dispositivo | verificación de ejecución pendiente | declarar limitación; opción de compilar (`compileDebugAndroidTestSources`) |
| Dependencia: spec, plan y tareas aprobados antes de implementar | bloqueo de arranque | gates del flujo SDD (no auto-ejecutar) |

## 12. Decisiones pendientes (propietario)

| # | Decisión | Quién la aprueba | Condición |
|---|---|---|---|
| D-B | Dirección técnica: alinear todo a `com.uttt.enverdelivery.app` — AUTORIZADA por el propietario el 2026-11-06 (Checkpoint 3.8, instrucción escrita). La aprobación formal de este spec/plan/tasks sigue PENDIENTE | Humano | firmar §14 para habilitar el slice |
| R6 | Alinear la autoridad del FileProvider (3 líneas: manifest L39 + `CreateOrderFlowScreen.kt:478,618`) | Humano (en la misma aprobación) | recomendación fundada del agente: INCLUIR; se resuelve marcando incluida/excluida al firmar |

IC (investigación del Checkpoint 3.8): el mapeo `<cache-path name="order_photos" path="order_photos/"/>` de `res/xml/file_paths.xml` coincide con el directorio que crea el código (`File(context.cacheDir, "order_photos")`, `CreateOrderFlowScreen.kt:470`) — no hay más referencias a la autoridad en el código (grep total: solo las 3 líneas citadas). Cambiar la autoridad NO requiere ningún cambio adicional para preservar cámara/URI.

## 13. Plan de pruebas (alto nivel)

- Compilación: `.\gradlew.bat :app:assembleDebug` (autorizada por el checkpoint; verificación del DoD).
- Greps de cierre sobre `app/**` excluyendo `build/`/`.gradle/`:
  - `com\.example\.(app|enver)` → 0 coincidencias (critério 2).
  - `\.fileprovider` → exactamente 3 coincidencias idénticas con valor `com.uttt.enverdelivery.app.fileprovider` (critério 5, si R6 incluida).
  - `\.app\.app\.` → 0 coincidencias (segmento duplicado del header eliminado).
- Instrumentadas: no disponibles (sin dispositivos/emulador); opcional `compileDebugAndroidTestSources` para compilar plantillas.
- NO verificable en este slice: ejecución runtime de la cámara (captura de foto) y del test instrumentado — se declaran como PENDIENTES con dispositivo, no como aprobadas.

## 14. Aprobación humana (OBLIGATORIO; no editar sin revisor)

| Campo | Valor |
|---|---|
| Estado de Aprobación | **APRUEBA — FT-01 v2** (D-B APROBADA; R6 **INCLUIDA**; preservación según instrucción; build real obligatorio; sin commits/push/remoto; respaldo previo obligatorio) |
| Revisor humano | Propietario (instrucción escrita «Aprobación formal — FT-01 Recuperar compilación», mensaje del 2026-11-06) |
| Fecha de aceptación expresa | 2026-11-06 |
| CHECKLIST_PR Fase A resuelta (1.1, 4.3-A, 5.2-A) | Pendiente de resolver por el humano (no bloquea el slice; la aprobación explícitamente NO ratifica la Constitution completa) |
| Sin conflicto con la constitución | Verificado por el agente en Checkpoint 3.8 (§ cumplimiento) y no contradicho en la aprobación; ratificación completa de la Constitution sigue PENDIENTE (decisión #6) |
