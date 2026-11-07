# Plan técnico — [FT-000 provisional] [título]

> Se elabora DESPUÉS de un spec con Aprobación firmada y ANTES de tasks. Solo lectura de código y arquitectura: este documento propone, no implementa. Si el plan requiere tocar archivos protegidos no contemplados, se detiene y se pide revisión de spec.

## 0. Datos de control

| Campo | Valor |
|---|---|
| Especificación aprobada (v/ruta) | specs/…/spec.md — Aprobada el … |
| Estado del plan | Borrador / Aprobado por humano (fecha, revisor) |
| Autor | |

## 1. Arquitectura actual relevante

- Qué existe hoy donde se apoya este cambio (paquetes/clases/estado), con rutas exactas:
- Convención vigente que se conserva (ver constitución §B):
- Zona delicada alcanzada (p. ej. `MainScaffold`, `CreateOrderUiState`, permisos, manifest):

## 2. Diseño mínimo propuesto

- Nombres de clases/composables/objetos y su responsabilidad (1 línea cada uno):
- Cómo se inserta en lo existente SIN reescribir lo demás:
- Lo que explícitamente NO cambia (abstinencia deliberada — listar pantallas/archivos intocables):

## 3. Archivos que podrían cambiar

| Archivo | Tipo de cambio (crear/modificar) | Por qué | Nivel de riesgo |
|---|---|---|---|
| | | | |

## 4. Archivos protegidos (prohibidos en este slice)

- Lista explícita de lo que NO debe modificarse: manifest, `app/build.gradle.kts`, `libs.versions.toml`, navegación, tema, recursos, pantallas fuera del alcance, valores de `gradle.properties`/`local.properties`:

## 5. Flujo de datos y estados afectados

- Estados nuevos/existentes tocados (`UiState`, variables de composición, ViewModel si aplica):
- Quién posee el estado (remember/hoisting/ViewModel — decisión con porqué, alineada con decisiones pendientes de Memoria):
- Flujo entre pantallas/secciones (qué recibe y qué devuelve cada callback):

## 6. Compatibilidad y migraciones

- Cambios de API/estructura que afecten a otros archivos existentes (con lista):
- Restricciones del stack verificadas (Kotlin/AGP/Compose/maps — citar restricciones de compatibilidad del proyecto):
- Migraciones de datos: no existe ninguna hoy; si surgiera, describirla aquí con su plan:

## 7. Riesgos y mitigaciones

| Riesgo | Afecta a | Mitigación |
|---|---|---|
| | | |

## 8. Estrategia de pruebas

- Unitarias (qué, con qué lib ya disponible; aserciones honestas, no siempre-falso):
- Instrumentadas (solo si hay dispositivo/emulador; si no, declararlo):
- Verificación sin dispositivo (comandos que sí corren: `:app:assembleDebug`, `:app:lint`):

## 9. Comprobaciones de preservación visual

- Pantallas/componentes visibles afectados y elementos congelados (baseline de `docs/ENVER_UI_BASELINE.md`):
- Cuál evidencia se exigirá (capturas antes/después si hay dispositivo; revisiones de diff descriptivas si no):

## 10. Plan de reversión

- Con Git: revertir el/los commits del slice (rama prevista).
- Sin commits aprobados aún: copia previa de los N archivos que se tocarán (lista explícita).
- Criterio de activación del rollback (qué fallo lo dispara):

## 11. Condiciones para iniciar la implementación (DoD del plan)

- [ ] Spec con Aprobación (4 campos completos).
- [ ] Este plan revisado y APROBADO por humano (fecha+revisor en §0).
- [ ] Lista de tareas existente y sus límites aprobados.
- [ ] Decisiones pendientes bloqueantes resueltas o registradas como pendiente explícita.
