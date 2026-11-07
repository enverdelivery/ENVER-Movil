# ENVER — Changelog

## Etapa 8 — Paso 2 con mapa real, buscador funcional y Paso 3 de tiempo (2026-10-31)

### Dependencias agregadas (justificadas para el mapa)
- `gradle/libs.versions.toml` + `app/build.gradle.kts`:
  - `com.google.maps.android:maps-compose:6.12.2` (Google Maps Compose; rama compatible con Kotlin 2.2.10 del proyecto — las 8.x/9.x exigen stdlib 2.4 y rompen la compilación).
  - `com.google.android.gms:play-services-maps:20.1.0` (Maps SDK explícito).
  - `com.google.android.gms:play-services-location:21.3.0` (ubicación actual vía FusedLocationProviderClient).

### Clave de Google Maps (pendiente de configurar)
- `AndroidManifest.xml`: `<meta-data com.google.android.geo.API_KEY value="${MAPS_API_KEY}"/>` dentro de `<application>`.
- `app/build.gradle.kts`: `defaultConfig.manifestPlaceholders["MAPS_API_KEY"]` toma la propiedad `MAPS_API_KEY` (vacía si no existe; **valor de referencia del mapa: "TBD_uv" comentado en el código**).
- **Para activar el mapa**: agregar en el `gradle.properties` raíz `MAPS_API_KEY=<clave del proyecto Google Cloud con Maps SDK for Android habilitado>`. Sin clave el mapa se renderiza en blanco en ejecución (no afecta la compilación).

### Permisos agregados (`AndroidManifest.xml`)
- `ACCESS_FINE_LOCATION` y `ACCESS_COARSE_LOCATION` (solo para mostrar la ubicación actual en el Paso 2).

### Paso 2 — `CreateOrderStep2Screen.kt` (reescrito)
- **Mapa real interactivo**: `GoogleMap` (240dp, esquinas 24dp; sustituye a la escena simulada). Mover, zoom y desplazamiento nativos. `onMapClick` coloca/mueve el pin sobre la coordenada tocada; banda flotante inferior muestra las coordenadas `Lat/Lng` del punto o el nombre si llegó por el buscador. El pin usa el icono por defecto del SDK (el hue verde no está disponible en maps-compose 6.x).
- **Ubicación actual**: permiso solicitado al entrar (`RequestMultiplePermissions`); al conceder, `FusedLocationProviderClient.lastLocation` centra la cámara y `isMyLocationEnabled=true`. Denegado → mensaje informativo (""Permite la ubicación…") y el mapa sigue funcionando con "Toca el mapa para seleccionar el punto de entrega" como etiqueta.
- **Buscador funcional**: `Geocoder` del sistema (`getFromLocationName`, 5 resultados, sesgado ±0.02° al centro visible), botón "Buscar" + `ImeAction.Search`; lista blanca de resultados bajo el campo; al elegir uno se anima la cámara (`CameraUpdateFactory`), se fija el pin y se muestra la dirección. El campo inicia vacío con placeholder ( igual que Login/Registro).
- **Detalle para tu repartidor**: placeholder real (gris `#94A3B8` cuando vacío, texto SIEMPRE en `#0F172A` al escribir); `locationDetail` inicia vacío (ya no viene preescrito).
- **Tus lugares habituales**: tarjeta clicable; cuando activa, borde verde + `CheckCircle` verde; cuando no, borde gris + `RadioButtonUnchecked` gris. Restaurado guarda `uiState.selectedHabitualPlace`.
- Los chips "Edificio B (Seleccionado)"/"Edificio A" y el pill "14 rescatistas cerca" quedan tal cuál la referencia visual.

### Paso 3 — nuevo `ui/screens/createorder/CreateOrderStep3Screen.kt`
- " ¿Para cuándo lo necesitas?" + "Selecciona el tiempo en el que necesitas que llegue tu pedido." con la misma tipografía/estilo de los pasos 1–2.
- 3 tarjetas de selección única (misma estética del Paso 2): ⚡ "Ahora" (entrega inmediata), 🕐 "En 1 hora", 📅 "Hoy más tarde".
- `uiState.deliveryTime` persiste; cuando no hay selección, sugerencia "Selecciona una opción para continuar" y **botón deshabilitado**; con selección, "Continuar con la confirmación" va al Paso 4; atrás → Paso 2; `OrderStepProgressHeader(3)`.

### Estado compartido (`CreateOrderUiState`)
- Nuevos campos: `selectedLat`, `selectedLng`, `selectedAddressLabel`, `selectedHabitualPlace`, `deliveryTime`; `locationSearch` y `locationDetail` inician vacíos (placeholders reales).

### Compilación
- `:app:compileDebugKotlin` ✅ · `:app:assembleDebug` ✅.
- Incidencias resueltas: maps-compose 9.0.1 y 8.4.0 exigen Kotlin 2.4 (proyecto en 2.2.10) → fijado 6.12.2; el parámetro `hue` no existe en esa API → marcador por defecto; `BitmapDescriptorFactory` no resolvió → se eliminó el icono personalizado.
- **No probado en runtime** (sin emulador): mapa, permisos, Geocoder y opciones del Paso 3 deben validarse en dispositivo; el mapa requiere la API key configurada.
- Intactos: Paso 1 (validaciones y cámara), placeholder del Paso 4, Home/Login/Register, navegación, barra inferior y encabezado global.

---


### Alcance del encabezado global (corrección)
- Retirado de `WelcomeScreen.kt`, `LoginScreen.kt`, `RegisterScreen.kt` y `WelcomeSuccessScreen.kt` (con sus imports; el `Spacer` original de Register/WelcomeSuccess restaurado). Sus diseños, formularios y navegación quedan intactos.
- Ahora se instancia **únicamente en `MainScaffold.kt`** (estructura compartida de la sección principal): cubre Home, Nuevo pedido (los 4 pasos heredan el mismo), Pedidos y Perfil, sin duplicarlo en `HomeScreen.kt` ni en los archivos de pasos.

### Diseño del encabezado (`EnverGlobalHeader.kt`)
- Nueva distribución: **rayo ⚡ en círculo verde a la izquierda** (40dp, `CenterStart`) y **bloque central centrado horizontalmente en pantalla** (align Center): "ENVER — SÁLVAME LA CLASE" (14sp ExtraBold) e inmediatamente debajo "Campus Central - Monterrey" (11sp, icono de ubicación 12dp). Círculo del rayo y textos centrados coexisten sin perder centrado real (Box + align).
- **Eliminada la insignia "Nivel 4 - Rescatista"** del encabezado (no trasladada a otras pantallas aparte de Home).

### Insignia "Nivel 4 - Rescatista" → bienvenida de Home
- `HomeScreen.kt`: el bloque de saludo pasó a `Row` (greeting a la izquierda, insignia a la derecha, `Alignment.Top`): "¡Hola, Mateo! 👋" / "¿Qué te salvas hoy?" intactos + `Surface` redondeada (12dp) verde tenue `EnverBadgeBg` con "⚡ Nivel 4 - Rescatista" (10sp bold, `EnverDarkGreen`). Único lugar de la app donde aparece.

### Indicador "PASO N DE 4" amplio y reutilizable (`CreateOrderFlowScreen.kt`)
- Extraído el componente compartido `OrderStepProgressHeader(currentStep)`: etiqueta + `StepProgressBar` integradas en un mismo bloque a **todo el ancho disponible** (segments `weight(1f)`, alto 6dp, espaciado 6dp; verde los pasos ≤ actual, gris los restantes).
- Usado por el Paso 1, el Paso 2 y los placeholders (pasos 3–4); el número y la barra se actualizan según el paso activo. El flujo/estado no se tocó.

### Paso 2 DE 4 — Ubicación (integración del código de referencia)
- Nuevo `ui/screens/createorder/CreateOrderStep2Screen.kt` (`CreateOrderStep2Content`), fiel a la referencia: "¿Dónde te llevamos el auxilio?", buscador (limpiar incluido), chips "Edificio B (Seleccionado)"/"Edificio A", escena de mapa con pin e informativo de rescatistas, "Detalle para tu repartidor", "Tus lugares habituales", botón "Continuar a tiempo de entrega" y línea "Compañeros verificados con matrícula activa".
- **Adaptado a la arquitectura**: sin `Scaffold`/barra duplicada (aporta `MainScaffold`, sección NUEVO activa), paleta duplicada mapeada al tema (mismos hex), indicador propio sustituido por `OrderStepProgressHeader(2)`, back circle → regresa al Paso 1, "Continuar" → Paso 3 (placeholder).
- **Estado compartido**: nuevos campos en `CreateOrderUiState` (`locationSearch`, `locationDetail` con los valores de la referencia) → se conservan al avanzar/regresar y entre secciones.
- **Sobre el mapa**: la referencia trae un mapa **simulado** (Box estático). Se integró tal cual (fielidad visual); el comportamiento real de "tocar para colocar/mover pin" requeriría un proveedor de mapas (p. ej. Google Maps SDK para Compose + clave de API). **Se identificará el requisito exacto cuando el usuario lo decida**; no se añadió dependencia ni clave, y no se inventaron edificios ni etiquetas.

### Compilación
- `:app:compileDebugKotlin` ✅ y `:app:assembleDebug` ✅ (solo warnings de API deprecada, preexistentes/estilo).
- Verificación estática: `EnverGlobalHeader(` solo en `MainScaffold`; "Nivel 4 - Rescatista" solo en Home; `OrderStepProgressHeader` compartido por pasos 1–4. Sin emulador: no se realizaron pruebas visuales en runtime.

### Pendiente para el Paso 2 real
- Decisión sobre proveedor de mapa interactivo (SDK + API key) o mantener la escena visual de la referencia.
- Datos reales de rescatistas cercanos y lugares habituales (hoy textuales de la referencia).
---

## Etapa 7 — Encabezado global: alcance, diseño, insignia y progreso amplio + Paso 2 (2026-10-30)

## Etapa 6 — Ajustes de Home, encabezado global y sección Nuevo pedido (2026-10-30)

### Análisis del código de referencia de Crear Pedido
- Solo se entregó el **Paso 1 de 4**; los pasos 2–4 no tienen referencia → implementados como placeholders conectados (avanzar/regresar, sin inventar diseño).
- Código tenía: paleta duplicada (mapeada al tema, mismos hex), barra inferior duplicada dentro de la pantalla (no se utilizó: la aporta `MainScaffold`), `Scaffold` propio (eliminado), falta de import `BorderStroke` (agregado), tarifa como texto libre sin validación, cámara solo visual y estado que se perdía entre pasos — todo corregido en la integración.

### Archivos creados
- `ui/components/common/EnverGlobalHeader.kt` — encabezado global ("ENVER — SÁLVAME LA CLASE", campus y badge "Nivel 4 - Rescatista"), una implementación compartida.
- `ui/screens/createorder/CreateOrderFlowScreen.kt` — sección Nuevo pedido: `CreateOrderUiState` (estado compartido, hoisted a `MainScaffold`: conserva datos entre pasos y secciones), flujo de 4 pasos en un solo destino (`currentStep` interno), Paso 1 fiel a la referencia con **validaciones** (título/detalles/tarifa obligatorios con mensajes visibles), **tarifa numérica** (teclado decimal, prefijo `$`, sanitizado: solo dígitos + 1 punto, máx 2 decimales, sin negativos/0) y **cámara real y opcional** (permiso `CAMERA` + `TakePicture` + `FileProvider`, vista previa real de la foto tomada, "Quitar"/"Tomar otra"; denegado → mensaje y se puede continuar sin foto). Pasos 2–4: placeholder con progreso, regresar y datos conservados (diseño pendiente de referencia; "Continuar a Ubicación" anticipa el paso 2).

### Archivos modificados
- `MainScaffold.kt` — agrega `EnverGlobalHeader` (una instancia para todas las secciones) y conecta `NUEVO` → `CreateOrderFlowScreen`; estado del flujo hoisted aquí.
- `HomeScreen.kt` — (A) "Mis pedidos activos" muestra contenedor con **"No hay pedidos"** cuando no existen (el espacio se conserva); (B) chips switch real: "Lo más solicitado" muestra sus 2 pedidos, "Más reciente" muestra **"No hay pedidos recientes"** en el mismo espacio (sin datos disponibles, nada inventado); (C) botón **"Pedir auxilio"** → navega a la sección Nuevo pedido (vía estado de `MainScaffold`, sin segundo NavHost); se retiró el header duplicado (lo cubre el global).
- `WelcomeScreen.kt`, `LoginScreen.kt`, `RegisterScreen.kt`, `WelcomeSuccessScreen.kt` — encabezado global insertado al inicio (contenido propio intacto; placeholders y diseños conservados).
- `AndroidManifest.xml` — permiso `CAMERA` + `FileProvider` (@xml/file_paths) para la cámara real.

### Archivos creados de recursos
- `res/xml/file_paths.xml` — rutas del FileProvider para fotos temporales del pedido.

### Navigation
- Sin rutas nuevas: los 4 pasos viven dentro del destino `home` como **estado interno de la sección NUEVO** (requisito: una sola sección del menú). Comportamiento del Atrás y flujos existentes intactos.

### Compilación
- `:app:compileDebugKotlin` ✅ · `:app:assembleDebug` ✅.
- **Validación solo estática/compilación**: sin emulador, no se probaron cámara ni navegación en runtime. Revisión de código: validaciones, switch de pedidos, estados vacíos y conectividad de pasos verificados en fuente.

### Pendientes
- Referencias de los **pasos 2–4** del pedido (actualmente placeholder; el estado ya los conecta).
- Datos reales de pedidos recientes y activos (hoy se muestra el estado vacío "No hay pedidos" / "No hay pedidos recientes").
- Persistencia de fotos capturadas (hoy en cache del app).

---

## Etapa 5 — Corrección visual de Home (2026-10-30)

Basado en el código de referencia del usuario. Cambios:

### `ui/screens/home/HomeScreen.kt`
- Pedidos convertidos a **disposición vertical** (`Column` + `OrderCardVerticalFullWidth` full-width, apilados con `Arrangement.spacedBy(16.dp)`); se eliminó el `LazyRow`/carrusel horizontal y el sub-composable `OrderCardItem` (reemplazado por el de la referencia).
- Nueva tarjeta full-width: zona de imagen superior de 140 dp (fondo gris `#CBD5E1` según referencia) que **carga las fotos locales existentes vía `EnverImage`+`ImageSource`** (`articulo_calculadora_casio`, `articulo_cartulina_plumon`, mapeadas con el nuevo campo `OrderItem.imageResId`), badge de categoría flotante, precio/botón "Ver detalles →" en fila y badge "100% tuya".
- Sub-filtros "Lo más solicitado"/"Más reciente" restyleados según referencia: fondo blanco + borde `#E2E8F0` / seleccionado verde tenue `#E6F4EA` (`EnverSubtleGreenBg`, nuevo color en el tema) con borde verde.
- Header: saludo 26sp; eliminado el avatar circular (queda el badge "Nivel 4 - Rescatista" rediseñado); botones de modo con contenedor 28dp/botones 24dp y altura 44dp.
- `orderList` actualizada a los 2 pedidos de la referencia (datos existentes conservados).
- **Intactos**: banner "¿Emergencia en plena clase?", "Mis pedidos activos", "100% Estudiantes Verificados" y scroll vertical general.
- Sin `Scaffold` ni barra propia (lo aporta `MainScaffold`, sin duplicación).

### `ui/components/navigation/EnverBottomNavigationBar.kt`
- **Únicos cambios**: las etiquetas de texto ("Inicio"/"Pedidos"/"Perfil") eliminadas — ahora **solo iconos** (24dp); activo verde, inactivo gris. Misma Surface blanca, sombra 8dp, altura 64dp, padding 24dp, FAB central de 44dp y navegación por estado de `MainScaffold`.

### `ui/theme/Color.kt`
- Añadido `EnverSubtleGreenBg` (`0xFFE6F4EA`, verde tenue para chips/fondos de subsecciones).

### `ui/components/navigation/MainScaffold.kt`, media/, navegación
- Sin cambios.

### Dependencias
- Ninguna nueva.

### Compilación
- `:app:compileDebugKotlin` ✅ y `:app:assembleDebug` ✅. Validación solo estática/compilación — sin emulador disponible, no se ejecutaron pruebas visuales en runtime.

### Pendientes
- Filtrado funcional de pedidos por chip (hoy solo cambia la selección visual; los datos son los de la referencia).

---

## Etapa 4 — Home y navegación principal (2026-10-30)

### Archivos creados
- `ui/screens/home/HomeScreen.kt` — código entregado, integrado tal cual en contenido + `OrderItem` (paleta `EnverHome*` centralizada al tema, mismos hex; sub-composable auxiliar `OrderCardItem` intacto).
- `ui/components/navigation/EnverBottomNavigationBar.kt` — barra inferior del diseño de Home, extraída como componente reutilizable (código idéntico).
- `ui/components/navigation/MainScaffold.kt` — contenedor compartido: `Scaffold` con la barra inferior fija y el contenido de la sección activa (`MainSection`: Inicio/Nuevo/Pedidos/Perfil). Solo `Inicio` tiene diseño real; las demás muestran placeholder mínimo documentado.
- `ui/components/media/ImageSource.kt` — modelo del origen de una imagen (`Local(resId)` / `Remote(url)`).
- `ui/components/media/EnverImage.kt` — componente único de carga: `Local` → `painterResource`; `Remote` → placeholder (la migración a nube solo requiere reescribir esa rama, sin tocar pantallas).

### Archivos modificados
- `navigation/NavRoutes.kt` — ruta `home`.
- `navigation/NavigationActions.kt` — `navigateToHome()`: `navigate(HOME){ popUpTo(LOGIN){inclusive=true}; launchSingleTop=true }`. `LOGIN` es siempre el fondo de pila en ambos flujos (el splash ya salió con popUpTo inclusivo), así que la pila queda solo con `home`: Atrás desde Home **no** regresa a Login/Registro/Success/splash.
- `navigation/AppNavHost.kt` — destino `home` → `MainScaffold`; `onLoginClick` y `onGoHomeClick` conectados a `navigateToHome()` (demostración visual, sin validar credenciales).
- Recursos: imágenes movidas de `java/.../images/` (ubicación inválida dentro de fuentes) → `res/drawable-nodpi/` con nombres válidos: `articulo_calculadora_casio.png` y `articulo_cartulina_plumon.png`. Son fotos de productos que corresponden a los títulos de `OrderItem`; sin referencias previas en código (verificado por grep).

### Dependencias
- Ninguna nueva (los iconos de Home pertenecen a `material-icons-extended` ya agregada).

### Compilación
- `:app:compileDebugKotlin` ✅ · `:app:assembleDebug` ✅. Validación solo estática/compilación — sin emulador en este entorno, no se ejecutaron pruebas visuales.

### Pendientes
- Diseño real de secciones Nuevo/Pedidos/Perfil (MainScaffold ya las cambia manteniendo la barra).
- Cargar imágenes en las tarjetas de pedidos cuando el diseño lo especifique (usar `EnverImage` + `ImageSource`).
- Migración de `ImageSource.Remote` a una lib de carga asíncrona cuando se defina el backend.

---

## Etapa 3 — Ajustes de formularios y pantalla post-registro (2026-10-30)

### Archivos modificados
- `ui/screens/auth/LoginScreen.kt` — texto introducido y cursor ahora oscuros (`focusedTextColor`/`unfocusedTextColor` `#0F172A`, `cursorColor` `#10B981`) en ambos campos; placeholders, iconos y fondos sin cambios.
- `ui/screens/auth/RegisterScreen.kt` —
  - mismo criterio de texto/cursor en los 5 campos;
  - selector de país ahora funcional: `DropdownMenu` con 10 países (MX, US, CA, BR, AR, CO, ES, GB, DE, FR), actualiza bandera y prefijo, conserva el número escrito (estado `selectedCountry` y `phone` independientes);
  - eliminado el campo "Confirmar contraseña" (estado `confirmPassword`/`isConfirmPasswordVisible`, icono `CheckCircleOutline`, etiqueta y campo);
  - espacio superior del formulario duplicado (`Spacer` inicial 8→16 dp; `Box` padding 20 dp intacto, el inferior no cambió).
- `ui/theme/Color.kt` — añade `EnverInfoBg` (`#EEF2F6`, caja informativa de la pantalla de éxito).
- `navigation/NavRoutes.kt` — nueva ruta `register_success`.
- `navigation/NavigationActions.kt` — nueva acción `navigateToRegisterSuccess()`.
- `navigation/AppNavHost.kt` — destino `register_success`; `onSubmitClick` del Registro ahora navega a `WelcomeSuccessScreen`; `onGoHomeClick` queda preparado (no-op) para el futuro Home.
- `docs/NAVIGATION_FLOW.md`, `docs/FRONTEND_ARCHITECTURE.md` — flujo y estructura actualizados.

### Archivos creados
- `ui/screens/onboarding/WelcomeSuccessScreen.kt` — pantalla de bienvenida post-registro (código entregado, paleta centralizada en el tema; visual idéntico). Distinción: `welcome` = splash inicial; `onboarding/WelcomeSuccessScreen` NO es splash.

### Dependencias
- Ninguna nueva.

### Pendientes
- Pantalla Home (cablear `onGoHomeClick`) y popup-to al llegar al Home.
- Emulador no disponible en este entorno: validación únicamente de compilación (`compileDebugKotlin` y `assembleDebug` OK).

---

## Etapa 2 — Frontend inicial y navegación (2026-10-30)

### Cambios

**Archivos creados**
- `ui/screens/auth/LoginScreen.kt` — código entregado como referencia, integrado tal cual.
- `ui/screens/auth/RegisterScreen.kt` — código entregado como referencia, integrado tal cual.
- `navigation/NavRoutes.kt` — constantes de rutas (`welcome`, `login`, `register`).
- `navigation/NavigationActions.kt` — acciones de navegación (incluye `popUpTo` inclusivo para no volver al Welcome).
- `navigation/AppNavHost.kt` — grafo de navegación y cableado de callbacks de las pantallas.
- `docs/FRONTEND_ARCHITECTURE.md`, `docs/NAVIGATION_FLOW.md`, `docs/CHANGELOG.md` — documentación técnica.

**Archivos renombrados / movidos**
- `ui/screens/WelcomeScreen.kt` → `ui/screens/welcome/WelcomeScreen.kt` (organización por funcionalidad).

**Archivos modificados**
- `ui/theme/Color.kt` — paleta ENVER centralizada: `EnverGreen`, `EnverGreenLight`, `EnverDarkGreen`, `EnverBackground`, `EnverCardBg`, `EnverInputBg`, `EnverTextMuted`, `EnverBadgeBg`.
- `ui/screens/welcome/WelcomeScreen.kt` — ahora recibe `onNavigateToLogin` y autonavega tras 2 s mediante `LaunchedEffect` + `delay` (se cancela sola al salir de composición).
- `MainActivity.kt` — delega a `AppNavHost` dentro de `EnverTheme`; ya no contiene composables propios.
- `gradle/libs.versions.toml`, `app/build.gradle.kts` — dependencias agregadas (ver abajo).

**Archivos eliminados**
- Ninguno (el `WelcomeScreen.kt` original se movió, no se perdió código).

### Dependencias agregadas

| Artefacto | Versión | Motivo |
|---|---|---|
| `androidx.navigation:navigation-compose` | 2.10.2 | Navegación real entre pantallas (no existía en el proyecto). |
| `androidx.compose.material:material-icons-extended` | 1.7.8 | Las pantallas entregadas usan iconos fuera del set core (`School`, `Shield`, `Visibility`, `HelpOutline`, `Badge`, `PhoneAndroid`, `ChatBubbleOutline`, `CheckCircleOutline`, `PersonOutline`). Última versión publicada; no gestionada por el BOM actual. |

No se actualizaron versiones de Gradle, Kotlin, AGP ni Compose BOM.

### Problemas detectados y soluciones

1. **Colisión de paletas**: `LoginScreen` definía `val EnverGreen…EnverTextMuted` a nivel de archivo, y `RegisterScreen` definía `EnverGreenReg…`; además `EnverGreen` ya existía en `ui/theme/Color.kt`. **Solución**: centralizar toda la paleta en `ui.theme.Color.kt` y reemplazar los sufijos `*Reg` por los nombres compartidos. Cero cambio visual (mismos valores hex).
2. **`Unresolved reference 'Preview'`** al integrar los archivos (el código entregado no importaba `androidx.compose.ui.tooling.preview.Preview`). **Solución**: agregar el import — único cambio fuera del alcance descrito.
3. Welcome duplicaría el splash nativo: el proyecto **no** tiene splash nativo configurado (`themes.xml` usa el theme por defecto), por lo que no había nada que conservar ni duplicar.

### Pendientes para etapas posteriores

- Pantalla de recuperación de contraseña (el callback `onForgotPasswordClick` está preparado y conectado como no-op en el grafo).
- Registro real (validación de matrícula, envío de PIN, backend).
- Autenticación real y estado de sesión persistente.
- Pantalla "Paso 2 de 2" del registro y posterior flujo principal (inicio, pedidos, perfil, historial).
- Componentes reutilizables en `ui/components/` cuando surjan shared real (ej. campos con estilo común).
- Validación en emulador/dispositivo físico (no disponible en este entorno).
