# Plan — FT-03 Pedidos

## Diseño
```
app/src/main/java/com/uttt.enverdelivery/app/
├── ui/
│   ├── components/navigation/
│   │   └── MainScaffold.kt        [EDITAR] rama PEDIDOS -> PedidosScreen(...)
│   └── screens/pedidos/
│       └── PedidosScreen.kt       [NUEVO] pantalla de sección con estado vacío
```

## Decisiones
- Sin NavHost route: `PEDIDOS` es una sección de `MainScaffold` (cuando selectedSection), no un destino de navegación.
- Estado vacío centrado: título de sección + tarjeta con mensaje; colores existentes (`EnverCardBg`, `EnverTextMuted`, `Color(0xFF0F172A)`); sin drawables nuevos (solo existen los 2 `articulo_*.png`).
- Sin callback de acción: nada que disparar en estado vacío; las callsites no cambian.

## Gates
1. `assembleDebug` exit 0.
2. Diff auditado (solo árbol de arriba + SDD/Memoria).
3. Greps: ramas `INICIO`/`PERFIL` del `when` intactas; rama `PEDIDOS` apunta a `PedidosScreen`.
