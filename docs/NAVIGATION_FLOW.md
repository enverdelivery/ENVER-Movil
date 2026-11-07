# ENVER — Flujo de Navegación

## Diagrama

```text
        arranque de la app
                │
                ▼
        WelcomeScreen (splash visual, 2s, auto-navegación)
                │  navigateToLogin + popUpTo(welcome){inclusive}
                ▼
           LoginScreen ◄──────────────────────┐
          │        │                          │
          │        │ "Iniciar Sesión en ENVER"│ "Regístrate aquí" / botón atrás /
          │        ▼                          │ "Inicia Sesión"
          │        │   REGISTER ──────────────┘
          │        │        │ (onSubmitClick)
          │        │        ▼
          │        │  WelcomeSuccessScreen (post-registro)
          │        │        │ "Ir al Home y explorar pedidos"
          ▼        │        ▼
          └────────┴─► navigateToHome()
                       navigate(HOME){ popUpTo(LOGIN){inclusive}; launchSingleTop }
                                │
                                ▼
                            MainScaffold (ruta home)  ← pila restante: [home]
                            ├─ barra inferior fija
                            └─ secciones: Inicio | Nuevo | Pedidos | Perfil
                               (cambian por estado; la pila no acumula pantallas)
```

## Rutas declaradas

| Ruta | Pantalla | Archivo |
|---|---|---|
| `welcome` | WelcomeScreen | `ui/screens/welcome/WelcomeScreen.kt` |
| `login` | LoginScreen | `ui/screens/auth/LoginScreen.kt` |
| `register` | RegisterScreen | `ui/screens/auth/RegisterScreen.kt` |
| `register_success` | WelcomeSuccessScreen | `ui/screens/onboarding/WelcomeSuccessScreen.kt` |
| `home` | MainScaffold (Home + secciones) | `ui/screens/home/HomeScreen.kt` + `ui/components/navigation/MainScaffold.kt` |

`startDestination = NavRoutes.WELCOME`.

**Nota:** `WelcomeSuccessScreen` es la pantalla de bienvenida *posterior al registro*
(distinta del splash `welcome`); se alcanza al pulsar "Crear mi Cuenta Universitaria".

## Responsabilidad de cada pantalla

- **WelcomeScreen**: splash visual exclusivo. Usa `LaunchedEffect` + `delay(2_000)`
  para navegar automáticamente a Login. Al salir de composición la corrutina se
  cancela sola (no quedan tareas activas). No es pantalla permanente ni parte del
  flujo de regreso.
- **LoginScreen**: interfaz visual; `onLoginClick(email, pass)` navega a Home como
  demostración (sin autenticación real ni validación de credenciales).
- **RegisterScreen**: interfaz visual con scroll vertical; `onSubmitClick` navega a
  `WelcomeSuccessScreen` (sin registro real).
- **WelcomeSuccessScreen**: confirma visualmente el registro; `onGoHomeClick` navega a Home.
- **MainScaffold/HomeScreen**: destino `home`, pantalla principal; barra inferior fija y secciones.

## Comportamiento del botón Atrás

- La transición Welcome → Login usa:
  `navigate(LOGIN) { popUpTo(WELCOME) { inclusive = true } }`
  por lo que el backstack queda **solo** con `login`: el botón Atrás de Android
  nunca regresa al Welcome (y no originalmente la pantalla no se recrea al rotar
  entre Login y Registro).
- Register ← → Login usan navegación normal de Navigation Compose: el Atrás del
  sistema regresa de Registro a Login de forma coherente.
- El botón de regreso visual de Registro (`onBackClick`) y el enlace "Inicia
  Sesión" usan `popBackStack(LOGIN)`, respetando posibles instancias previas.
- Entrada a Home (`navigateToHome`) usa `popUpTo(LOGIN){inclusive=true}` +
  `launchSingleTop`: la pila queda únicamente con `home`, por lo que el botón Atrás
  desde Home **no** regresa a Login, Registro, WelcomeSuccess ni al splash.

## Navegación inferior (MainScaffold)

- `NavRoutes.HOME` renderiza `MainScaffold` (`ui/components/navigation/MainScaffold.kt`).
- `MainScaffold` también coloca el **encabezado global** (`ui/components/common/EnverGlobalHeader.kt`) encima del contenido de cada sección: una sola instancia compartida por Home, Nuevo pedido (los 4 pasos lo heredan), Pedidos y Perfil. **No aparece en Welcome, Login, Register ni WelcomeSuccess** (intencional: solo la sección principal posterior a Home lo muestra).
- La barra (`EnverBottomNavigationBar`) vive **una sola vez** en el `bottomBar` del `Scaffold`; no se desplaza con el contenido y respeta `paddingValues`.
- Secciones `MainSection` (Inicio/Nuevo/Pedidos/Perfil) cambian **por estado** dentro del mismo destino: la barra retiene su selección y el contenido se reemplaza sin añadir entradas a la pila; el Atrás desde Home sale de la app.
- **Nuevo pedido** es UNA sola sección: `CreateOrderFlowScreen` gestiona 4 pasos con estado interno (`currentStep`) y `CreateOrderUiState` hoisted a `MainScaffold` (datos conservados al avanzar/regresar y entre cambios de sección). El Paso 1 valida título/detalles/tarifa y "Continuar a Ubicación" avanza al paso 2; los pasos 2–4 son placeholders conectados (diseño pendiente de referencia).
- El botón "Pedir auxilio" del banner de emergencia de Home cambia la sección a `NUEVO` (mismo destino `home`, sin NavHost adicional).
- Para agregar una sección real con barra: crear su pantalla en `ui/screens/<funcionalidad>/`, agregarla al `enum MainSection` y sustituir su rama en el `when` de `MainScaffold`. Para un paso del pedido: reemplazar el placeholder correspondiente en `CreateOrderFlowScreen`.

## Cómo agregar pantallas nuevas (resto del grafo)

1. La pantalla nueva define **solo** callbacks (jamás un `NavController` directo).
2. En `AppNavHost` se conectan con `NavigationActions` o `navController`.
3. Rutas nuevas en `NavRoutes.kt`; acciones con backstack especial en `NavigationActions.kt`.
4. Si la sección pertenece a la app principal (con barra inferior), se integra por
   `MainScaffold`, no como destino nuevo del grafo.
