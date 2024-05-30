package com.chaintech.passwordmanager.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chaintech.passwordmanager.ui.screen.HomeScreen
import com.chaintech.passwordmanager.ui.screen.SplashScreen
import com.chaintech.passwordmanager.viewModel.PasswordViewModel

/**
 * This composable function defines the root navigation graph for the application.
 * It utilizes Jetpack Navigation's NavHost component to manage navigation between screens.
 *
 * The function performs the following actions:
 *  - Creates a `NavController` instance for handling navigation throughout the app.
 *  - Retrieves a view model instance of `PasswordViewModel` using Hilt for dependency injection.
 *  - Defines a `NavHost` that manages the navigation flow:
 *      - `navController`: The NavController instance used for navigation.
 *      - `startDestination`: Sets the SplashScreen as the initial screen.
 *      - `route`: Defines the root route for the navigation graph (MainRoots.ROOT).
 *  - Composable functions are defined for each screen:
 *      - `Screens.SPLASH_SCREEN`: Renders the SplashScreen composable.
 *      - `Screens.HOME_SCREEN`: Renders the HomeScreen composable, passing the passwordViewModel as a parameter.
 */
@ExperimentalMaterial3Api
@Composable
fun RootNavGraph() {
    val navController = rememberNavController()

    val passwordViewModel = hiltViewModel<PasswordViewModel>()

    NavHost(
        navController = navController,
        startDestination = Screens.SPLASH_SCREEN,
        route = MainRoots.ROOT
    ) {
        composable(Screens.SPLASH_SCREEN) {
            SplashScreen(navController = navController)
        }

        composable(Screens.HOME_SCREEN) {
            HomeScreen(passwordViewModel = passwordViewModel)
        }
    }
}