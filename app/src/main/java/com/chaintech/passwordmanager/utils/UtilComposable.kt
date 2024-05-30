package com.chaintech.passwordmanager.utils

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.chaintech.passwordmanager.R

@Composable
fun BiometricLauncher(promptManager: BiometricPromptManager, onSuccess: () -> Unit) {
    val context = LocalContext.current // Retrieve the current context

    // Collect biometric prompt results as state
    val biometricResult by promptManager.promptResults.collectAsState(
        initial = null
    )

    // Handle biometric prompt result using LaunchedEffect
    LaunchedEffect(biometricResult) {
        biometricResult?.let { result ->
            when (result) {
                is BiometricPromptManager.BiometricResult.AuthenticationError -> {
                    // Handle authentication error
                }
                BiometricPromptManager.BiometricResult.AuthenticationFailed -> {
                    // Handle authentication failure
                }
                BiometricPromptManager.BiometricResult.AuthenticationNotSet -> {
                    // Handle biometric authentication not set up
                }
                BiometricPromptManager.BiometricResult.AuthenticationSuccess -> {
                    onSuccess() // Call onSuccess callback on successful authentication
                }
                BiometricPromptManager.BiometricResult.FeatureUnavailable -> {
                    // Handle biometric feature unavailable
                }
                BiometricPromptManager.BiometricResult.HardwareUnavailable -> {
                    // Handle biometric hardware unavailable
                }
                BiometricPromptManager.BiometricResult.Null -> {
                    // Handle null result
                }
            }
        }
    }

    // Show biometric prompt using LaunchedEffect
    LaunchedEffect(Unit) {
        promptManager.showBiometricPrompt(
            title = context.getString(R.string.unlock_password_manager),
            description = ""
        )
    }
}

// Function to define custom colors for OutlinedTextField
@Composable
fun outlinedTextFieldCustomColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color.Black,
    focusedLabelColor = Color.Black,
    cursorColor = Color.Black
)
