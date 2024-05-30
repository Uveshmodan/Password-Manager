package com.chaintech.passwordmanager.ui.sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chaintech.passwordmanager.R
import com.chaintech.passwordmanager.data.room.model.PasswordTable
import com.chaintech.passwordmanager.ui.theme.C_ACCENT
import com.chaintech.passwordmanager.ui.theme.C_B3B3B3
import com.chaintech.passwordmanager.ui.theme.C_BACKGROUND
import com.chaintech.passwordmanager.ui.theme.C_FF2A2A
import com.chaintech.passwordmanager.utils.EncryptionUtil

@ExperimentalMaterial3Api
@Composable
fun ShowPasswordSheet(
    password: PasswordTable, // PasswordTable object to display
    onDismiss: () -> Unit, // Callback function when the sheet is dismissed
    onEdit: () -> Unit, // Callback function when editing the password
    onDelete: () -> Unit // Callback function when deleting the password
) {

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
            // Title "Account Details"
            Text(
                text = stringResource(R.string.account_details),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = C_ACCENT
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Account Name
            Text(
                text = stringResource(R.string.account_name),
                style = MaterialTheme.typography.bodyMedium,
                color = C_B3B3B3,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = password.accountName,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = Color.Black,
                fontSize = 22.sp
            )

            // Spacer
            Spacer(modifier = Modifier.height(28.dp))

            // Username/Email
            Text(
                text = stringResource(R.string.username_email),
                style = MaterialTheme.typography.bodyMedium,
                color = C_B3B3B3,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = password.emailUname,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = Color.Black,
                fontSize = 22.sp
            )

            // Spacer
            Spacer(modifier = Modifier.height(28.dp))

            // Password
            Text(
                text = stringResource(R.string.password),
                style = MaterialTheme.typography.bodyMedium,
                color = C_B3B3B3,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(3.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                var isHidden by remember {
                    mutableStateOf(true)
                }

                val decryptedString = EncryptionUtil.decrypt(password.password)

                // Display password with option to show/hide
                Text(
                    text = if (isHidden) "∗∗∗∗∗∗∗∗" else decryptedString,
                    fontSize = 22.sp,
                    color = Color.Black
                )

                IconButton(onClick = { isHidden = !isHidden }) {
                    Icon(
                        painter = painterResource(id = if (isHidden) R.drawable.ic_hide else R.drawable.ic_visibility),
                        contentDescription = null
                    )
                }
            }

            // Spacer
            Spacer(modifier = Modifier.height(30.dp))

            // Edit and Delete Buttons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        onEdit() // Call edit callback
                        onDismiss() // Dismiss the bottom sheet
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    modifier = Modifier
                        .weight(1f)
                        .height(57.dp)
                ) {
                    Text(
                        text = stringResource(R.string.edit),
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.W600
                    )
                }

                Button(
                    onClick = {
                        onDelete() // Call delete callback
                        onDismiss() // Dismiss the bottom sheet
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = C_FF2A2A),
                    modifier = Modifier
                        .weight(1f)
                        .height(57.dp)
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.W600
                    )
                }
            }
        }

        // Spacer
        Spacer(modifier = Modifier.height(20.dp))
    }
}
