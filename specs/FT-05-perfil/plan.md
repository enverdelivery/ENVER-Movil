# Plan — FT-05 Perfil

## Diseño
```
app/src/main/java/com/uttt.enverdelivery/app/
├── ui/components/navigation/
│   └── MainScaffold.kt         [EDITAR] import + rama PERFIL -> PerfilScreen(fillMaxSize); SectionPlaceholder se conserva/elimina según usos (grep)
└── ui/screens/perfil/
    └── PerfilScreen.kt         [NUEVO] "Tu perfil" + tarjeta (icono PersonOutline + "Todavía no tienes información de perfil" + nota backend)
```
SDD: `specs/FT-05-perfil/{spec,plan,tasks}.md` + índice (+1 fila) + Memoria (+1 fila).

## Decisiones
- Mismo patrón FT-03 (estado vacío centrado), icono existente del proyecto (`PersonOutline`).
- Sin CTA ni botones; sin datos personales [POR DEFINIR].

## Gates
1. `assembleDebug` exit 0 (log FT-05).
2. Greps: rama PERFIL apunta a PerfilScreen; usos de SectionPlaceholder verificados (queda si NUEVO u otra lo usan — no hay).
3. Diff auditado: SOLO árbol de arriba + SDD/Memoria; sin secretos ni datos ficticios.
