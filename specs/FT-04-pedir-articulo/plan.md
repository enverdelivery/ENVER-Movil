# Plan — FT-04 Pedir artículo

## Diseño
```
app/src/main/java/com/uttt.enverdelivery/app/
├── navigation/
│   └── AppNavHost.kt                [EDITAR] bandera `seccionPedidosPendiente` + wiring onPedirClick + params HOME
├── ui/components/navigation/
│   └── MainScaffold.kt              [EDITAR] params seccionSolicitada/onSeccionConsumida + LaunchedEffect
├── ui/screens/detail/
│   └── ArticleDetailScreen.kt       [EDITAR] param onPedirClick; botón guarda pedido local (dedup por id) y llama onPedirClick; comentario actualizado
└── ui/screens/pedidos/
    ├── PedidosLocalState.kt         [NUEVO] object con mutableStateListOf<OrderItem> + pedir() idempotente
    └── PedidosScreen.kt             [EDITAR] rama lista cuando hay pedidos (imagen/título/costo + aviso demo); estado vacío intacto
```
SDD nuevos: `specs/FT-04-pedir-articulo/*`, índice `specs/README.md` (+1 fila), `Memoria.md` (+1 fila).

## Decisiones
- SIN rutas nuevas: el regreso a Pedidos es cambio de SECCIÓN dentro del MainScaffold (previa decisión FT-03: secciones no son rutas NavHost).
- SIN modelo nuevo: el pedido es el `OrderItem` del artículo (FT spec §7 lo marca como deuda de renombrado).
- Bandera en `AppNavHost` (remember mutable simple, consume-on-use vía LaunchedEffect en MainScaffold) — evita clases/ViewModels.

## Gates
1. `assembleDebug` exit 0.
2. Greps: `onPedirClick` presente en detalle+navhost; `seccionSolicitada` en MainScaffold; `mutableStateListOf` en PedidosScreen.
3. Diff auditado — SOLO árbol de arriba + SDD/Memoria; sin artefactos generados, sin secretos.
