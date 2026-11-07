# ENVER — Checkpoint 2: Auditoría del estado actual (inventario real)

> Fecha: 2026-11-05
> Tipo: informe de solo lectura. **Ningún archivo de implementación fue modificado** durante esta auditoría.
> Método: inspección estática del código fuente, configuración y documentación existente. No se ejecutaron builds, tests ni la app en dispositivo.
> Informe de referencia usado en contraste: `ENVER_SDD_REPOSITORY_AUDIT.md` (auditoría del repositorio MovilSDD). **Nota de ubicación**: ese informe NO existe en `docs/` de este repositorio; la copia leíble se encuentra en `C:\AndroidProject\PruebaSSDMovilProfe\MovilSDD\docs\ENVER_SDD_REPOSITORY_AUDIT.md`. No se copió a este repo para respetar la restricción de crear solo los 3 documentos indicados.

---

## 1. Alcance y limitaciones de la auditoría

**Inspeccionado por completo (leído):**
- Raíz: `settings.gradle.kts`, `build.gradle.kts`, `.gitignore`, `gradle.properties` (solo verificación de existencia de claves, sin revelar valores), `gradlew/gradlew.bat`.
- `gradle/`: `libs.versions.toml`, `wrapper/gradle-wrapper.properties`, `gradle-daemon-jvm.properties` (toolchain 21).
- `app/build.gradle.kts`, `app/src/main/AndroidManifest.xml`, todos los recursos XML (`res/values`, `res/xml`).
- **Todo el código Kotlin** bajo `app/src/main/java/com/example/enver/` (18 archivos .kt, incluidos los 3 archivos del flujo de pedidos y todos los componentes).
- Tests: `app/src/test/java/com/example/enver/ExampleUnitTest.kt`, `app/src/androidTest/java/com/example/enver/ExampleInstrumentedTest.kt` (plantillas).
- Documentación existente: `docs/CHANGELOG.md`, `docs/FRONTEND_ARCHITECTURE.md`, `docs/NAVIGATION_FLOW.md`.
- Estructura de `res/drawable*`, `res/mipmap*`, `app/src/main/keepRules/rules.keep` (listado).

**No inspeccionado / limitaciones:**
1. **No se ejecutó compilación ni tests** (restricción del checkpoint: no lanzar builds ni modificar nada). El estado "compila" se cita del `docs/CHANGELOG.md` (entradas 2026-10-30/31: `:app:compileDebugKotlin` ✅ y `:app:assembleDebug` ✅), no re-verificado en esta sesión.
2. **No se ejecutó la app en emulador/dispositivo** (el propio CHANGELOG registra "No probado en runtime (sin emulador)" en sus 2 últimas etapas). Todo lo relativo a mapa, cámara, permisos y comportamiento visual se conoce solo por lectura de código.
3. **No es repositorio Git**: `.git` no existe en la raíz. No hay historial, diff ni tags. Estado verificado con test de existencia.
4. No existe `.opencode/`, `.specify/`, `.github/`, `AGENTS.md` ni `opencode.json` en el repo (verificado por listado de raíz). Tampoco `detekt.yml`, `.editorconfig` ni configuración de análisis estático.
5. `gradle.properties` y `local.properties`: se verificó la presencia/ausencia de la propiedad `MAPS_API_KEY` en `gradle.properties` (no está definida); **no se leyeron ni se exponen valores** de esas propiedades.
6. La carpeta `build/` y `.gradle/` raíces (salidas generadas) fueron excluidas deliberadamente.

---

## 2. Configuración y estructura (B.1)

### 2.1 Módulos Gradle

Módulo único `:app` (`settings.gradle.kts:26`, `rootProject.name = "Enver"`). Sin módulos de dominio/datos.

### 2.2 Versiones (evidencia: `gradle/libs.versions.toml`)

| Componente | Versión | Evidencia |
|---|---|---|
| Gradle wrapper | 9.4.1 | `gradle/wrapper/gradle-wrapper.properties:4` |
| AGP | 9.2.1 | `gradle/libs.versions.toml:2` |
| Kotlin | 2.2.10 (solo plugin `kotlin.plugin.compose`; sin kotlin-android explícito en catálogo) | `gradle/libs.versions.toml:9,40` |
| Compose BOM | 2026.02.01 | `gradle/libs.versions.toml:10` |
| Navigation Compose | 2.10.2 | `gradle/libs.versions.toml:11` |
| material-icons-extended | 1.7.8 (no gestionada por el BOM) | `gradle/libs.versions.toml:12` |
| maps-compose | 6.12.2 | `gradle/libs.versions.toml:13` |
| play-services-maps | 20.1.0 | `gradle/libs.versions.toml:14` |
| play-services-location | 21.3.0 | `gradle/libs.versions.toml:15` |
| JUnit 4 | 4.13.2 | `gradle/libs.versions.toml:4` |
| Toolchain JVM del daemon | 21 | `gradle/gradle-daemon-jvm.properties:12` |

**Nota de compatibilidad documentada:** el CHANGELOG (Etapa 8, 2026-10-31:6-7,36) registra que maps-compose 8.x/9.x exigen Kotlin 2.4 y rompían la compilación con Kotlin 2.2.10 → se fijó 6.12.2. Dato relevante al tocar dependencias en la migración.

### 2.3 Dependencias (`app/build.gradle.kts:47-67`)

- UI: Compose BOM + material3 + activity-compose + ui/graphics/tooling + material-icons-extended + navigation-compose + core-ktx + lifecycle-runtime-ktx.
- Mapa/ubicación: `maps-compose` + `play-services-maps` + `play-services-location`.
- Test: JUnit4, compose-ui-test-junit4, espresso, ext-junit (plantillas; **no hay ningún test de proyecto propio**).
- **Ausencias notables** (hechos, no defectos): sin coroutines explícitas (se usa `kotlinx.coroutines` en `CreateOrderStep2Screen.kt:82-84` vía transitivas de lifecycle/compose), sin lib de carga de imágenes remota, sin serialización, sin DI, sin Retrofit/OkHttp, sin ViewModel explicit (no se usa ningún ViewModel en el proyecto).

### 2.4 Configuración de build (`app/build.gradle.kts`)

- `namespace`/`applicationId`: `com.example.enver`; `compileSdk 37 (minor 1)`, `minSdk 24`, `targetSdk 37` (líneas 7-19).
- `manifestPlaceholders["MAPS_API_KEY"]` desde propiedad `MAPS_API_KEY` (vacío si no existe; líneas 21-26). **Estado real:** la propiedad NO está definida en `gradle.properties` → el mapa se renderiza vacío en ejecución (el placeholder `${MAPS_API_KEY}` en `AndroidManifest.xml:19-23` quedará vacío).
- `buildTypes.release`: **`optimization.enable = false`** (líneas 31-37) → sin minify/shrink; comportamiento heredado de la plantilla, distinto del endurecido de MovilSDD.
- `compileOptions`: Java 11 (líneas 38-41). `buildFeatures.compose = true` (42-44).
- Sin `buildConfigField`, sin `kotlinOptions/jvmToolchain` explícitos, sin coverage/kover, sin lint config propia (usa la por defecto).

### 2.5 Manifest (`app/src/main/AndroidManifest.xml`)

- Permisos declarados: `CAMERA`, `ACCESS_COARSE_LOCATION`, `ACCESS_FINE_LOCATION` (líneas 5-7). No hay más (sin INTERNET explícito — *por determinar*: Play Services/maps la añadirán por fusión de manifiestos; verificar en merge de release).
- `<application>`: `allowBackup="true"`, `dataExtractionRules`, `fullBackupContent`, `theme=Theme.Enver`, `supportsRtl` (líneas 9-17).
- `<meta-data com.google.android.geo.API_KEY value="${MAPS_API_KEY}">` (líneas 21-23).
- `MainActivity` exported con intent-filter LAUNCHER, `windowSoftInputMode=adjustResize` (líneas 24-35).
- `FileProvider` (authorities `com.example.enver.fileprovider`, `exported=false`, `@xml/file_paths`; líneas 37-45). `res/xml/file_paths.xml` expone solo `cache-path order_photos/`.

### 2.6 Entry points / Application

- `MainActivity.kt:10-19`: `enableEdgeToEdge()` + `setContent { EnverTheme { AppNavHost() } }`. **No existe clase `Application`** personalizada (no hay `MultiDex` ni configuración global).
- No hay inyección de dependencias ni contenedor global (no aplica Koin ni otro).

### 2.7 Tests y análisis estático

- `app/src/test/.../ExampleUnitTest.kt` y `app/src/androidTest/.../ExampleInstrumentedTest.kt`: plantillas del asistente (JUnit4; androidTest con compose-junit4 disponible). **0 tests de proyecto.**
- Sin Detekt, sin ktlint, sin kover, sin `.editorconfig`, sin CI (`scripts/` no existe), sin `dependabot`. Comandos reales disponibles en el repo según CHANGELOG: `:app:compileDebugKotlin` y `:app:assembleDebug` (los únicos gates que alguna vez se han ejecutado).

### 2.8 Estado de Git

- **No es repositorio Git** (`.git` inexistente). Implicación: no hay trazabilidad de cambios, diff base ni puntos de rollback transitivos; el rollback actual solo es posible por copias manuales o el IDE.

### 2.9 Configuración OpenCode / agentes / skills

- No existe ninguna (`/` sin `.opencode/`, sin `AGENTS.md`, sin `opencode.json`, sin `.specify/`). Metodología SDD: **inexistente** (partida de cero; se puede aprovechar el diseño de MovilSDD, ver doc 3).

### 2.10 Documentación existente

| Archivo | Contenido | Estado |
|---|---|---|
| `docs/CHANGELOG.md` | Historial por etapas (1–8) con archivos creados/modificados, dependencias, compilación y pendientes | Activo y detallado; es la principal fuente de estado histórico y de las pruebas de compilación |
| `docs/FRONTEND_ARCHITECTURE.md` | Estructura de paquetes, responsabilidades, criterios de organización, cómo agregar pantallas | **Ligeramente desincronizada**: omite `CreateOrderStep2Screen.kt` y `CreateOrderStep3Screen.kt` en su árbol y dice "pasos 2–4 pendientes" (FRONTEND_ARCHITECTURE.md:30,67) cuando el CHANGELOG Etapa 8 (2026-10-31) ya implementó el Paso 3 y reescribió el Paso 2 |
| `docs/NAVIGATION_FLOW.md` | Diagrama, rutas, comportamiento del Atrás, secciones de MainScaffold | Coherente con el código actual (verificado contra `AppNavHost.kt`, `NavigationActions.kt`, `MainScaffold.kt`) |

---

## 3. Estructura real de paquetes (B.1/B.2)

```text
com.example.enver/
├── MainActivity.kt                      # Solo tema + AppNavHost (20 líneas)
├── images/                              # 2 PNG dentro de java/ (NO SON RECURSOS VALIDOS, duplicados de drawable-nodpi; ver §9 problemas)
│   ├── Calculadora Casio.png
│   └── Cartulina blaca + plumon negro.png
├── navigation/
│   ├── NavRoutes.kt                     # 5 rutas const string
│   ├── AppNavHost.kt                    # NavHost único, startDestination=welcome
│   └── NavigationActions.kt             # Acciones con backstack (popUpTo)
└── ui/
    ├── theme/{Color,Theme,Type}.kt      # Paleta Enver* (9 colores) + MaterialTheme (dynamicColor=true) + Typography mínima
    ├── components/
    │   ├── common/EnverGlobalHeader.kt  # Header global solo en MainScaffold
    │   ├── navigation/{MainScaffold,EnverBottomNavigationBar}.kt
    │   └── media/{ImageSource,EnverImage}.kt
    └── screens/
        ├── welcome/WelcomeScreen.kt                 # splash 2s auto-navegación
        ├── auth/{LoginScreen,RegisterScreen}.kt     # formularios visuales
        ├── onboarding/WelcomeSuccessScreen.kt       # post-registro
        ├── home/HomeScreen.kt                       # sección Inicio + OrderItem + OrderCardVerticalFullWidth
        └── createorder/
            ├── CreateOrderFlowScreen.kt             # maestro de 4 pasos + CreateOrderUiState + Paso 1 + placeholder Paso 4
            ├── CreateOrderStep2Screen.kt            # Paso 2: mapa real, Geocoder, lugares habituales
            └── CreateOrderStep3Screen.kt            # Paso 3: selección de tiempo de entrega
```

Recursos relevantes: `res/drawable-nodpi/articulo_calculadora_casio.png`, `articulo_cartulina_plumon.png` (fotos reales de las tarjetas de Home); `res/values/strings.xml` contiene solo `app_name` (todos los textos están hardcodeados en Composables, en español); `res/values/colors.xml` y `themes.xml` son plantilla (colores purple/teal sin uso app visible).

---

## 4. Navegación actual (B.2)

**Archivo central:** `navigation/AppNavHost.kt`; rutas en `navigation/NavRoutes.kt` (strings planos, **no type-safe**); acciones en `navigation/NavigationActions.kt`.

Rutas: `welcome`, `login`, `register`, `register_success`, `home` (participación directa del NavHost). Los 4 pasos del pedido y las secciones Inicio/Nuevo/Pedidos/Perfil **no son rutas**: viven dentro del destino `home` por estado interno.

| Transición | Mecanismo | Evidencia |
|---|---|---|
| Welcome → Login (tras 2s) | `navigateToLogin()` = `popUpTo(WELCOME){inclusive=true}` → backstack queda solo `login` | `NavigationActions.kt:7-11`, `WelcomeScreen.kt:33-36` |
| Login → Registro / Registro → Login | `navigate` y `popBackStack(LOGIN)` (Atrás del sistema coherente) | `NavigationActions.kt:13-19` |
| Registro → Éxito | `navigateToRegisterSuccess()` (`register_success`) | `NavigationActions.kt:21-23` |
| Login o Éxito → Home | `navigateToHome()` = `popUpTo(LOGIN){inclusive=true}` + `launchSingleTop` → pila queda `[home]`; Atrás desde Home sale de la app | `NavigationActions.kt:25-32` |
| Secciones Inicio/Nuevo/Pedidos/Perfil | `selectedSection` por `remember { mutableStateOf }` en `MainScaffold`; sin entradas de pila | `MainScaffold.kt:43,73-84` |
| Pasos 1→4 del pedido | `currentStep` por `remember` dentro de `CreateOrderFlowScreen`; atrás/continuar mutan la variable | `CreateOrderFlowScreen.kt:107-133` |

**Argumentos de rutas:** ninguno (todas las rutas sin params). Login serializa `email,password` solo hacia el callback `onLoginClick`, que lo ignora (`AppNavHost.kt:34-37`).

## 5. Inventario de pantallas y flujos (B.2)

| Pantalla / elemento | Archivo | Responsabilidad real | Estado |
|---|---|---|---|
| Splash / bienvenida | `ui/screens/welcome/WelcomeScreen.kt` | Fondo verde, "ENVER / Sálvame la clase / La red de ayuda instantánea del campus", auto-navega a Login a los 2s | Implementada (estática) |
| Login | `ui/screens/auth/LoginScreen.kt` | Formulario visual (correo institucional, contraseña con toggle visibilidad, botón). **Sin validación ni autenticación**: `onLoginClick(email,pass)` navega directo a Home | Interfaz lista; funcionalidad simulada |
| Registro | `ui/screens/auth/RegisterScreen.kt` | Formulario (nombre, correo, teléfono con selector de país de 10 entradas `COUNTRIES`, contraseña con medidor estático, checkbox términos). **Sin validaciones reales**; `onSubmitClick` navega a éxito sin registrar nada | Interfaz lista; funcionalidad simulada |
| Éxito post-registro | `ui/screens/onboarding/WelcomeSuccessScreen.kt` | Confirmación con tarjeta de perfil estática ("Mateo Alarcón", "MA", Rol Solicitant..., campus Central UTTT) e ítems informativos; botón → Home | Interfaz lista; datos hardcodeados |
| MainScaffold | `ui/components/navigation/MainScaffold.kt` | Scaffold con bottom-bar fija + header global; cambia sección por estado; **holder del estado del pedido** (`CreateOrderUiState` hoisted) | Implementado |
| Bottom navigation | `ui/components/navigation/EnverBottomNavigationBar.kt` | 4 destinos: Inicio, FAB "+", Pedidos, Perfil (solo iconos; activo verde) | Implementada; Pedidos/Perfil muestran placeholder "Pantalla en construcción" (`MainScaffold.kt:82-83,92-109`) |
| Header global | `ui/components/common/EnverGlobalHeader.kt` | "ENVER — SÁLVAME LA CLASE" + "Campus Central - Monterrey"; solo instancia en `MainScaffold` | Implementado |
| Home (sección Inicio) | `ui/screens/home/HomeScreen.kt` | Saludo "¡Hola, Mateo!", badge "Nivel 4 - Rescatista", switch Necesito/Quiero repartir (solo cambia `selectedTab`; no cambia contenido), chips filtro (datos solo para "Lo más solicitado": 2 `OrderItem` con imágenes locales; "Más reciente" → vacío), banner emergencia (→ sección Nuevo), "Mis pedidos activos" (vacío hardcodeado), banner verificación | Interfaz lista; datos estáticos |
| Pedido Paso 1 | `CreateOrderFlowScreen.kt` (`CreateOrderStep1Content`, líneas 136-391) | Categoría (4 chips), título y detalles obligatorios con mensajes de error, tarifa con sanitización (`sanitizeTarifa` línes 634-653) y validación `> 0` (`isValidTarifa` 655-658), foto opcional con cámara real, continuar→Paso 2 | Interfaz + validación local listas |
| Cámara (Paso 1) | `CreateOrderFlowScreen.kt:427-630` | Permiso `CAMERA` runtime, `TakePicture()` + `FileProvider` a `cacheDir/order_photos/`, vista previa con `decodeSampledBitmap` (≤1280), Quitar/Tomar otra | Implementado (estático); runtime por verificar |
| Pedido Paso 2 | `CreateOrderStep2Screen.kt` | Mapa Google real (GoogleMap 240dp, onMapClick fija pin), ubicación actual (FusedLocationProvider, permisos runtime FINE/COARSE), búsqueda Geocoder (5 resultados, sesgo ±0.02° del centro), chips "Edificio B (Seleccionado)"/"Edificio A" (estáticos), "Detalle para tu repartidor", 1 "lugar habitual" fijo, continuar→Paso 3 (sin validación de haber elegido punto) | Implementado (estático); mapa sin API key → vacío en runtime |
| Pedido Paso 3 | `CreateOrderStep3Screen.kt` | 3 opciones de tiempo (Ahora / En 1 hora / Hoy más tarde), selección única, botón deshabilitado hasta elegir, continuar→Paso 4 | Implementada (estática) |
| Pedido Paso 4 | `CreateOrderFlowScreen.kt:708-771` | Placeholder "Paso 4 en construcción" con progreso y regreso | Placeholder |
| Secciones Pedidos y Perfil | `MainScaffold.kt:82-83` | Placeholder textual | Placeholder |
| EnverImage/ImageSource | `ui/components/media/*` | `Local(resId)` pinta recurso; `Remote(url)` placeholder "Imagen no disponible" | Implementado (Local) |
| Emergencia | Inexistente como pantalla: el botón "Pedir auxilio" solo cambia la sección a `NUEVO` | `MainScaffold.kt:74-77` | Pendiente |

**Estado compartido del flujo de pedidos** (clave para la migración):
- `CreateOrderUiState` (`CreateOrderFlowScreen.kt:80-97`): clase con `mutableStateOf` por campo; creada en `MainScaffold.kt:46` con `remember { ... }` → sobrevive cambios de sección/paso mientras `MainScaffold` esté en composición.
- `MainSection selectedSection` (`MainScaffold.kt:43`) y `CreateOrderUiState`: `remember` sin `rememberSaveable` → **se pierden en rotación/config change y muerte de proceso**.
- `currentStep` está en `CreateOrderFlowScreen.kt:107` (dentro de la sección) → al cambiar de sección y volver, **el paso vuelve a 1 aunque los datos se conservan**.
- No hay persistencia (DataStore/SharedPreferences/Room): nada del pedido ni de sesión sobrevive al proceso.

## 6. Estado funcional (B.3)

Clasificación (procedencia: lectura estática + CHANGELOG; **ninguna verificación en runtime en esta auditoría**, y el CHANGELOG registra "Sin emulador: no pruebas de runtime" en Etapas 2-8):

| Funcionalidad | Clasificación | Evidencia y notas |
|---|---|---|
| Compilación del proyecto | Implementada (según historial, no re-verificada aquí) | CHANGELOG Etapas 2–8: `compileDebugKotlin`/`assembleDebug` ✅ |
| Splash → Login | Implementada, solo inspeccionada estáticamente | `WelcomeScreen.kt:33-36`; lógica trivial |
| Login visual | Placeholder/simulación (no autentica) | `AppNavHost.kt:34-37` ignora credenciales |
| Registro visual | Placeholder/simulación (no registra ni valida) | `RegisterScreen.kt:47-57`; `onSubmitClick` directo a éxito |
| Registro exitoso → Home | Implementada estáticamente (navegación) | `AppNavHost.kt:51-55` |
| Home visual con datos estáticos | Implementada, estática | `HomeScreen.kt:58-79` (2 pedidos duros, resto vacío) |
| Barra inferior + secciones | Implementada, estática | `MainScaffold.kt` |
| Pedido Paso 1 (validaciones + tarifa) | Implementada, estática | `CreateOrderFlowScreen.kt:142-152,354-367,634-658` |
| Cámara (foto real + preview) | Implementada, estática; runtime por determinar | `CreateOrderFlowScreen.kt:427-630`; CHANGELOG Etapa 6: "No probado cámara en runtime" |
| Mapa Paso 2 | Implementada, estática; **runtime por determinar y probablemente degradada sin API key** | `CreateOrderStep2Screen.kt:399-432`; `app/build.gradle.kts:21-26`; MAPS_API_KEY ausente en `gradle.properties` |
| Permisos de ubicación | Implementada, estática | `CreateOrderStep2Screen.kt:108-167`; runtime por determinar |
| Geocoder | Implementada, estática | `CreateOrderStep2Screen.kt:170-209`; servicio de sistema, puede diferir por dispositivo |
| Selección tiempo Paso 3 | Implementada, estática | `CreateOrderStep3Screen.kt:115-226` |
| Confirmación/creación real del pedido (Paso 4) | Pendiente | Placeholder `CreateOrderFlowScreen.kt:708-771` |
| Pedidos activos/recientes reales | Pendiente | `HomeScreen.kt:78-79` lista vacía; `MainScaffold.kt:82` placeholder |
| Perfil | Pendiente | `MainScaffold.kt:83` placeholder |
| Emergencia (pantalla/flujo propio) | Pendiente | Solo cambia de sección |
| Persistencia (sesión, pedidos, fotos) | Pendiente | No existe DataStore/SharedPreferences/Room en el proyecto |
| Backend/API | Pendiente | No hay red (salvo servicios de Google vía lib de maps/location) |

**Compila vs. funciona:** el historial acredita compilación, pero **no existe evidencia de que mapa, cámara, permisos o el flujo completo funcionen en ejecución** — son "por verificar en dispositivo".

## 7. Arquitectura actual (B.4)

**Cómo están organizadas las responsabilidades hoy:**

- **UI/Componentes:** agrupados por funcionalidad (`ui/screens/*`) y compartidos (`ui/components/*`); tema centralizado en `ui/theme/Color.kt` (paleta `Enver*` única; p. ej. las 4 pantallas de flujo consumen `EnverGreen` etc.).
- **Estado de pantalla:** `mutableStateOf` + `remember` local en cada composable (p. ej. `LoginScreen.kt:40-42`, `HomeScreen.kt:53-54`). Estado compartido del pedido: clase mutable con `mutableStateOf` hoisted a `MainScaffold` (`CreateOrderFlowScreen.kt:80-97`, `MainScaffold.kt:46`).
- **Eventos:** callbacks simples hacia arriba (`onLoginClick`, `onSubmitClick`, `onBackClick`); pantallas desacopladas del `NavHostController` (solo `AppNavHost`/`NavigationActions` lo tocan) — convención explícita en `FRONTEND_ARCHITECTURE.md:75` y `NAVIGATION_FLOW.md:87-91`.
- **Navegación:** NavHost único con rutas string + `NavigationActions`; secciones y pasos por estado interno, no rutas.
- **Modelos/validaciones:** solo in-pantalla: `OrderItem` (`HomeScreen.kt:34-44`), `Country`/`COUNTRIES` (`RegisterScreen.kt:32-45`), `HabitualPlace` (`CreateOrderStep2Screen.kt:655-659`), `TimeOption` (`CreateOrderStep3Screen.kt:254-258`), funciones privadas de validación de tarifa (`CreateOrderFlowScreen.kt:634-658`). No hay capa de dominio.
- **Acceso a datos/persistencia:** inexistente. Todo es estado en memoria; fotos en `cacheDir`.
- **Integraciones externas:** Google Maps (maps-compose + play-services), FusedLocationProvider, Geocoder del sistema; cámara nativa por intent. Sin backend.
- **Errores:** validación de formularios con `isError`+`supportingText` (Paso 1); mensajes cuando permiso denegado (`CreateOrderStep2Screen.kt:480-487`, `CreateOrderFlowScreen.kt:571-585`); en Geocoder las excepciones se **tragan** (`catch (_: Exception) { null }`, `CreateOrderStep2Screen.kt:185-186`); sin manejo global de errores ni Result types.
- **Pruebas:** ninguna de proyecto (solo plantillas).
- **Logs:** ninguno (no hay `Log.*` ni librería de logging).

**Problemas confirmados:**

1. `MainScaffold` es un stateholder global de facto (sección activa + estado del pedido) con `remember` (no `rememberSaveable`): rotación o proceso nuevo **pierde la sección y el borrador del pedido**; volver a la sección NUEVO reinicia `currentStep` a 1 y el borrador persiste solo mientras la app vive.
2. Registro/Login sin validación alguna: se navega con campos vacíos; "medidor de seguridad" y badge "Se validará matrícula" son cosméticos (`RegisterScreen.kt:441-459,232-250`).
3. Fuera de la validación de tarifa (buena saniti­zación local) no hay reglas de negocio compartidas ni prevención de doble avanzar; el Paso 2 permite continuar sin punto de entrega elegido (`CreateOrderStep2Screen.kt:606-627`, botón sin `enabled`).
4. Datos "vivos" hardcodeados (usuario Mateo, nivel Rescatista, campus, +180 compañeros, 14 rescatistas activos, lugares habituales, edificios A/B) — consistentes con etapa visual, pero son compromisos visuales/textuales que deben preservarse y sustituirse luego por datos reales.
5. Sin tests y sin Git: no hay red de seguridad para refactors → esta es la brecha más crítica para adoptar SDD con confianza.
6. `images/` dentro de `java/com/example/enver/` contiene 2 PNG (con espacios en nombre) que duplican los de `res/drawable-nodpi`: archivos muertos en directorio de fuentes (`app/src/main/java/com/example/enver/images/`) — señalada ya como ubicación inválida en `CHANGELOG.md` Etapa 4 (línea 155): los movidos a res. Los de `java/` son copias remanentes.
7. `FRONTEND_ARCHITECTURE.md` desincronizado con la implementación (ver §2.10).
8. Release sin minify/optimización (`app/build.gradle.kts:31-37`) y `allowBackup="true"` en manifest — por decidir en la constitución de ENVER (MovilSDD endurece ambos).

**Oportunidades (no defectos):**
- La ya-existente disciplina de separación (callbacks puros, tema centralizado, entorno compartido MainScaffold) es una buena base para introducir stateholders (ViewModel) sin giro grande.
- `EnverImage`/`ImageSource` ya aislan la futura carga remota.
- `docs/CHANGELOG.md` ya funciona como memoria informal de decisiones — semilla de `Memoria.md`.

## 8. Seguridad y calidad (B.6)

| Área | Estado | Evidencia |
|---|---|---|
| Permisos | `CAMERA`, `FINE/COARSE_LOCATION` declarados y usados en runtime launchers coherentes con uso real | Manifest líneas 5-7; `CreateOrderFlowScreen.kt:458-468`; `CreateOrderStep2Screen.kt:147-167` |
| Claves / config sensible | MAPS_API_KEY por propiedad Gradle (vacía hoy). **No se leyeron/exponen secretos**; no hay otras claves en código (grep de build.gradle.kts). `gradle.properties`/`local.properties` siguen el patrón Android por defecto (gitignore incluye `local.properties`; `gradle.properties`-raíz NO está en `.gitignore:1-15`) | `app/build.gradle.kts:21-26`; `.gitignore` |
| Persistencia | Solo archivos de foto temporales en `cacheDir/order_photos/` vía FileProvider (`file_paths.xml`); nada más | `CreateOrderFlowScreen.kt:471-475` |
| Logs / errores | Sin logs; excepciones del Geocoder silenciadas | `CreateOrderStep2Screen.kt:185-186` |
| Red | Sin backend propio; sin `network_security_config`; sin INTERNET declarado propio (por determinar tras fusión con Play Services). HTTP no se usa en código propio | Manifest y §2.5 |
| Validaciones de formularios | Paso 1 completa (obligatorios + tarifa numerica >0 con sanitización); Login/Registro sin validación real; Paso 2 sin requisito de punto seleccionado | §5 y §7 |
| Pruebas | Solo plantillas JUnit4/Compose | §2.7 |
| Comandos de verificación disponibles | `.\gradlew.bat :app:compileDebugKotlin`, `.\gradlew.bat :app:assembleDebug`, `.\gradlew.bat :app:testDebugUnitTest` (sin tests propios), `.\gradlew.bat :app:lint` | Según Gradle configurado; los dos primeros acreditados por CHANGELOG |
| CI | No existe (`.github/` ausente; no Git) | §2.9 |

## 9. Inventario de archivos por ruta (índice de evidencia)

Código (todos leídos): `MainActivity.kt`; `navigation/NavRoutes.kt`, `AppNavHost.kt`, `NavigationActions.kt`; `ui/theme/Color.kt`, `Theme.kt`, `Type.kt`; `ui/components/common/EnverGlobalHeader.kt`; `ui/components/media/EnverImage.kt`, `ImageSource.kt`; `ui/components/navigation/MainScaffold.kt`, `EnverBottomNavigationBar.kt`; `ui/screens/welcome/WelcomeScreen.kt`; `ui/screens/auth/LoginScreen.kt`, `RegisterScreen.kt`; `ui/screens/onboarding/WelcomeSuccessScreen.kt`; `ui/screens/home/HomeScreen.kt`; `ui/screens/createorder/CreateOrderFlowScreen.kt`, `CreateOrderStep2Screen.kt`, `CreateOrderStep3Screen.kt`.

Config/recursos: `settings.gradle.kts`, `build.gradle.kts`, `app/build.gradle.kts`, `gradle/libs.versions.toml`, `gradle/wrapper/gradle-wrapper.properties`, `gradle/gradle-daemon-jvm.properties`, `app/src/main/AndroidManifest.xml`, `res/values/{strings,colors,themes}.xml`, `res/xml/{backup_rules,data_extraction_rules,file_paths}.xml`, `keepRules/rules.keep` (listado), `.gitignore`.

Tests: `app/src/test/java/com/example/enver/ExampleUnitTest.kt`; `app/src/androidTest/java/com/example/enver/ExampleInstrumentedTest.kt` (plantillas).

Docs: `docs/CHANGELOG.md`, `docs/FRONTEND_ARCHITECTURE.md`, `docs/NAVIGATION_FLOW.md`.

## 10. Limitaciones de esta auditoría (resumen)

- No se ejecutaron compilación/tests: el estado "compila" depende del CHANGELOG.
- Sin runtime/emulador: mapa, cámara, permisos, Geocoder, backstack real y cualquier comportamiento visual quedan **sin verificar en ejecución**.
- Sin Git: sin diff ni historial; no se puede confirmar qué cambió recientemente fuera del CHANGELOG.
- Se omite contenido de `gradle.properties`/`local.properties` (lectura parcial para diagnóstico, sin exponer secretos).
