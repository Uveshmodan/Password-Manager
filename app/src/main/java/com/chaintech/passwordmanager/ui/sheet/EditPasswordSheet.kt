package com.chaintech.passwordmanager.ui.sheet

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chaintech.passwordmanager.R
import com.chaintech.passwordmanager.data.room.model.PasswordTable
import com.chaintech.passwordmanager.ui.theme.C_BACKGROUND
import com.chaintech.passwordmanager.utils.EncryptionUtil
import com.chaintech.passwordmanager.utils.outlinedTextFieldCustomColors

@ExperimentalMaterial3Api
@Composable
fun EditPasswordSheet(
    onDismiss: () -> Unit, // Callback function when the sheet is dismissed
    editPassword: PasswordTable, // PasswordTable object to edit
    onEdit: (PasswordTable) -> Unit // Callback function when editing the password
) {
    val context = LocalContext.current // Get the current context

    // Mutable state for the input fields
    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    // LaunchedEffect to set initial values when the composable is launched
    LaunchedEffect(Unit) {
        name = editPassword.accountName // Set account name
        email = editPassword.emailUname // Set email/username
        val decryptedString = EncryptionUtil.decrypt(editPassword.password) // Decrypt password
        password = decryptedString // Set decrypted password
    }

    // Validation rules for the input fields
    val validations = listOf(
        name.isNotEmpty(),
        email.isNotEmpty(),
        password.length >= 8
    )

    // Function to validate the password and return an error message if any
    fun validatePassword(): String {
        if (!Regex("[0-9]").containsMatchIn(password)) {
            return "Password must contain at least one number"
        }

        if (!Regex("[A-Z]").containsMatchIn(password)) {
            return "Password must contain at least one uppercase letter"
        }

        if (!Regex("[a-z]").containsMatchIn(password)) {
            return "Password must contain at least one lowercase letter"
        }

        return ""
    }


    // Bottom modal sheet
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = C_BACKGROUND,
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        onDismissRequest = onDismiss // Dismiss callback
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {

            // Input field for account name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = {
                    Text(text = stringResource(id = R.string.account_name))
                },
                textStyle = TextStyle(fontSize = 18.sp),
                singleLine = true,
                colors = outlinedTextFieldCustomColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Input field for email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = {
                    Text(text = stringResource(id = R.string.username_email))
                },
                textStyle = TextStyle(fontSize = 18.sp),
                singleLine = true,
                colors = outlinedTextFieldCustomColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Input field for password
            var passwordVisible by remember { mutableStateOf(false) }

            var isErrorPassword by remember {
                mutableStateOf(false)
            }
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = {
                    Text(text = stringResource(id = R.string.password))
                },
                isError = isErrorPassword,
                textStyle = TextStyle(fontSize = 18.sp),
                singleLine = true,
                colors = outlinedTextFieldCustomColors(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image =
                        if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_hide
                    IconButton(
                        onClick = {
                            passwordVisible = !passwordVisible
                        }
                    ) {
                        Icon(painter = painterResource(image), contentDescription = null)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Button to apply the edit
            Button(
                onClick = {
                    val error = validatePassword()

                    if (error.isEmpty()) {
                        val data =
                            PasswordTable(id = editPassword.id, accountName = name, emailUname = email, password = password)
                        onEdit(data) // Call the edit callback with the updated password data
                        onDismiss() // Dismiss the bottom sheet
                    } else {
                        isErrorPassword = true
                        Toast.makeText(context, error, Toast.LENGTH_LONG).show() // Show error message
                    }
                },
                enabled = validations.all { it }, // Enable button only if all validations pass
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(57.dp)
            ) {
                Text(
                    text = stringResource(R.string.apply_edit),
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.W600
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp)) // Spacer for layout
    }
}
