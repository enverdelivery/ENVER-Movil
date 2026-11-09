# Tareas — FT-04 Pedir artículo

- [x] T04.1 `PedidosLocalState.kt` NUEVO (`pedir(order)` idempotente por id) — verif: incluido en build exit 0 (T04.6).
- [x] T04.2 `PedidosScreen.kt`: rama lista (título/imagen/costo + aviso demo) cuando `pedidos.isNotEmpty()`; estado vacío intacto — verif: build T04.6.
- [x] T04.3 `ArticleDetailScreen.kt`: param `onPedirClick: () -> Unit = {}` + onClick = guardar pedido (dedup) + onPedirClick; comentario FT-02 actualizado — verif: build T04.6.
- [x] T04.4 `MainScaffold.kt`: params `seccionSolicitada: MainSection?` + `onSeccionConsumida` + LaunchedEffect consume — verif: build T04.6.
- [x] T04.5 `AppNavHost.kt`: bandera `seccionPedidosPendiente` + wiring HOME (params) y ARTICLE_DETAIL (onPedirClick = marcar bandera + pop) — verif: build T04.6.
- [x] T04.6 `.\gradlew.bat :app:assembleDebug` — log real: `%TEMP%\opencode\enver_assembleDebug_ft04.txt` — verif REAL: BUILD SUCCESSFUL exit 0 (34 s, 36 tareas: 4 ejecutadas/32 up-to-date).
- [x] T04.7 Greps + diff auditado del alcance (5 archivos Kotlin + SDD) — verif: salidas reales.
- [x] T04.8 `specs/README.md` índice FT-04 + `Memoria.md` registro corto — verif: inspección.
- [x] T04.9 Rama `ft-04-pedir-articulo` (base `docs/sdd-alineacion` cdfb0d0) + commit + push upstream — verif: `ls-remote` == HEAD.
