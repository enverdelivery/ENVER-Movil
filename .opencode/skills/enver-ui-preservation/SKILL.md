---
name: enver-ui-preservation
description: Protocolo de preservación visual para cualquier cambio que toque composables/salidas en ENVER (wizard, home, login/registro, navegación). Congela valores exactos contra docs/ENVER_UI_BASELINE.md y define la evidencia exigida. NO use para cambios no visuales.
---

# Skill enver-ui-preservation

> Activar ANTES de editar cualquier composable, tema, recurso gráfico/textual o navegación del módulo `:app`.

## 1. Fuente de verdad visual

- `docs/ENVER_UI_BASELINE.md`: valores exactos congelados (colores hex, dp/sp, textos literales, orden y rutas de navegación, comportamiento del Back).
- Declara incertidumbres: secciones SinCapturaLocal (mapa/cámara/permisos pendientes de caps con dispositivo — baseline §7).

## 2. Flujo obligatorio antes de editar UI

1. **Extraer**: leer baseline de las pantallas afectadas; listar los valores congelados que el cambio toca (aunque sea tangencialmente).
2. **Declarar**: escribir en el plan §9 qué elementos quedan intocados y cuáles cambian con Aprobación del spec.
3. **Evidencia**: con dispositivo → capturas antes/después por pantalla afectada; sin dispositivo → revisión de diff con valores exactos citados (declarar el límite; no alegar verificación en runtime).

## 3. Congelados obligatorios (ilustrativos, no completos — el listado real vive en la baseline)

- Splash, Login, Registro, Home, Notificaciones (con su badge), NUEVO/PERFIL (secciones + backstack), overlays del wizard (dimensión, posición del botón Continuar, confirmación del último paso), header/topbar.
- Wizard: pasos 1/2/3 con sus overlays y portadas, tal como existen hoy.
- Tema: colores y estilos globales congelados sin spec; ninguna alteración de la paleta fuera de lo aprobado.

## 4. Análisis incompleto permitido (rule of engagement)

- Solo los elementos aprobados en el spec cambian; tocar una pantalla NO listada en el plan detiene el slice aunque parezca "el mismo patrón".
- Solo los elementos aprobados en el spec cambian; tocar una pantalla NO listada en el plan detiene el slice aunque parezca "el mismo patrón".
- Textos nuevos: a recursos de strings solo con decisión aprobada (pendiente #9 de Memoria); si el spec no lo cubre, no es excusa para hardcodear.

## 5. Checklist de salida del slice

- [ ] Diffsŭ de cada composable citan valores exactos aceptados.
- [ ] Pantallas no-listadas intocadas (verificado por grep/nombre en diff).
- [ ] Evidencia visual adjuntada o límite declarado.
- [ ] visual-preservation revisó con informe.
