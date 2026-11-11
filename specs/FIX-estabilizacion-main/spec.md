# FIX — Estabilización de `main` (restos de fusiones duplicadas)

## 1. Contexto
Los 5 PR (FT-02..FT-05 + docs) se integraron a `main` por squash (Nov 9). Los merges de reconciliación del propietario re-introdujeron bloques antiguos: `main` @ `2067f01` NO compilaba.

## 2. Defectos verificados (grep del árbol de origin/main)
1. `MainScaffold.kt`: rama `PERFIL` duplicada (PerfilScreen + `SectionPlaceholder` inexistente) → unresolved reference.
2. `AppNavHost.kt`: dos `composable(NavRoutes.ARTICLE_DETAIL)`; el primero malformado (lambda sin coma + param inexistente `onArticleClick` dentro de `ArticleDetailScreen`); el segundo con versión FT-02 antigua.
3. `ArticleDetailScreen.kt`: `fun ArticleDetailScreen` duplicada (4 `Button(`, botón inerte FT-02).
4. `PedidosScreen.kt`: `fun PedidosScreen` duplicada.

## 3. Corrección (única operación justificada)
Reconstrucción de los 4 archivos con su última versión buena (`2339b78…`, contenido aprobado FT-05 — NO restauración antigua; `diff 2339b78→main` demostró que el único delta eran +24 líneas de restos). Resultado: −24 líneas exactas, 0 ediciones semánticas nuevas.

## 4. Criterios de aceptación
- 0 duplicados por grep; `assembleDebug` exit 0; wizard/PERFIL/etc. sin cambios semánticos.
- Log: `%TEMP%\opencode\enver_test_lint_ft07.txt` (assembleDebug green, 21 s up-to-date sobre árbol idéntico a 2339b78 ya compilado 38 s con tareas ejecutadas en 4.6).

## 5. Riesgos
- El resto de archivos de `main` no se tocó (solo los 4 afectados; diff confirmado).

## Aprobación
| Campo | Valor |
|---|---|
| Estado | Aprobada por instrucción (Checkpoint 4.7 §1.2: corregir en rama independiente los bloques duplicados demostrables) |
| Revisor | Propietario ENVER |
| Fecha | 2026-11-09 |
| Evidencia | Este spec + diff −24 + build |
