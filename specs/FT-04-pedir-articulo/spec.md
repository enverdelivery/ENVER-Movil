# FT-04 — Acción del botón "Pedir" en el detalle de artículo

## 1. Contexto y objetivo
El botón "Pedir" de FT-02 es inerte (`onClick = {}`). Objetivo: darle una acción real en memoria local — registrar el pedido del artículo y llevar al usuario a la sección Pedidos — sin backend, sin persistencia (provisional, declarado) y sin inventar datos.

## 2. Requisitos funcionales
1. Al pulsar "Pedir" en el detalle de un artículo existente, el artículo se registra como pedido en memoria local (datos reales del `OrderItem` mostrado; NADA inventado).
2. Tras el registro, la app vuelve a Home y la sección activa de la barra inferior pasa a ser **Pedidos**, mostrando la lista de pedidos hechos en esta sesión.
3. Cada artículo se registra como máx. 1 pedido (bloqueo idempotente para pulsaciones accidentales; re-pulsar "Pedir" en el mismo artículo no duplica, pero sí navega a Pedidos).
4. Si no hay pedidos registrados, PedidosScreen conserva su estado vacío actual (sin datos ficticios).
5. En la lista de pedidos se muestra un aviso honesto: pedido registrado localmente (demo provisional, SIN envío/confirmación de servidor).
6. Sin backend, persistencia, DI, ViewModels ni dependencias nuevas; el estado se pierde al reiniciar el proceso (provisionalidad declarada al usuario).

## 3. Requisitos no funcionales
- Cambios mínimos; reutilizar `OrderItem` existente como dato del pedido (sin modelo nuevo).
- Gate: `assembleDebug` exit 0 + greps + diff auditado.

## 4. Arquitectura y contratos internos
- Estado local de composición (constitución §A.6): `object PedidosLocalState` con `mutableStateListOf<OrderItem>` (reactivo composable, sin ViewModel).
- Return-to-section: `MainScaffold` acepta `seccionSolicitada: MainSection?` + `onSeccionConsumida` (LaunchedEffect consume y resetea; re-ejecutable en usos repetidos de "Pedir").
- Cadena: `ArticleDetailScreen(onPedirClick)` ← `AppNavHost` (marca bandera + popBackStack) → HOME recomposed → `MainScaffold` selecciona PEDIDOS → consume bandera. `HomeScreen` NO se toca (no participa en el nuevo flujo).

## 5. Criterios de aceptación
1. Pulsar "Pedir" guarda el artículo pedido → lista visible en Pedidos con imagen/título/costo reales y aviso demo.
2. Segunda pulsación sobre el mismo artículo no crea un segundo pedido.
3. Pedidos vacío (sin pedir nada) conserva el estado vacío de FT-03 idéntico.
4. Botón "Pedir": texto/color/tamaño sin cambios; back/TopAppBar sin cambios.
5. `assembleDebug` exit 0; diff limitado a los archivos del plan.

## 6. Plan de pruebas
- Gate de compilación + greps (`onClick = { }` de "Pedir" ya no existe; import LaunchedEffect presente).
- Pruebas device (tap real, flujo completo, recomposición) — [PEND] sin dispositivo.
- Límite verificado por código: dedup por `id` en `PedidosLocalState` (INSPECCIÓN, no runtime).

## 7. Riesgos, límites y deuda
- Estado pérdida al reiniciar (memoria de proceso) — provisionalidad afirmada en UI con el aviso demo.
- La Lista crece sin límite en la sesión — corte de detalle (los pedidos reales necesitan backend).
- Pantalla de pedidos usa `OrderItem` (campos de artículo sobran para pedido); cuando llegue el backend real, `PedidoItem` propio — [POR DEFINIR].

## Aprobación
| Campo | Valor |
|---|---|
| Estado | Aprobada por instrucción directa del propietario (Checkpoint 4.3, pasos 2/§Comportamiento esperado: demostración local en memoria provisional, wiring mínimo si el ciclo completo exigiera más) |
| Revisor | Propietario ENVER |
| Fecha | 2026-11-08 |
| Evidencia | Instrucción del Checkpoint 4.3 en el informe; limitaciones declaradas (§7) |
