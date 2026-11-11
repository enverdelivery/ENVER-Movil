# Plan y tareas — CI/CD (implementación Checkpoint 4.7)

## Tareas

- [x] T-CI.1 `.github/workflows/ci.yml` NUEVO: `pull_request`+`push` a `main`; `permissions: contents: read`; acciones por tag mayor (checkout@v4, setup-java@v4 temurin 21, gradle/actions/setup-gradle@v4, upload-artifact@v4) — pendiente endurecer a SHA fijo (registrado); pasos: escaneo de secretos (grep, exit 1), `assembleDebug`, `testDebugUnitTest`, `lintDebug`, artefactos de reportes `if: always()` — verif REAL: comandos ejecutados localmente (ver T-CI.2).
- [x] T-CI.2 Verificación local de comandos del pipeline: `assembleDebug` ✅ exit 0; `testDebugUnitTest` ✅ 9/9 tests (8 nuevos ValidacionEntradaTest + 1 plantilla), 0 fallos; `lintDebug` ❌ reproducible por deuda PREEXISTENTE (`CreateOrderStep2Screen.kt:427` UnrememberedMutableState, fuera del alcance — NO se corrigió sin autorización; CI quedará rojo en lint hasta resolverla) — log `%TEMP%\opencode\enver_test_lint_ft07.txt`.
- [x] T-CI.3 `AGENTS.md` §8 nuevo: 7 pasos obligatorios al cerrar cada bloque + regla de proporcionalidad — verif: edición aplicada.
- [ ] T-CI.4 (PENDIENTE propietario) Endurecer pins a SHA; decidir corrección autorizada del lint preexistente; (futuro) CodeQL/dependabot cuando el repo lo permita.
- [ ] T-CI.5 (PENDIENTE GitHub) El pipeline se declara NO VERIFICADO en GitHub Actions hasta ver un run real; los resultados locales son la única evidencia actual.

## Seguridad (controles incorporados)
- Permisos mínimos del workflow (`contents: read`); sin credenciales en el YAML; escaneo de secretos como paso bloqueante; no se usan claves reales (Maps/AWS) en CI; artefactos solo de reportes de pruebas/lint.
