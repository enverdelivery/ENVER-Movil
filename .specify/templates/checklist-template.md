# Checklist por feature — [FT-000 provisional] [título]

> Se copia a `specs/<slug>/checklists/checklist.md`. Regla: cada ítem es una comprobación específica, observable y vinculada a un requisito (R# del spec) o a una tarea (T# de tasks). PROHIBIDO escribir frases vagas ("verificar que todo funcione") — cada ítem lleva su evidencia.
> Marca: ✅ (evidencia) / ➖ (no aplica + justificación) / ⏳ (pendiente). Un ⏳ impide el cierre del slice.

## 1. Por requisito

| # | Requisito (R# del spec) | Comprobación observable | Evidencia esperada | Marca |
|---|---|---|---|---|
| 1 | R1 | [Qué se ve/mide exactamente, p. ej. "el botón Continuar queda deshabilitado (color muted) sin selección en el Paso 3"] | captura / diff / salida | |

## 2. Por criterio de aceptación (§6 del spec)

| # | Criterio (Dado/Cuando/Entonces condensado) | Cómo se comprueba | Evidencia | Marca |
|---|---|---|---|---|

## 3. Verificaciones de build y análisis

| # | Comando | Resultado | Evidencia | Marca |
|---|---|---|---|---|
| 3.1 | `.\gradlew.bat :app:assembleDebug` | | salida/fecha | |
| 3.2 | `.\gradlew.bat :app:lint` | | salida/fecha | |
| 3.3 | (si el slice añade tests) `.\gradlew.bat :app:testDebugUnitTest` | | salida | |

## 4. Preservación visual (si se toca UI)

| # Pantalla | Elemento congelado confirmado intacto | Comprobación (diff de valores exactos hex/sp/dp/texto) | Captura antes/después (o declaración de solo-diff) | Marca |
|---|---|---|---|---|

## 5. Seguridad/privacidad (si el slice toca datos, permisos o red)

| # | Comprobación | Evidencia | Marca |
|---|---|---|---|
| 5.1 | Diff sin secretos ni claves (`git diff` revisado; `gradle.properties`/`local.properties` fuera de staging) | | |
| 5.2 | Permisos añadidos == declarados en spec (ninguno extra) | manifest + spec | |

## 6. Cierre

| # | Comprobación | Evidencia | Marca |
|---|---|---|---|
| 6.1 | Todos los requisitos marcados ✅/➖ y ningún ⏳ | | |
| 6.2 | Memoria.md actualizada | link | |
| 6.3 | Diff completo listado 1:1 con archivos del plan | `git status`/`git diff` revisado | |
