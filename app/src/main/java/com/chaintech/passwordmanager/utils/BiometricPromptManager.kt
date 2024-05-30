package com.chaintech.passwordmanager.utils

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


class BiometricPromptManager(
    private val activity: AppCompatActivity // Activity where the biometric prompt will be displayed
) {

    // Channel to emit biometric prompt results
    private val resultChannel = Channel<BiometricResult>()

    // Flow of biometric prompt results
    val promptResults = resultChannel.receiveAsFlow()

    // Function to show the biometric prompt
    fun showBiometricPrompt(
        title: String, // Title of the biometric prompt
        description: String // Description of the biometric prompt
    ) {
        // Get BiometricManager instance
        val manager = BiometricManager.from(activity)

        // Determine authenticators based on Android version
        val authenticators = if(Build.VERSION.SDK_INT >= 30) {
            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
        } else BIOMETRIC_STRONG

        // Build BiometricPrompt.PromptInfo
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setDescription(description)
            .setAllowedAuthenticators(authenticators)

        // Set negative button text for versions below Android 30
        if(Build.VERSION.SDK_INT < 30) {
            promptInfo.setNegativeButtonText("Cancel")
        }

        // Handle various biometric authentication errors
        when(manager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                resultChannel.trySend(BiometricResult.HardwareUnavailable)
                return
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                resultChannel.trySend(BiometricResult.FeatureUnavailable)
                return
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                resultChannel.trySend(BiometricResult.AuthenticationNotSet)
                return
            }
            else -> Unit // No error
        }

        // Create and authenticate BiometricPrompt
        val prompt = BiometricPrompt(
            activity,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    resultChannel.trySend(BiometricResult.AuthenticationError(errString.toString()))
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    resultChannel.trySend(BiometricResult.AuthenticationSuccess)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    resultChannel.trySend(BiometricResult.AuthenticationFailed)
                }
            }
        )
        prompt.authenticate(promptInfo.build())
    }

    // Sealed interface representing different biometric prompt results
    sealed interface BiometricResult {
        data object HardwareUnavailable: BiometricResult // Hardware unavailable
        data object FeatureUnavailable: BiometricResult // Biometric feature unavailable
        data class AuthenticationError(val error: String): BiometricResult // Authentication error
        data object AuthenticationFailed: BiometricResult // Authentication failed
        data object AuthenticationSuccess: BiometricResult // Authentication succeeded
        data object AuthenticationNotSet: BiometricResult // Biometric authentication not set up
        data object Null: BiometricResult // Placeholder for null result
    }

}
