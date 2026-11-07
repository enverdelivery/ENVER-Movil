# ENVER — Arquitectura del Frontend

> Estado: etapa visual (sin backend, API, base de datos ni autenticación real).

## Estructura de carpetas

```text
com.example.enver/
├── MainActivity.kt                 # Activity de entrada; solo instancia el tema + AppNavHost
├── navigation/
│   ├── AppNavHost.kt               # Grafo de navegación (NavHost) de la app
│   ├── NavRoutes.kt                # Constantes de rutas (welcome / login / register)
│   └── NavigationActions.kt        # Acciones de navegación reutilizables (transiciones reales)
└── ui/
    ├── theme/
    │   ├── Color.kt                # Paleta centralizada de ENVER + colores base de la plantilla
    │   ├── Theme.kt                # EnverTheme (Material 3)
    │   └── Type.kt                 # Tipografía (Material 3)
    └── screens/
        ├── welcome/
        │   └── WelcomeScreen.kt    # Splash visual con auto-navegación
        ├── auth/
        │   ├── LoginScreen.kt      # Inicio de sesión (visual)
        │   └── RegisterScreen.kt   # Registro de estudiante (visual)
        ├── onboarding/
        │   └── WelcomeSuccessScreen.kt  # Bienvenida post-registro (no es splash)
        └── home/
            └── HomeScreen.kt      # Contenido de la sección "Inicio"
        └── createorder/
            └── CreateOrderFlowScreen.kt   # Sección "Nuevo pedido" (4 pasos + validaciones + cámara)
    ├── components/
    │   ├── common/
    │   │   └── EnverGlobalHeader.kt  # Encabezado global reutilizable en todas las pantallas
    │   ├── navigation/
    │   │   ├── EnverBottomNavigationBar.kt  # Barra inferior reutilizable
    │   │   └── MainScaffold.kt   # Contenedor principal (header global + barra fija + secciones)
    │   └── media/
    │       ├── ImageSource.kt   # Modelo del origen de imagen (Local/Remote)
    │       └── EnverImage.kt    # Componente único para pintar imágenes

res/drawable-nodpi/               # Fotos estáticas de articulos de pedidos

docs/
└── *.md                            # Documentación técnica del proyecto

app/src/main/res/...                # Recursos Android existentes (sin cambios)
```

La estructura completa propuesta en la planificación también contempla
`ui/components/`, `data/`, `domain/` y `util/`. Por ahora **no se crean carpetas
vacías**: se incorporarán cuando exista un componente reutilizable real, un modelo
de dominio/DTO concreto o utilidades compartidas.

## Responsabilidad de cada paquete

| Paquete | Responsabilidad |
|---|---|
| raíz (`MainActivity`) | Punto de entrada Android. Aplica `EnverTheme` y delega toda la UI interna a `AppNavHost`. No contiene pantallas ni lógica de navegación. |
| `navigation` | Define rutas, transiciones y comportamiento del backstack. Clases de navegación **nunca** se mezclan con composables de pantalla. |
| `ui.theme` | Única fuente de verdad de colores/tema. Cualquier pantalla consume desde aquí; **no se redefinen colores locales**. |
| `ui.screens.welcome` | Pantalla de bienvenida (splash visual). Autonavega a Login tras un retardo breve y no queda en el backstack. |
| `ui.screens.auth` | Pantallas del flujo de cuenta (Login, Registro). Agrupadas por funcionalidad; ahí iría una futura `ForgotPasswordScreen` o `VerificationScreen`. |
| `ui.screens.onboarding` | Pantallas posteriores al registro y previas al flujo principal (`WelcomeSuccessScreen`). |
| `ui.screens.home` | Contenido de la sección "Inicio" (`HomeScreen` + modelo `OrderItem`). Se renderiza dentro de `MainScaffold`. |
| `ui.components.navigation` | Componentes de navegación compartidos: `MainScaffold` (header global + barra inferior fija + secciones por estado) y `EnverBottomNavigationBar` (una única instancia). |
| `ui.components.common` | Componentes visuales globales: `EnverGlobalHeader` — encabezado (rayo ⚡ a la izquierda; nombre, lema y campus centrados) mostrado únicamente por la estructura compartida (`MainScaffold`) de la sección principal (Home, Nuevo pedido, Pedidos, Perfil); nunca duplicar su implementación ni instanciarlo fuera de ella. La insignia "Nivel 4 - Rescatista" vive SOLO en el bloque de saludo de `HomeScreen`. |
| `ui.screens.createorder` | Flujo "Nuevo pedido": `CreateOrderFlowScreen` (4 pasos con estado interno) + `CreateOrderUiState` (datos conservados entre pasos). Paso 1 integrado; pasos 2–4 pendientes de referencia visual. Cámara real opcional con `FileProvider` + permiso `CAMERA`. |
| `ui.components.media` | Carga de imágenes: `ImageSource` (Local por recurso / Remote por URL) + `EnverImage`. Las pantallas nunca pintan imágenes directamente sino a través de `EnverImage`, de modo que migrar a contenido remoto (fotos de productos/perfiles desde servidor) solo implicará reescribir la rama `Remote` de `EnverImage` (p. ej. un loader asíncrono), sin tocar ningún composable. |

## Criterios de organización

1. Pantallas agrupadas por funcionalidad (`auth`, `welcome`, y luego `home`, `orders`, `profile`, etc.).
2. Navegación aislada en `com.example.enver.navigation`.
3. Colores centralizados en `ui.theme.Color.kt` (elimina duplicados tipo `EnverGreenReg`).
4. Pantallas desacopladas del `NavHostController`: solo exponen callbacks (`onRegisterClick`, etc.); es `AppNavHost` quien los conecta a acciones de navegación.
5. No se crean capas (`data`, `domain`) ni abstracciones sin propósito actual.

## Cómo agregar una pantalla nueva

1. Crear el composable en `ui/screens/<funcionalidad>/<Pantalla>Screen.kt`.
2. Si comparte colores/iconos, usar la paleta de `ui.theme` (nunca definir constantes locales duplicadas).
3. Definir la ruta en `navigation/NavRoutes.kt`.
4. Declara el destino `composable(...)` en `navigation/AppNavHost.kt`.
5. Si la pantalla necesita transiciones, agrega la acción en `NavigationActions.kt`.
6. La pantalla recibe callbacks (con valores por defecto); el cableado real ocurre en `AppNavHost`.

Ejemplo: para recuperar contraseña → `ui/screens/auth/ForgotPasswordScreen.kt`,
ruta `forgot_password`, y conectar `onForgotPasswordClick` al nuevo destino.
