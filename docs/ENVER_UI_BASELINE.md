# ENVER — Línea base visual (UI Baseline)

> Fecha: 2026-11-05
> Propósito: registrar TODO lo identificable en código que **debe preservarse sin cambios visuales** durante la reorganización SDD/arquitectónica. Este documento será el insumo de las verificaciones de "preservación visual" de los futuros gates.
> Alcance de evidencia: **solo lectura estática de código + `docs/CHANGELOG.md`**. NO se ejecutó la app ni se tomaron capturas. Todo lo que requiera confirmación visual en ejecución está marcado en §7.
> Regla de este documento: valores exactos (hex, sp, dp, textos) citados del código; si una decisión de migración depende de compara capturas reales, se exige evidencia (§7) antes de migrar.

---

## 1. Pantallas y flujos que deben preservarse

### 1.1 WelcomeScreen (splash) — `ui/screens/welcome/WelcomeScreen.kt`
- Fondo plano verde `EnverGreen` (0xFF10B981) a pantalla completa; padding de barras del sistema (`systemBarsPadding`).
- 2 círculos decorativos (320dp, `EnverGreenLight` al 15% de alfa) en esquinas superior-izquierda e inferior-derecha (`DecorativeCircles`, líneas 79-103).
- Centrado: "ENVER" (42sp, Black, blanco, letterSpacing 6sp) + "Sálvame la clase" (18sp Medium) + "La red de ayuda instantánea del campus" (14sp Normal, alfa 0.8, offset y=8dp).
- Comportamiento: auto-navega a Login tras `delay(2_000)` (líneas 33-36). **No puede quedar en backstack** (`popUpTo(WELCOME){inclusive}` en `NavigationActions.kt:7-11`).

### 1.2 LoginScreen — `ui/screens/auth/LoginScreen.kt`
- Fondo `EnverBackground` (0xFFF8FAFC), padding 24dp, columna distribuida con SpaceBetween.
- Header centrado: círculo verde 42dp con "⚡" blanco 20sp + bloque de texto "ENVER" (24sp ExtraBold, 0xFF0F172A) sobre "SÁLVAME LA CLASE" (11sp Bold, `EnverDarkGreen`, letterSpacing 1sp).
- "Bienvenido de vuelta!" 26sp Bold (0xFF1E293B); subtexto con salto de línea explícito (13sp muted, Center).
- Tarjeta blanca 24dp de esquinas, elevación 2dp, padding 20dp con:
  - Fila "Correo institucional" / "@campus.edu / @tec.mx" (verde 10sp).
  - Campo correo: íconos `School`, placeholder "tu.matricula@campus.edu" (0xFF94A3B8), fondo `EnverInputBg` (0xFFF1F5F9), bordes transparentes, cursor verde, texto introducido 0xFF0F172A.
  - Fila "Contraseña" + "¿Olvidaste tu contraseña?" (10sp `EnverDarkGreen`).
  - Campo contraseña con `Lock`, toggle `Visibility/VisibilityOff`, placeholder "••••••••••••", transformación de contraseña.
  - Botón principal: "Iniciar Sesión en ENVER" + "→" (blanco 14-16sp Bold), esquinas 25dp, verde, alto 50dp.
- Footer: "¿Aún no tienes cuenta? " + "Regístrate aquí" (verde oscuro Bold 13sp); tarjeta blanca 20dp con `Shield` verde + "Exclusivo para estudiantes activos y personal\nuniversitario acreditado." (10sp, 0xFF334155, Center).

### 1.3 RegisterScreen — `ui/screens/auth/RegisterScreen.kt`
- Fondo `EnverBackground`, padding 20dp, scroll vertical.
- Header: botón circular blanco atrás (38dp, `ArrowBack`), pill "Paso 1 de 2 · Datos Básicos" (fondo 0xFFE2E8F0, punto verde 6dp, 11sp 0xFF334155), botón circular blanco de ayuda.
- Título "Registro de Estudiante" (24sp ExtraBold) + subtexto 2 líneas (12sp muted).
- Campos (todos con fondo `EnverInputBg`, bordes transparentes, esquinas 12dp): Nombre completo (icono `Badge`, placeholder "ej. Mateo Alarcón"), Correo institucional (`MailOutline`, placeholder "ej. mateo.alarcon@campus.edu", badge verde "Se validará matrícula"), WhatsApp/Teléfono (etiqueta ámbar "Avisos de entrega" 0xFFD97706; selector de país 85dp con bandera+código " ▾" y `DropdownMenu` con las 10 entradas de `COUNTRIES`: MX +52, US +1, CA +1, BR +55, AR +54, CO +57, ES +34, GB +44, DE +49, FR +33; campo "55 1234 5678" con `ChatBubbleOutline`), Contraseña segura (toggle visibilidad, placeholder "🔑 Mínimo 8 caracteres").
- Medidor de seguridad STATICO: "Nivel de seguridad" / "Media" (ámbar Bold) + 4 barritas (3 verdes/1 gris 0xFFE2E8F0).
- Checkbox de términos verde + texto "Acepto los Términos y Condiciones y el Código de Honor de convivencia y auxilio estudiantil en el campus." + "🌐 Consultar Aviso de Privacidad Estudiantil" (10sp verde oscuro; **no clicable en código**, visual).
- Botón "Crear mi Cuenta Universitaria" (25dp esquinas, verde, 50dp alto).
- Footer "¿Ya tienes tu cuenta ENVER activa? " + "Inicia Sesión".
- Nota funcional (no visual): el submit no valida nada; registrarlo también como deuda en doc 1 §7.

### 1.4 WelcomeSuccessScreen — `ui/screens/onboarding/WelcomeSuccessScreen.kt`
- Fondo `EnverBackground`, padding 24dp, columna SpaceBetween.
- Badge "¡REGISTRO COMPLETADO! 🎉" (fondo `EnverBadgeBg` 0xFFD1FAE5, 20dp esquinas, punto verde, 11sp verde oscuro Bold).
- Círculo verde central 96dp con `CheckCircle` blanco 48dp, elevación 8dp.
- "¡Bienvenido a la comunidad\nENVER!" 24sp ExtraBold centrado (0xFF0F172A).
- Tarjeta de perfil (padding 16dp): avatar 40dp 0xFFE2E8F0 con "MA" verde oscuro; "ESTUDIANTE" (10sp, 0xFF64748B) + "Mateo Alarcón" (16sp Bold); badge "Activo" verde tenue con punto; división 0xFFF1F5F9; fila Rol Inicial ("Solicitante" con `PersonOutline` verde) / Campus Asignado ("Central UTTT" con `LocationOn`).
- Caja informativa `EnverInfoBg` (0xFFEEF2F6, 16dp): círculo morado 0xFF4F46E5 36dp con `DirectionsBike`, "¿Quieres ganar dinero libre?" y subtexto.
- Tip centrado ámbar: "💡 Tip: Tu primer rescate incluye entrega prioritaria" (0xFFD97706).
- Botón "Ir al Home y explorar pedidos" + "→" (verde, 50dp).
- Footer "Red segura estudiantil universitaria · 100% verificado" con `Shield` gris.

### 1.5 MainScaffold + navegación inferior — `ui/components/navigation/MainScaffold.kt`, `EnverBottomNavigationBar.kt`, `EnverGlobalHeader.kt`
- `Scaffold` con `containerColor = EnverBackground`; bottomBar = barra inferior; contenido = Column(header global + sección activa con `weight(1f)`).
- **EnverGlobalHeader** (solo instancia en MainScaffold): `Box` con rayo ⚡ en círculo verde 40dp alineado CenterStart; bloque central centrado con "ENVER — SÁLVAME LA CLASE" (14sp ExtraBold) y debajo "Campus Central - Monterrey" (11sp muted con `LocationOn` 12dp). Padding 20/10dp. **Nunca duplicar en pantallas** (convención de `FRONTEND_ARCHITECTURE.md:66`).
- **EnverBottomNavigationBar**: Surface blanca, sombra 8dp, alto 64dp, padding horizontal 24dp; iconos outlined 24dp (`Home`, `Receipt`, `PersonOutline`); activo `EnverGreen`, inactivo `EnverTextMuted`; FAB central circular verde 44dp con `Add` blanco. **Solo iconos, sin etiquetas.**
- Secciones: Inicio | Nuevo (FAB) | Pedidos | Perfil; Pedidos y Perfil = placeholders "Pantalla en construcción" (18sp Bold 0xFF0F172A + 12sp muted). Los placeholders SON parte de la línea base actual (su diseño real es deuda).
- Navegación inferior cambia por estado (no rutas); mantener ese comportamiento al migrar a rutas reales si se decide (ver doc 3, decisión abierta).

### 1.6 Home (sección Inicio) — `ui/screens/home/HomeScreen.kt`
- Saludo "¡Hola, Mateo! 👋" (26sp Bold) + "¿Qué te salva la clase hoy?" (13sp muted); a la derecha badge "⚡ Nivel 4 - Rescatista" (fondo `EnverBadgeBg`, 12dp esquinas, 10sp Bold verde oscuro). **Única ubicación de la insignia.**
- Switch de modos (contenedor `EnverInputBg` 28dp esquinas, padding 4dp): "✦ Necesito algo" e "🛍 Quiero repartir" (`ShoppingBag` 14dp); activo verde con texto blanco, 24dp esquinas, 44dp alto. Cambiar de modo **no cambia el contenido actual** (comportamiento a preservar hasta tener datos).
- Chips de filtro: "🔥 Lo más solicitado" y "🕒 Más reciente"; blanco + borde 0xFFE2E8F0 / seleccionado fondo `EnverSubtleGreenBg` (0xFFE6F4EA) + borde verde + label `EnverDarkGreen`.
- Tarjetas de pedido (`OrderCardVerticalFullWidth`, apiladas con 16dp de separación): Card 24dp esquinas fondo `EnverCardBg` elevación 2dp; zona imagen 140dp fondo 0xFFCBD5E1 con `EnverImage(ImageSource.Local(...))` (recursos `articulo_calculadora_casio`, `articulo_cartulina_plumon` en `res/drawable-nodpi/`); badge flotante blanco alfa 0.9 con "🏷️" y categoría (10sp Bold 0xFF1E293B); etiqueta "ARTÍCULOS SOLICITADOS" (9sp Bold muted letterSpacing 0.5sp); título 15sp Bold máx 2 líneas con ellipsis; fila "COSTO"/"TU GANANCIA TOTAL" (9sp muted) + precio (18sp ExtraBold verde) + botón "Ver detalles →" (38dp alto, 16dp esquinas); badge opcional "✔ 100% tuya - Sin comisión de app" (fondo `EnverBadgeBg`, 9sp).
- Datos actuales: 2 pedidos de "Lo más solicitado" (Calculadora científica Casio fx-991 $45 MXN; Cartulina blanca + plumón negro $40 MXN con is100PercentYours, "Alex Martinez", "4.9 · 14 rescates", "TICs"); "Más reciente" → "No hay pedidos recientes".
- Banner emergencia: fondo 0xFF047857, 24dp esquinas, padding 20dp; badge "⚡ ULTRA EXPRESS" (fondo 0xFF065F46, blanco 10sp Bold) + "Entrega prom: 12 min" (0xFFA7F3D0); "¿Emergencia en plena\nclase?" (22sp ExtraBold blanco); "Lanzamos tu aviso a +180 compañeros\nque están caminando por tu edificio." (12sp 0xFFD1FAE5); botón blanco "⚡ Pedir auxilio" (texto 0xFF047857, 20dp esquinas, 44dp alto) → cambia de sección a NUEVO.
- "Mis pedidos activos" con punto verde 6dp + "+14 activos" (12sp muted SemiBold); Surface blanca 20dp con "No hay pedidos" centrado.
- Banner "100% Estudiantes Verificados" (`VerifiedUser` verde oscuro 20dp) con "Solo usuarios con correo @tec.mx o credencial oficial." (10sp).

### 1.7 Pedido Paso 1 — `ui/screens/createorder/CreateOrderFlowScreen.kt`
- Header de progreso compartido `OrderStepProgressHeader`: "PASO N DE 4" (11sp Bold verde) + `StepProgressBar` (4 segmentos `weight(1f)`, 6dp alto, 3dp esquinas, 6dp separación; verde si `step <= currentStep`, gris 0xFFE2E8F0 si no). Reutilizado en pasos 1-4: **mantener un solo componente**.
- "¿Qué necesitas salvar hoy?" (24sp ExtraBold 0xFF0F172A).
- Etiqueta "CATEGORÍA" (10sp Bold muted, letterSpacing 0.5sp) + 4 chips 72dp de alto esquinas 16dp (`Papelería 📄`, `Comida 🍔`, `Tecnología 💻`, `Otros 📦`); seleccionado verde texto blanco, no seleccionado `EnverCardBg` + borde 0xFFE2E8F0. **"Comida" es la categoría inicial** (`CreateOrderUiState.selectedCategory`).
- "¿Qué te urge?" + "Sé específico · Obligatorio" (10sp muted); campo multilínea 100dp con placeholder "Ej. Cable cargador tipo C de 2 metros o un sándwich de pollo de la cafetería central..."; error "El título del pedido es obligatorio" (0xFFDC2626 10sp).
- "Detalles / Instrucciones especiales" + campo 80dp placeholder "Si compras en la cafetería, sin mayonesa por favor"; error "Los detalles del pedido son obligatorios".
- "Establece tu tarifa" + campo con prefijo "$" (16sp Bold) y teclado `Decimal`; placeholder "Recuerda contemplar el precio del producto"; error "Ingresa una tarifa válida mayor a $0.00". Sanitización viva: solo dígitos + 1 punto, máx 2 decimales (`sanitizeTarifa`).
- "Foto o apunte de referencia (opcional)" + tarjeta `EnverCardBg` 20dp esquinas con:
  - Estado vacío: círculo `EnverBadgeBg` 48dp clicable con `CameraAlt` verde oscuro; "Tomar foto o subir captura" (13sp Bold); "Ayuda a que traigan el conector o modelo exacto" (11sp muted Center); 3 `SuggestionChip` inertes ("Apuntes", "Conector", "Menú" — 12dp esquinas, sin onClick).
  - Estado con foto: `Image` de la foto real (Crop, 180dp alto, 14dp esquinas); "Vista previa de tu foto" (11sp); botones "Quitar" (borde) y "Tomar otra" (verde) con íconos 16dp.
  - Permiso denegado: "Permiso de cámara denegado: puedes continuar sin fotografía." + "Volver a intentar" (TextButton verde oscuro).
- Botón "Continuar a Ubicación" + "→" (verde, 25dp, 52dp).

### 1.8 Pedido Paso 2 — `ui/screens/createorder/CreateOrderStep2Screen.kt`
- Botón circular blanco "Atrás" (36dp, `ArrowBack` 18dp) + "Atrás" 13sp Bold.
- `OrderStepProgressHeader(2)`.
- "¿Dónde te llevamos el auxilio?" (24sp) + subtexto 2 líneas (12sp muted).
- Buscador: `Search` verde, placeholder "Buscar lugar cercano (Ej. Biblioteca, Cafetería...)", fondo blanco, borde verde al focus / 0xFFE2E8F0 fuera, `ImeAction.Search`; trailing = `CircularProgressIndicator` verde 18dp buscando o `IconButton` de búsqueda. Resultados en Surface blanca 16dp esquinas + borde: dirección (12sp SemiBold) y línea secundaria (10sp muted).
- Chips estáticos: "Edificio B (Seleccionado)" (verde, icono `Check` blanco) y "Edificio A" (blanco, borde gris).
- Mapa real de Google (`GoogleMap`): 240dp alto, 24dp esquinas; `MapProperties(isMyLocationEnabled = permiso)`, zoom controls off, compass off; `onMapClick` pone/mueve el pin (Marker por defecto del SDK); overlay "📌 14 rescatistas activos cerca" (blanco alfa 0.9, 10sp Bold verde oscuro, esquinas 10dp) arriba-izquierda; banda inferior flotante `EnverDarkGreen` 20dp esquinas con punto verde y texto = dirección o "Toca el mapa para seleccionar el punto de entrega" (11sp Bold blanco).
- Sin permiso: "Permite la ubicación para ver tu posición actual en el mapa." (11sp muted); tocar el mapa (si no hay permiso) pide permisos.
- "Detalle para tu repartidor" (`EditLocation`) + campo con placeholder "Salón 302 - Fila del fondo junto a la ventana".
- "TUS LUGARES HABITUALES" (11sp Bold muted) + tarjeta única 16dp esquinas: círculo `EnverBadgeBg` 36dp con "🏛️", "Mi salón habitual (Edif B - 302)" (12sp Bold) + "Piso 3 · Facultad de Ingeniería" (10sp), `CheckCircle` verde si seleccionada / `RadioButtonUnchecked` gris; borde verde/gris. **Selección inicial: el primero** (`selectedHabitualPlace ?: first`).
- Botón "Continuar a tiempo de entrega" + "→"; NOTA: hoy continúa **sin exigir** punto seleccionado (deuda registrada en doc 1 §7; la decisión de endurecerlo es de alcance y necesita aprobación).
- Pie: punto verde + "Compañeros verificados con matrícula activa" (10sp muted).

### 1.9 Pedido Paso 3 — `ui/screens/createorder/CreateOrderStep3Screen.kt`
- "Atrás" circular igual al Paso 2 + `OrderStepProgressHeader(3)`.
- "¿Para cuándo lo necesitas?" (24sp) + "Selecciona el tiempo en el que necesitas que llegue tu pedido." (12sp muted).
- 3 tarjetas de selección única (16dp esquinas, borde verde al seleccionar): ⚡ `Bolt` "Ahora" — "Entrega inmediata, para lo más urgente"; 🕐 `Schedule` "En 1 hora" — "Con margen para que tu compañero lo consiga"; 📅 `Today` "Hoy más tarde" — "Para cuando termines tu clase". Círculo 36dp verde con icono blanco al seleccionar / `EnverBadgeBg` con icono verde oscuro; `CheckCircle`/`RadioButtonUnchecked` a la derecha.
- Sin selección: "Selecciona una opción para continuar" (11sp) y botón "Continuar con la confirmación" deshabilitado (contenedor muted alfa 0.3). Pie igual al Paso 2.

### 1.10 Pedido Paso 4 — placeholder
- "Paso 4 en construcción" + "El diseño de este paso se integrará al entregar el código de referencia. Tus datos del paso anterior se conservan." Botón "Continuar" deshabilitado. `OrderStepProgressHeader(4)`.

## 2. Componentes reutilizables a conservar (inventario)

| Componente | Archivo | Uso | Restricción |
|---|---|---|---|
| `EnverGlobalHeader` | `ui/components/common/EnverGlobalHeader.kt` | MainScaffold únicamente | No instanciar fuera; no duplicar |
| `EnverBottomNavigationBar` | `ui/components/navigation/EnverBottomNavigationBar.kt` | MainScaffold | 1 sola instancia en bottomBar |
| `MainScaffold` | `ui/components/navigation/MainScaffold.kt` | destino `home` | Holder estado pedido; revisar en refactor con preservación |
| `OrderStepProgressHeader`/`StepProgressBar` | `CreateOrderFlowScreen.kt:675-706` | pasos 1–4 | Componente único compartido |
| `OrderCardVerticalFullWidth` | `HomeScreen.kt:449-575` | Home | Estilo full-width apilado |
| `EnverImage` + `ImageSource` | `ui/components/media/` | Home (Local) | Punto único de migración remota |
| `CategoryChip`, `OrderPhotoSection` | `CreateOrderFlowScreen.kt:395-611` | Paso 1 | Estilo referencia |
| `HabitualPlace`, `TimeOption` (modelos UI) | Paso 2/3 | — | Datos actual de la referencia |

## 3. Paleta y tema (fuente de verdad: `ui/theme/Color.kt:14-22`)

| Nombre | Hex | Uso principal |
|---|---|---|
| `EnverGreen` | 0xFF10B981 | Primario: botones, chips, FAB, barra progreso, splash |
| `EnverGreenLight` | 0xFF6EE7B7 | Círculos decorativos del splash |
| `EnverDarkGreen` | 0xFF065F46 | Textos verdes, badge ULTRA EXPRESS, banda pin, iconos |
| `EnverBackground` | 0xFFF8FAFC | Fondo general |
| `EnverCardBg` | 0xFFFFFFFF | Tarjetas y campos del pedido |
| `EnverInputBg` | 0xFFF1F5F9 | Campos de Login/Registro |
| `EnverTextMuted` | 0xFF64748B | Textos secundarios, iconos inactivos |
| `EnverBadgeBg` | 0xFFD1FAE5 | Insígnias/círculos verdes tenues |
| `EnverSubtleGreenBg` | 0xFFE6F4EA | Chips de filtro seleccionados |
| `EnverInfoBg` | 0xFFEEF2F6 | Caja informativa del éxito |

Gris/rojo/ámbar/morado/carpetas puntuales usados inline: 0xFF0F172A / 0xFF1E293B / 0xFF334155 / 0xFF64748B (grises Slate como texto), 0xFFDC2626 (errores), 0xFFD97706 (ámbar avisos), 0xFF94A3B8 / 0xFFCBD5E1 / 0xFFE2E8F0 (placeholders/bordes), 0xFF047857 / 0xFF065F46 / 0xFFA7F3D0 / 0xFFD1FAE5 (banner emergencia), 0xFF4F46E5 (moto). Coexisten colores de plantilla sin uso en pantallas (Purple80/Pink40, `res/values/colors.xml` purple/teal).

**Matiz importante de tema:** `EnverTheme` (`Theme.kt:37-58`) usa `dynamicColor: Boolean = true` → en Android 12+ esquema dinámico del sistema, no la paleta ENVER. Las pantallas pintan colores con constantes (no `MaterialTheme.colorScheme`), por lo que el aspecto actual visual no depende del colorScheme activo — pero cualquier componente que use `MaterialTheme.colorScheme` (p. ej. `EnverImage` remoto usa `surfaceVariant`) puede variar por sistema. Registrar como decisión abierta en la constitución (desactivar dynamic color o escribir un esquema propio; NO decidir en esta auditoría).

Tipografía: `Type.kt` define solo `bodyLarge` (16sp/24sp lineHeight); el resto de tamaños va por parámetro directo (`sp`), consistente con la pareja de diseño entregada.

## 4. Textos importantes que deben mantenerse ( sposób de referencia rápida)

- "Sálvame la clase" / "La red de ayuda instantánea del campus" (splash)
- "ENVER — SÁLVAME LA CLASE" / "Campus Central - Monterrey" (header global)
- "Nivel 4 - Rescatista", "¡Hola, Mateo! 👋", "¿Qué te salva la clase hoy?" (Home)
- "Necesito algo" / "Quiero repartir"; "Lo más solicitado" / "Más reciente"
- "ULTRA EXPRESS", "Entrega prom: 12 min", "Pedir auxilio", "+14 activos"
- "100% Estudiantes Verificados" / "Solo usuarios con correo @tec.mx o credencial oficial."
- "PASO N DE 4"; mensajes de error del Paso 1 (§1.7)
- "Jugadores clave del Paso 2": "Toca el mapa para seleccionar el punto de entrega", "14 rescatistas activos cerca", "Edificio B (Seleccionado)", "Mi salón habitual (Edif B - 302)", "Compañeros verificados con matrícula activa"
- "Ahora" / "En 1 hora" / "Hoy más tarde" + sus descripciones
- ¡Todos los textos viven hardcodeados en Composables (`strings.xml` solo tiene `app_name`); si se decide externalizar a recursos, deberá demostrarse 1:1 el mismo renderizado.

## 5. Recursos y assets

- `res/drawable-nodpi/articulo_calculadora_casio.png`, `articulo_cartulina_plumon.png` (fotos de las tarjetas del Home; referenciadas vía `ImageSource.ORDER_*` en `ImageSource.kt:10-11`).
- Copias muertas en `app/src/main/java/com/example/enver/images/` ("Calculadora Casio.png", "Cartulina blaca + plumon negro.png"): NO se usan (duplicados en ubicación no válida; ver doc 1 §7 problema 6) — su limpieza es deuda documentada, no urgente.
- Iconos: material-icons-extended via `Icons.Outlined.*`. Iconos del launcher en `res/mipmap*`.
- Sin fuentes custom, sin vector assets propios (los `ic_launcher_foreground/background` son de plantilla).

## 6. Comportamientos interactivos no negociables (identificados en código)

1. Splash auto-navega a Login a los 2s y no queda en backstack.
2. Atrás del sistema: Login nunca vuelve al splash; Home nunca vuelve a Login/Registro; secciones y pasos no añaden entradas de pila (`NAVIGATION_FLOW.md`).
3. La barra inferior fija retiene la sección; "Pedir auxilio" lleva a NUEVO.
4. El borrador del pedido persiste entre pasos y secciones (mientras la app vive); el paso actual vuelve a 1 al re-contactar la sección.
5. Paso 1 exige título/detalles/tarifa válida para pasar; la tarifa se filtra mientras se escribe.
6. Paso 3 bloquea continuar sin opción elegida; Paso 2 no lo exige con el punto del mapa.
7. Cámara: pedir permiso solo al intentar fotografiar; permitir continuar sin foto; vista previa real con Quitar/Tomar otra.
8. Mapa: pedir ubicación al entrar; tocando el mapa se mueve el pin; buscador Geocoder sesgado al área visible; la cámara centra en el punto elegido (o en la ubicación actual si aún no hay punto).
9. Home: chips solo cambian la selección visual (no los datos); el FAB "+" va a NUEVO.

## 7. Evidencia disponible y capturas pendientes (requiere validación visual en ejecución)

**Evidencia disponible hoy:** código fuente íntegro + `docs/CHANGELOG.md` (con notas por etapa) + `docs/NAVIGATION_FLOW.md`. Nada más.

**Capturas/evidencia PENDIENTE antes de refactors que toquen UI** (cada una con dispositivo/emulador real):
1. Splash → Login (transición 2s) — confirmar sin parpadeo.
2. Login/Registro render completo (scroll, dropdown de países, medidor).
3. Home completo (scroll, imagenes de tarjetas, banner, estados vacíos).
4. Secciones Pedidos y Perfil (placeholders).
5. Paso 1: validaciones con errores visibles; cámara en runtime (tomar foto, preview, Quitar/Tomar otra; permiso denegado).
6. Paso 2: **mapa con API key** (hoy sin clave se renderiza vacío — ejecutar con `MAPS_API_KEY` seteada), pin al tocar, ubicación actual al aceptar permiso, Geocoder con resultados.
7. Paso 3: selección y botón habilitado.
8. Rotación de pantalla / cambio de config en MainScaffold — documentar el comportamiento actual (se pierde la sección activa y el borrador con `remember`) ANTES de decidir si es un comportamiento a preservar o una regresión a corregir con aprobación.

Sin esas capturas, el "gate de preservación visual" será textual/code-review, no comparativa visual: dejarlo explícito en cada PR.
