package com.uttt.enverdelivery.app.navigation

import androidx.navigation.NavHostController

class NavigationActions(private val navController: NavHostController) {

    fun navigateToLogin() {
        navController.navigate(NavRoutes.LOGIN) {
            popUpTo(NavRoutes.WELCOME) { inclusive = true }
        }
    }

    fun navigateToRegister() {
        navController.navigate(NavRoutes.REGISTER)
    }

    fun returnToLogin() {
        navController.popBackStack(NavRoutes.LOGIN, inclusive = false)
    }

    fun navigateToRegisterSuccess() {
        navController.navigate(NavRoutes.REGISTER_SUCCESS)
    }

    // Punto de entrada principal de la app: limpia toda la pila de
    // autenticación (login/register/success/splash) y deja Home como raíz.
    fun navigateToHome() {
        navController.navigate(NavRoutes.HOME) {
            popUpTo(NavRoutes.LOGIN) { inclusive = true }
            launchSingleTop = true
        }
    }
}
