package com.chaintech.passwordmanager.ui.screen

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.chaintech.passwordmanager.R
import com.chaintech.passwordmanager.navigation.Screens
import com.chaintech.passwordmanager.ui.theme.C_ACCENT
import com.chaintech.passwordmanager.utils.BiometricLauncher
import com.chaintech.passwordmanager.utils.BiometricPromptManager

/**
 * This composable function represents the splash screen of the application.
 * It handles user interaction for unlocking the app through fingerprint authentication
 * and navigates to the home screen upon successful authentication.
 *
 * @param navController The NavController responsible for navigation within the app.
 */
@Composable
fun SplashScreen(navController: NavHostController) {

    Box(
        // Center the content of the Box horizontally and vertically
        contentAlignment = Alignment.Center,
        // Fill the entire available space of the parent layout
        modifier = Modifier.fillMaxSize()
    ) {

        // Get the current Activity context from LocalContext
        val context = LocalContext.current as AppCompatActivity

        // Create a BiometricPromptManager instance to handle fingerprint authentication
        val promptManager = remember {
            BiometricPromptManager(context)
        }

        // Launch the biometric prompt using the promptManager
        BiometricLauncher(promptManager) {
            // Navigate to the home screen on successful authentication
            navController.navigate(Screens.HOME_SCREEN) {
                // Clear the back stack up to (inclusive of) the splash screen
                popUpTo(Screens.SPLASH_SCREEN) {
                    inclusive = true
                }
            }
        }

        // Button to trigger fingerprint authentication prompt
        OutlinedButton(
            onClick = {
                promptManager.showBiometricPrompt(
                    context.getString(R.string.unlock_password_manager),
                    "" // Optional description for the prompt
                )
            },
            border = BorderStroke(color = Color.Black, width = 1.dp)
        ) {
            Text(
                text = stringResource(R.string.unlock_app),
                style = MaterialTheme.typography.titleMedium,
                color = C_ACCENT
            )
        }

    }
}

