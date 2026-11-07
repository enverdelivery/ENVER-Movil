package com.uttt.enverdelivery.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uttt.enverdelivery.app.ui.components.navigation.MainScaffold
import com.uttt.enverdelivery.app.ui.screens.auth.LoginScreen
import com.uttt.enverdelivery.app.ui.screens.auth.RegisterScreen
import com.uttt.enverdelivery.app.ui.screens.onboarding.WelcomeSuccessScreen
import com.uttt.enverdelivery.app.ui.screens.welcome.WelcomeScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navigationActions = remember(navController) { NavigationActions(navController) }

    NavHost(
        navController = navController,
        startDestination = NavRoutes.WELCOME,
        modifier = modifier
    ) {
        composable(NavRoutes.WELCOME) {
            WelcomeScreen(
                onNavigateToLogin = navigationActions::navigateToLogin
            )
        }
        composable(NavRoutes.LOGIN) {
            LoginScreen(
                onLoginClick = { _, _ ->
                    navigationActions.navigateToHome()
                },
                onRegisterClick = navigationActions::navigateToRegister,
                onForgotPasswordClick = {
                    // Preparado para la futura pantalla de recuperación de contraseña
                }
            )
        }
        composable(NavRoutes.REGISTER) {
            RegisterScreen(
                onBackClick = navigationActions::returnToLogin,
                onSubmitClick = navigationActions::navigateToRegisterSuccess,
                onLoginRedirectClick = navigationActions::returnToLogin
            )
        }
        composable(NavRoutes.REGISTER_SUCCESS) {
            WelcomeSuccessScreen(
                onGoHomeClick = navigationActions::navigateToHome
            )
        }
        composable(NavRoutes.HOME) {
            // Home y sus secciones comparten la barra inferior fija de MainScaffold
            MainScaffold()
        }
    }
}
