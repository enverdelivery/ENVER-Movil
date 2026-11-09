# ENVER-SISTEMA — Especificación maestra del sistema móvil

> Spec maestra (describe el sistema completo; NO sustituye a las specs por feature `FT-XX`).
> Rector: `.specify/memory/constitution.md` + `AGENTS.md`. Plantilla: `specs/_template.md`.
> Convención de estados: **[IMPL]** implementado y verificado con evidencia · **[PEND]** definido, no implementado · **[POR DEFINIR]** sin decisión humana.

## 1. Contexto y objetivo

ENVER es una app Android de un solo módulo (`:app`, Kotlin/Jetpack Compose, paquete `com.uttt.enverdelivery.app`) para solicitar rescates de artículos dentro del campus: un usuario publica lo que necesita (flujo "Nuevo") y puede visualizar artículos publicados en el campus (Home) abriéndolos en detalle. Stack fijado por la constitución: Kotlin 2.2.10, AGP 9.2.1, Gradle 9.4.1, Compose BOM 2026.02.01, maps-compose 6.12.2, JDK 21.

## 2. Requisitos funcionales (estado real por superficie)

| Superficie | Estado | Detalle |
|---|---|---|
| Splash / Login / Registro (pantallas) | [IMPL] | Pantallas visuales navegan sin validar credenciales (`AppNavHost.kt`) — la validación real es [PEND] y depende de backend. |
| Onboarding (Welcome, WelcomeSuccess) | [IMPL] | Visual; sin sesión real ni persistencia. |
| Home (Inicio) | [IMPL] | Selector "Necesito algo"/"Quiero repartir", filtros, badges, 2 artículos demo (`SampleArticles`) → tarjeta clicable. |
| Detalle de artículo (FT-02) | [IMPL] | Ruta `article_detail/{articleId}` full-screen; datos reales del índice demo; botón "Pedir" **sin acción** [PEND acción aprobada]. |
| Crear pedido (wizard 3 pasos) | [IMPL] visual | Paso 0/1 (foto con cámara+FileProvider, direcciones), paso 2 (detalle, entrega), paso 3 (confirmación). Estado en `remember` — se pierde en rotación [POR DEFINIR #1]. |
| Sección Pedidos (FT-03) | [IMPL] visual | Estado vacío honesto: "Todavía no tienes pedidos". Sin datos porque no existen pedidos reales. |
| Sección Perfil | [IMPL] placeholder | "Pantalla en construcción". |
| Mapa | [IMPL] componente | `maps-compose` en wizard; `MAPS_API_KEY` NO proveída → se renderiza vacío (decisiones pendientes #3). |
| Cámara | [IMPL] componente | TakePicture + FileProvider `com.uttt.enverdelivery.app.fileprovider`; runtime sin dispositivo NO verificado. |
| Pedidos reales / pagos / repartidor / estados de pedido / notificaciones / calificación | NO EXISTE | Código 0 (grep auditado en Checkpoint 2/3.4); carece de requisito humano para spec — [POR DEFINIR]. |

## 3. Requisitos no funcionales

- [IMPL] Compilación verificable: `.\gradlew.bat :app:assembleDebug` verde reproducible (FT-01, FT-02, FT-03).
- [IMPL] Seguridad de secretos: `MAPS_API_KEY` solo como `findProperty` con fallback vacío; `local.properties` ignorado.
- [PEND] CI, tests de proyecto (0 hoy), análisis estático (no existen detekt/ktlint), minify/allowBackup (decisiones #4) — ver `specs/CI-CD/spec.md`.
- [POR DEFINIR] Persistencia local (DataStore u otro), sesión, terminología del dominio `Rescatista`/`repartidor` (pendiente #10).

## 4. Arquitectura y contratos internos

- Composable UI organizada: `ui/screens/{auth,welcome,onboarding,home,detail,pedidos,createorder}`, `ui/components/{common,media,navigation}`, `ui/theme`.
- Navegación: `AppNavHost` (Splash/Welcome/Login/Register/Home + `ARTICLE_DETAIL`) + `MainScaffold` con bottom bar de 4 secciones (INICIO/NUEVO/PEDIDOS/PERFIL) — secciones internas, no rutas NavHost.
- Estado: local de composición (sin ViewModel/DI — decisión: capas no ceremoniales, constitución §A.6); `CreateOrderUiState` hoisted en `MainScaffold` (zona delicada).
- Responsabilidades: el móvil hoy NO está conectado a ningún backend; todo dato es demo o memoria. Backend oficial [POR DEFINIR] — entrada de contratos: URL AWS anunciada por el profesor, PENDIENTE de entregar (ver `specs/APIs/README.md`).

## 5. Criterios de aceptación (del sistema, actual)

1. `assembleDebug` exit 0 en cada incremento publicado (evidencia en Memoria: FT-01 51 s, FT-02 7 s, FT-03 3 s).
2. Sin secretos en el tree versionable; diff auditado por humano en cada slice.
3. Preservación visual contra `docs/ENVER_UI_BASELINE.md` cuando el diff toca UI.
4. Pruebas visuales/back/cámara requieren dispositivo — se declaran [PEND] si no se ejecutan.

## 6. Plan de pruebas (disponibles hoy)

- `.\gradlew.bat :app:assembleDebug` (gate real, ejecutado en cada checkpoint).
- Greps de cierre por slice (imports, autoridad FileProvider, literal de test, secretos) + `git diff` auditado.
- [PEND] Tests unitarios/instrumentados (no existen), ensayo con dispositivo (no disponible en el entorno del agente).

## 7. Riesgos, límites y deuda

- Cámara/mapa no verificados en runtime (sin dispositivo); `currentStep` del wizard se reinicia; borrador del pedido no sobrevive rotación (decisión pendiente #1).
- `docs/FRONTEND_ARCHITECTURE.md` desincronizado (deuda documental conocida).
- El código NO depende de specs/plan del repositorio de referencia (MovilSDD): solo la metodología SDD está alineada; el stack profesor (Koin/Kover/etc.) NO se copia.

## Aprobación

| Campo | Valor |
|---|---|
| Estado | Aprobada para uso como referencia viva (Checkpoint "Alineación rápida" 2026-11-08: instrucciones del propietario definen su creación y contenido; documento NO ratifica nuevas decisiones de producto) |
| Revisor | Propietario ENVER |
| Fecha | 2026-11-08 |
| Evidencia | Todos los puntos trazan a Memoria.md / audit docs / código existente; nada inventado |
