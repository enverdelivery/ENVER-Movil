# Tasks - FT-02-detalle-articulo

> 1 slice = este incremento (solo FT-02). Orden topológico. Verificación real por tarea; no marca `[x]` sin evidencia.

## Fase 1: Datos + Home wiring

- [x] T02.1 `ui/screens/home/SampleArticles.kt` NUEVO (object con `mostRequested` = los 2 `OrderItem` actuales sin cambios, `byId(id)`) — verif REAL: comparación literal-exhaustiva vs respaldo FT-01 (14/14 literales idénticos; se corrigió acento de "Útiles" detectado en auto-revisión: 218→218)
- [x] T02.2 ídem alcance — verif REAL: incluida en build exit 0 (ver T02.7); botón interno "Ver 🚀" de la tarjeta también dispara onArticleClick (antes `onClick={}`); `rememberSaveable` en 2 vars + imports
- [x] T02.3 ídem — verif REAL: en build exit 0 (T02.7)

## Fase 2: Ruta + pantalla de detalle

- [x] T02.4 ídem — verif REAL: en build exit 0 (T02.7)
- [x] T02.5 ídem — 1º build FALLÓ por import de `R` faltante en SampleArticles (corregido en el mismo slice, atribuible al incremento); 2º build exit 0 (T02.7)
- [x] T02.6 ídem — verif REAL: en build exit 0 (T02.7)

## Fase 3: Verificación + docs + commit

- [x] T02.7 — verif REAL: `BUILD SUCCESSFUL in 7s`, exit 0, 36 tareas (6 ejecutadas/30 up-to-date); log `%TEMP%\\opencode\\enver_assembleDebug_ft02.txt`
- [x] T02.8 — verif REAL: git diff vs baseline f3e58cc: +37/−29 en app/src SOLO wiring/aprobado literales intactos; los literales demo viven ahora únicamente en SampleArticles (14/14 idénticos al backup)
- [x] T02.9 — verif: esta tabla + Memoria.md actualizados
- [ ] T02.10 Commit descriptivo + push a `origin/main` (sin --force) + verificación hash remoto — verif: `git ls-remote` == HEAD local.

> Limitación declarada permanente mientras no haya dispositivo: prueba visual del detalle (tap tarjeta, back, transición) queda PENDIENTE, no aprobada.

📌 Problema preexistente o fuera de alcance → STOP, documentar, pedir autorización.
