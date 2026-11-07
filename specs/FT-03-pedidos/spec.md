# FT-03 — Pedidos (sección "Pedidos" del MainScaffold)

## 1. Objetivo
Reemplazar el placeholder de la sección **Pedidos** por una pantalla real con estado vacío honesto, accesible desde la barra de navegación inferior existente.

## 2. Alcance
**Incluido**
- Pantalla nueva: `ui/screens/pedidos/PedidosScreen.kt` (estado vacío: "Todavía no tienes pedidos" + mensaje explicativo).
- Wiring mínimo: `MainScaffold.kt` — rama `MainSection.PEDIDOS` ahora renderiza `PedidosScreen` (misma posición central que tenía el placeholder).

**Excluido**
- Rutas en `NavRoutes`/`NavigationActions`/`AppNavHost` (la sección vive dentro del `MainScaffold`, igual que INICIO/NUEVO; no es un destino NavHost).
- Cualquier botón CTA, dato inventado (pedidos, pagos, repartidores, estados, usuario), integración de backend o persistencia.
- Flujo Crear Pedido (`CreateOrderUiState` = zona delicada, no integrada), FT-02, login/registro, mapas/cámara, PERFIL (`SectionPlaceholder` queda intacto), bottom bar, encabezado global.

## 3. Comportamiento visible
1. Al pulsar "Pedidos" en la barra inferior, el contenido muestra título + estado vacío centrado.
2. El estado vacío usa el vocabulario visual existente: título color `Color(0xFF0F172A)`, texto secundario `EnverTextMuted`, tarjeta interior `EnverCardBg` con `RoundedCornerShape(16.dp)`.
3. No hay navegación hacia fuera; volver a "Inicio" conserva el estado del perfil de Home.

## 4. Criterios de verificación
- `assembleDebug` BUILD SUCCESSFUL exit 0.
- Diff limitado a: `PedidosScreen.kt` (nuevo), `MainScaffold.kt` (1 rama del `when`), specs FT-03, `Memoria.md`.
- El placeholder de PERFIL no cambia; la semántica de `MainSection` no cambia.
- Sin iconos/recursos nuevos; sin textos que inventen datos de pedidos.

## 5. Riesgos y limitaciones
- Verificación de interacción/tap requiere dispositivo/emulador — PENDIENTE.
- Si el diseño real de "Pedidos" llega más adelante, esta pantalla queda como versión 1 (estado vacío) y se reemplaza por su versión con datos.

## 6. Archivos
| Archivo | Acción |
|---|---|
| `app/src/main/java/com/uttt.enverdelivery/app/ui/screens/pedidos/PedidosScreen.kt` | Nuevo |
| `app/src/main/java/com/uttt.enverdelivery/app/ui/components/navigation/MainScaffold.kt` | Edición mínima |
| `specs/FT-03-pedidos/{spec,plan,tasks}.md` | Nuevos |
| `Memoria.md` | Registro |

## Aprobación
| Campo | Valor |
|---|---|
| Estado | Aprobada (Checkpoint 4.2 — instrucciones del propietario: alcance inicial propuesto + método declarados) |
| Revisor | Propietario ENVER |
| Fecha | 2026-11-06 |
| Evidencia | Alcance/método definidos en el mensaje Checkpoint 4.2; pantalla niega datos inexistentes en lugar de simularlos |
