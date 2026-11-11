# Plan y tareas — CI/CD (implementación Checkpoint 4.7)

## Tareas

- [x] T-CI.1 `.github/workflows/ci.yml` NUEVO: `pull_request`+`push` a `main`; `permissions: contents: read`; acciones por tag mayor (checkout@v4, setup-java@v4 temurin 21, gradle/actions/setup-gradle@v4, upload-artifact@v4) — pendiente endurecer a SHA fijo (registrado); pasos: escaneo de secretos (grep, exit 1), `assembleDebug`, `testDebugUnitTest`, `lintDebug`, artefactos de reportes `if: always()` — verif REAL: comandos ejecutados localmente (ver T-CI.2).
- [x] T-CI.2 Verificación local de comandos del pipeline: `assembleDebug` ✅ exit 0; `testDebugUnitTest` ✅ 9/9 tests (8 nuevos ValidacionEntradaTest + 1 plantilla), 0 fallos; `lintDebug` ✅ exit 0 tras corrección autorizada de los 2 errores de lint (ver T-CI.2a/T-CI.2b); warning/hints restantes NO bloqueantes documentados (deps desactualizadas, ModifierParameter, UnusedResources, AutoboxingState, RedundantLabel) — log real por ejecución.
- [x] T-CI.2a (fix autorizado por instrucción del propietario, 2026-11-11) `CreateOrderStep2Screen.kt:427`: `MarkerState(position=…)` creado en composición → `rememberMarkerState(position = position)` (idioma oficial de maps-compose; recuerda el estado y sincroniza la posición vía LaunchedEffect; comportamiento del pin preservado). Verif: lintDebug exit 0; wizard sin cambios de diseño.
- [x] T-CI.2b (fix autorizado por instrucción del propietario) `AndroidManifest.xml`: `<uses-feature android:name="android.hardware.camera" android:required="false"/>` añadido tras el permiso CAMERA — NO añade ni modifica permisos; declara la cámara como hardware opcional (fix prescrito por Google para PermissionImpliesUnsupportedChromeOsHardware; sin cambio de comportamiento en teléfonos). NOTA: cambio de manifest documentado aquí por exigencia del skill enver-maps-camera-permissions; NO se creó spec independiente por ser corrección de lint de 1 línea autorizada expresamente.
- [x] T-CI.2c (limpieza propia) `RegisterScreen.kt`: `Uri.parse(url)` → `url.toUri()` (KTX, warning UseKtx en código introducido por FT-06; misma semántica).
- [x] T-CI.3 `AGENTS.md` §8 nuevo: 7 pasos obligatorios al cerrar cada bloque + regla de proporcionalidad — verif: edición aplicada.
- [ ] T-CI.4 (PENDIENTE propietario) Endurecer pins a SHA; decidir corrección autorizada del lint preexistente; (futuro) CodeQL/dependabot cuando el repo lo permita.
- [ ] T-CI.5 (PENDIENTE GitHub) El pipeline se declara NO VERIFICADO en GitHub Actions hasta ver un run real; los resultados locales son la única evidencia actual.

## Seguridad (controles incorporados)
- Permisos mínimos del workflow (`contents: read`); sin credenciales en el YAML; escaneo de secretos como paso bloqueante; no se usan claves reales (Maps/AWS) en CI; artefactos solo de reportes de pruebas/lint.
