---
name: enver-maps-camera-permissions
description: Contexto obligatorio antes de tocar mapa, cámara, permisos, Geocoder o URIs/archivos en ENVER. Contiene el inventario real, restricciones duras y preservaciones aprobadas. Siga este flujo antes de proponer cambios; verifique contra el código actual.
---

# Skill enver-maps-camera-permissions

> Activar SIEMPRE antes de proponer cambio alguno en: permisos Android (manifest), mapa (maps-compose), cámara (TakePicture / EnverImage), Geocoder, FileProvider/URI, camera_file_paths.xml, o build config de Maps.

## 1. Inventario real verificado (Checkpoint 2 — no suponer, citar)

- Deps vigentes: `com.google.android.gms:play-services-location`, `io.coil-kt:coil-compose`, `com.google.maps.android:maps-compose` **6.12.2** (compatible con Kotlin 2.2.10; cualquier otra versión de maps-compose rompe build — CHANGELOG Etapa 8).
- Permisos en manifest: `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, `READ_MEDIA_IMAGES` (Android 13+), `CAMERA`, INTERNET.
- Geocoder con fallback textual cuando el backend de geocoding falla (dirigirse a `Geocoder.isPresent()` / try-catch en el código de picking).
- Cámara: flujo TakePicture con URI vía FileProvider (`camera_file_paths.xml`) y `EnverImage(qualifier)`.
- `MAPS_API_KEY`: NO definida en `gradle.properties` a la fecha (mapa renderiza vacío en runtime). La clave se maneja SOLO por propiedad local — nunca escribir clave en archivos versionables.

## 2. Reglas duras para cualquier cambio de esta área

1. **Sin spec no hay cambio.** Permiso/mapa/cámara requieren spec + security-reviewer (AGENTS §5).
2. **Mínimo privilegio:** nunca añadir permisos que el spec no liste; sin `requestLegacyExternalStorage`, sin permisos deprecated.
3. **Ningún valor de clave** llega a repositorio: `local.properties` (ignorado) o propiedad de entorno; el build script referencia, no hardcode.
4. **No reemplazar mecanismos existentes** (FileProvider paths, EnverImage qualifiers, patrón TakePicture) — solo extenderlos si el spec lo dice.
5. **Flujo de permisos preservado:** primera solicitud → rationale si deniega → settings si deniega "para siempre"; el estado de denegación NO se silencia.
6. **Fallback del Geocoder:** si geocoding falla, el fallback textual actual sigue disponible; el error informa al usuario con el texto existente (no silenciar).
7. **Rotación/muerte de proceso:** la FotoStep no pierde la selected URI sin decisión aprobada (BUG ROTACIÓN documentado, decisión #1 pendiente de Memoria).
8. **Probar SOLO en dispositivo:** con emulador/no-device, GPS y Foto se declaran PENDIENTES; no alegar "probado" sin run real.

## 3. Zonas de alta sensibilidad (archivo:zona)

| Archivo | Razón de sensibilidad |
|---|---|
| `AndroidManifest.xml` | permisos, FileProvider |
| `camera_file_paths.xml` | rutas de imagen compartidas (scope del provider) |
| `MainScaffold.kt:43,46` | estado del wizard (`remember`) — zona CreateOrderUiState |
| `CreateOrderFlowScreen.kt:107` | reinicio de `currentStep` |
| `gradle/libs.versions.toml` | pareja Kotlin↔maps-compose |
| `app/build.gradle.kts` | inyección de MAPS_API_KEY— sin clave escrita |

## 4. Descripciones de los experimentos aprobados (no re-inventar)

- Captura de foto → preview con borrado/confirmación y overlays actuales.
- Selector de fuentes: cámara/galería preservados (sin nueva librería de imágenes).
- Ubicación: toggle sobre el mapa NO disponible sin API key; el marcador actual vive del Geocoder.

## 5. Checklist previo al cambio (completar ANTES del slice)

- [ ] Spec aprobada con los permisos/deny exactos.
- [ ] security-reviewer revisó el diff.
- [ ] Sin clave en repositorio (grep del diff por `AIza` y nombres de variable de clave).
- [ ] Evidencia de ejecución del comando disponible (o PENDIENTE declarado).
