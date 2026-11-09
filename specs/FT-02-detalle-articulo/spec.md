# FT-02-detalle-articulo - Detalle de artículo desde Home

> Elaborado con `specs/_template.md` (formato del profesor). Rector: constitución ENVER + `AGENTS.md`.
> Base: baseline `f3e58cc8d11cae21e5ba358ebdf4db5ba4e1a32f` publicado en `main`.

## 1. Contexto y objetivo

Problema: las tarjetas de artículos de Home (`OrderCardVerticalFullWidth`) no son clicables y sus datos (`OrderItem`) no tienen pantalla propia; el profesor no puede ver una navegación real pivotada desde la pantalla principal.

Alcance (esta feature):

- Nueva `ArticleDetailScreen` (`ui/screens/detail/`), full-screen sin barra inferior ni encabezado global (push de navegación, mismo patrón que la referencia MovilSDD).
- Ruta `article_detail/{articleId}` + botón/volver que regresa a Home (`popBackStack`).
- Clic de tarjeta de Home → detalle del artículo seleccionado.
- "Pedir" visible, SIN acción funcional (no simula solicitud/aceptación/transacción).
- Fuente única de datos: la lista demo se extrae de `HomeScreen` a `SampleArticles` (single source; evita duplicar datos).

Fuera de alcance: cambios de diseño en Home (solo el clic), login/registro, wizard de pedidos (`CreateOrderUiState`), mapas, cámara, barra inferior, encabezado global, persistencia, backend.

## 2. Requisitos funcionales

- `ArticleDetailScreen(articleId: String, onBackClick: () -> Unit, modifier: Modifier = Modifier)`:
  - Busca el artículo en `SampleArticles` por `id`; si no existe: título "Artículo no disponible" + botón "Volver" (robustez; no se inventan datos).
  - Imagen del artículo vía `EnverImage(source = ImageSource.Local(imageResId), contentDescription = title)`.
  - Nombre (`title`) y categoría (`category`), exactos de `OrderItem`.
  - Costo con el formato actual de `OrderItem.cost` (ej. `"$45 MXN"` — sin re-formatar).
  - Badge "100% tuyo" cuando `is100PercentYours == true`.
  - Solicitante (`requesterName`), calificación (`rating`) y facultad (`faculty`) reutilizadas cuando estén presentes (nulidad = fila omitida).
  - Botón principal **"Pedir"** (texto exacto), `onClick = {}` (sin ninguna acción).
- `NavRoutes.ARTICLE_DETAIL = "article_detail/{articleId}"`; siempre se navega con un `id` real del catálogo.
- `HomeScreen`: exposición de `onArticleClick: (String) -> Unit = {}` (default conserva compatibilidad); la tarjeta llama `onArticleClick(order.id)`.

## 3. Requisitos no funcionales

- Preservación visual: reutiliza los colores/tema/compartidos existentes (`EnverGreen`, `EnverCardBg`, `EnverBadgeBg`, `EnverTextMuted`, `EnverDarkGreen`, `EnverImage`) — no new hex; baseline Home y otras pantallas intactas.
- Build verde obligatorio (`assembleDebug`), sin dependencias nuevas.
- Navegación: sin pop inesperado del HOME; el stack queda Home → Detalle (pop regresa a Home).
- Sin secretos, sin logs nuevos, sin datos nuevos: solo reutiliza el catálogo demo.

## 4. Arquitectura y contratos internos

- Navegación string-routes (lo que existe en ENVER): `NavRoutes` + `NavigationActions.navigateToArticleDetail(articleId)` + entrada `composable` en `AppNavHost` parseando `{articleId}`.
- Estado: pantalla stateless (solo derivación del `OrderItem` por id); sin ViewModel ni capas nuevas (ENVER aún no las tiene).
- Hoisted list: `ui/screens/home/SampleArticles.kt` con `mostRequested` (los 2 `OrderItem` actuales, SIN cambios) y `byId(id)`. `HomeScreen` consume el objeto.
- Retención de estado de Home: `selectedTab`/`selectedFilter` cambian de `remember` a `rememberSaveable` (justificado: al regresar del push la ruta HOME se re-crea; sin ello se pierde el estado del usuario — regresión evitable en 2 líneas).
- Ajustes adicionales aprobados por esta spec: ① extracción del catálogo `SampleArticles.kt`; ② wiring `onArticleClick` que atraviesa `MainScaffold.kt` (param default, sin cambios de diseño); ③ `rememberSaveable` en 2 variables de Home; los 3 son mínimos y no alteran comportamiento visual.

## 5. Criterios de aceptación

1. Given el catálogo demo, When se toca una tarjeta (Calculadora o Cartulina) en Home, Then se abre el detalle con imagen/nombre/categoría/costo del artículo correcto.
2. Given detalle de un artículo con `is100PercentYours = true`, Then badge "100% tuyo" se muestra; con `false`, no.
3. Given artículo sin `requesterName`/`rating`/`faculty`, Then esas filas no aparecen (sin datos inventados).
4. Given abierta, When se pulsa back (sistema o botón), Then regresa a Home sin mostrar el detalle en la barra inferior.
5. Given barra inferior, Then presencia/elementos idénticos (sin nueva sección "Detalle").
6. Given la pantalla, Then "Pedir" visible y sin acción (código: `onClick = {}`).
7. Given `assembleDebug`, Then `BUILD SUCCESSFUL` exit 0.
8. Given diff del slice, Then solo esos 6 archivos (SampleArticles nuevo + Home, NavRoutes, NavigationActions, AppNavHost, ArticleDetailScreen) + docs.
9. Given id desconocido en la ruta, Then detalle muestra "Artículo no disponible" + botón "Volver" sin crash; Given `imageResId = null`, Then el bloque de imagen queda placeholder (igual que la tarjeta) y el resto de datos sí se muestran.
10. Los datos presentados son exactamente los del `OrderItem` fuente (comparación manual en diff, no hay otra fuente).

## 6. Plan de pruebas

- `.\gradlew.bat :app:assembleDebug` (exit 0 obligatorio) + greps de cierre (sin secretos, wiring).
- Diff completo auditado (0 líneas fuera de alcance).
- Preview `ArticleDetailScreenPreview` (tooling local; se nota). Verificación ONLY en compilación.
- PENDIENTES (sin dispositivo/emulador): click visual de tarjetas, popBackStack visual, navegación completa, 60fps — se declaran, NO aprobadas.

## 7. Riesgos, límites y deuda

- Sin dispositivo: la navegación completa y las transiciones NO están ejecutadas (limitación declarada).
- "Pedir" sin acción = deuda productiva conocida: se activará en el slice de pedidos real de futuro (FT-0X TBD); NO se simula nada.
- `rememberSaveable` es un cambio de detalle inofensivo pero queda a revisión del cierre manual del propietario: revisado en diff.
- Textos nuevos: "Pedir", "Volver", "Artículo no disponible" — resuelto en el alcance aprobado de la spec (Checkpoint 4.1).
- El detalle es full-screen sin bottom-bar: decisión de stack (profesor). Si el propietario prefiere con bottom bar, ajuste de navegación (cambio de alcance) → nueva mini-spec.

## Aprobación

| Estado | Revisor | Fecha | Evidencia |
|---|---|---|---|
| Aprobada | Propietario (Checkpoint 4.1, alcance explicito + "Pedir") | 2026-11-06 | instrucción escrita del checkpoint (Alcance aprobado FT-02) |
