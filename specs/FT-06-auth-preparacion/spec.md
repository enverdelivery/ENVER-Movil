# FT-06 — Preparación de autenticación Login/Registro (sin contrato de servidor)

## 1. Requisito (fuente)
Sección 4 del Checkpoint 4.7: incorporar verificación de credenciales en Login, validación en Registro, estados de carga/error, indicador de seguridad y aviso de privacidad; preparar código para integrar las APIs de AWS cuando el contrato llegue — sin inventar endpoints/JSON/tokens.

## 2. Decisiones de alcance
- **No se navega a Home/éxito con credenciales sin verificar** (regla dura del checkpoint).
- Sin persistencia de sesión ni storage; sin capa HTTP real; sin `baseUrl` inventada.
- La integración se prepara con una interfaz `AuthRepository` + resultado sellado; la implementación actual devuelve un aviso honesto (integración pendiente) — la UI muestra el mensaje y NO simula éxito.

## 3. Diseño
- `data/AuthRepository.kt` NUEVO: `interface AuthRepository` (`iniciarSesion`, `registrarCuenta`), `sealed ResultadoAutenticacion { Exito; Error(mensaje) }`, `AuthRepositoryPendienteDeContrato` (error honesto). Al llegar el contrato AWS: única implementación que cambia; UI intacta.
- `ui/screens/auth/ValidacionEntrada.kt` NUEVO (lógica pura, testeable): `esEmailValido` (regex), `esTelefonoValido` (8–15 dígitos, separadores ignorados — regla provisional documentada), `evaluarNivelDeSeguridad` (0–4: longitud ≥8, ≥12, dígito, variedad), `etiquetaDeNivel` ("Vacía/Baja/Media/Alta/Muy alta"), `noVacio`.
- `LoginScreen.kt`: validación local (correo, contraseña no vacía) → mensaje bajo el botón; envío único por pulsación vía `rememberCoroutineScope` + repositorio; en `Exito` (futuro) navega por `onLoginClick`; hoy muestra error honesto y permanece en Login. Texto de error rojo #DC2626, `fillMaxWidth`, `fontSize 12.sp` (único añadido visual mínimo).
- `RegisterScreen.kt`: validaciones locales (nombre, correo, teléfono, contraseña, casilla obligatoria — no auto-marcada); medidor de contraseña ahora DINÁMICO (misma geometría 4 barritas; etiqueta/color por nivel: verde si nivel≥3, ámbar si no); link "🌐 Consultar Aviso de Privacidad Estudiantil" ahora clicable → `Intent(ACTION_VIEW)` + chooser al navegador con URL **exacta** `https://enverdelivery.github.io/AvisoDePrivacidad/`; abrir la página ≠ aceptar (la aceptación sigue siendo la casilla); envío válido → repositorio → mensaje honesto, sin navegación a éxito simulado. `AVISO_PRIVACIDAD_URL` como constante.
- `AppNavHost.kt`: SIN cambios (callbacks intactos; dejan de usarse en el flujo actual — wiring se reactivará con contrato real).

## 4. Límites y preservaciones
- No se modifica estética de botones/campos existentes (solo el Text de estado añadido bajo cada CTA y el color/etiqueta del medidor ya existente).
- Wizard, Perfil, Pedidos, Home: intactos.

## 5. Riesgos
- `runBlocking` descartado por bloqueo del hilo principal; se usó `rememberCoroutineScope` (idiomático, sin dependencias nuevas).
- El mensaje de integración pendiente es texto de interfaz, no error de red real; cuando exista el backend se mapearán sus códigos a `ResultadoAutenticacion.Error`.

## 6. Criterios de aceptación
- `assembleDebug` exit 0; `testDebugUnitTest` 9/9 (8 de `ValidacionEntradaTest`); lint: deuda preexistente documentada (no introducida por este slice); sin secretos en el diff; sin navegación simulada en Login/Registro.

## Aprobación
| Campo | Valor |
|---|---|
| Estado | Aprobada por instrucción (Checkpoint 4.7 §4; profesor) |
| Revisor | Propietario ENVER |
| Fecha | 2026-11-11 |
| Evidencia | Este spec + tests + logs |
