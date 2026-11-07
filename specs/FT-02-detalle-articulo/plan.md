# Plan - FT-02-detalle-articulo

> Elaborado con el formato adoptado del profesor (Checkpoint 4.1). Rector: constitución + `AGENTS.md` + `spec.md`. Sin implementación.

## 1. Diseño (archivos)

```text
app/src/main/java/com/uttt.enverdelivery/app/
├── navigation/
│   ├── NavRoutes.kt            # + ARTICLE_DETAIL = "article_detail/{articleId}"
│   ├── NavigationActions.kt    # + navigateToArticleDetail(articleId)
│   └── AppNavHost.kt           # + composable(route) { ArticleDetailScreen(...) }
├── ui/screens/home/
│   ├── SampleArticles.kt       # NUEVO: object SampleArticles { mostRequested (los 2 OrderItem actuales SIN cambios); byId(id) }
│   └── HomeScreen.kt           # consume SampleArticles; tarjeta clic → onArticleClick(order.id); remember→rememberSaveable (2 vars)
├── ui/screens/detail/
│   └── ArticleDetailScreen.kt  # NUEVA pantalla (full-screen, back propio, "Pedir" sin acción)
└── ui/components/navigation/
    └── MainScaffold.kt         # wiring: + param onArticleClick (default {}), pasa a HomeScreen
```

Fuera: auth, wizard (`CreateOrderFlowScreen`/`CreateOrderUiState`), mapas, cámara, `EnverBottomNavigationBar`, `EnverGlobalHeader`, recursos, gradle.

## 2. Wiring de navegación (contrato)

1. `OrderCardVerticalFullWidth(order, onClick = {})` → `onArticleClick(order.id)` en el uso de `HomeScreen`.
2. `HomeScreen(onArticleClick=...)` → `MainScaffold(...)` lo recibe y re-envía.
3. `AppNavHost` `composable(NavRoutes.HOME) { MainScaffold(onArticleClick = navigationActions::navigateToArticleDetail) }` — NOTES: NavigationActions.navigateToArticleDetail(articleId): `navController.navigate("article_detail/$articleId")` (stack push, sin popUpTo).
4. `composable(NavRoutes.ARTICLE_DETAIL) { backStackEntry -> ArticleDetailScreen(articleId = backStackEntry.arguments?.getString("articleId").orEmpty(), onBackClick = navigationActions.returnToHome?) }` — back = `navController.popBackStack()` (método pequeño en NavigationActions).
5. Ruta HOME queda intacta; la barra inferior NO tiene nueva sección.

## 3. UI/preservación del detalle

- Scaffold con `containerColor = EnverBackground`; top-bar propia (Botón IconButton back ArrowBack + "Volver") o back gestual; layout Column scroll.
- Reutiliza exactamente: `EnverImage`, `ImageSource.Local`, `EnverGreen`, `EnverCardBg`, `EnverBadgeBg`, `EnverTextMuted`, `EnverDarkGreen`, `EnverSubtleGreenBg` (mismos tonos/tipografías del sistema actual); rounded corners estilo ENVER (20–24.dp).
- "Pedir": `Button(onClick = {})` con estilo de botón primario ENVER (`containerColor = EnverDarkGreen` existe? usar `EnverGreen`/`EnverDarkGreen` como tarjetas actuales). Sin navegación, sin toasts, sin try/catch.
- Badge "100% tuyo" (Surface) solo donde aplique; sin datos hardcodeados.

## 4. Gates (ENVER)

`assembleDebug` → greps de cierre → diff de alcance auditado → docs (tasks `[x]`, Memoria). Sin lint/detekt/kover (no existen en ENVER); las pruebas que requieren dispositivo/emulador se declaran PENDIENTES si no hay emulador.

## 5. Docs vivos a actualizar al cerrar

- `specs/FT-02-detalle-articulo/tasks.md` (marcar `[x]` con verif real).
- `Memoria.md` (estado del slice, decisiones, deuda, build real).
- `specs/FT-02-detalle-articulo/spec.md` Aprobación ya firmada por Checkpoint 4.1; conciliar al cierre.

## 6. Tareas

Ver `tasks.md`.
