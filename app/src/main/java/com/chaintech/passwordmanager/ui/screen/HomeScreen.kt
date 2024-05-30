package com.chaintech.passwordmanager.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chaintech.passwordmanager.R
import com.chaintech.passwordmanager.data.room.model.PasswordTable
import com.chaintech.passwordmanager.ui.sheet.EditPasswordSheet
import com.chaintech.passwordmanager.ui.sheet.InsertPasswordSheet
import com.chaintech.passwordmanager.ui.sheet.ShowPasswordSheet
import com.chaintech.passwordmanager.ui.theme.C_ACCENT
import com.chaintech.passwordmanager.ui.theme.C_BACKGROUND
import com.chaintech.passwordmanager.ui.theme.C_DIVIDER
import com.chaintech.passwordmanager.viewModel.PasswordViewModel


@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    passwordViewModel: PasswordViewModel
) {
    // State variables for managing sheet visibility and data
    var isInsertPasswordSheet by remember { mutableStateOf(false) }
    var isShowPasswordSheet by remember { mutableStateOf(false to PasswordTable()) }
    var isEditPasswordSheet by remember { mutableStateOf(false to PasswordTable()) }

    // Retrieve password list from ViewModel
    val passwordList by passwordViewModel.readAllPassword().collectAsState(initial = emptyList())

    // Scaffold with TopAppBar and FloatingActionButton
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.password_manager),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = C_BACKGROUND)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isInsertPasswordSheet = true },
                containerColor = C_ACCENT
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        containerColor = C_BACKGROUND
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            // Divider between TopAppBar and password list
            HorizontalDivider(color = C_DIVIDER)

            // LazyColumn to display password list
            LazyColumn(
                contentPadding = PaddingValues(20.dp)
            ) {
                items(
                    count = passwordList.size,
                    key = { index -> passwordList[index].id }
                ) { position ->
                    ListItem(
                        title = passwordList[position].accountName,
                        onClick = {
                            isShowPasswordSheet = true to passwordList[position]
                        }
                    )
                }
            }
        }
    }

    // InsertPasswordSheet composable
    if (isInsertPasswordSheet) {
        InsertPasswordSheet(
            onInsert = {
                passwordViewModel.insertPassword(it)
            },
            onDismiss = {
                isInsertPasswordSheet = false
            }
        )
    }

    // ShowPasswordSheet composable
    if (isShowPasswordSheet.first) {
        ShowPasswordSheet(
            password = isShowPasswordSheet.second,
            onDismiss = {
                isShowPasswordSheet = false to PasswordTable()
            },
            onEdit = {
                isEditPasswordSheet = true to isShowPasswordSheet.second
            },
            onDelete = {
                passwordViewModel.deletePassword(isShowPasswordSheet.second.id)
            }
        )
    }

    // EditPasswordSheet composable
    if (isEditPasswordSheet.first) {
        EditPasswordSheet(
            editPassword = isEditPasswordSheet.second,
            onDismiss = {
                isEditPasswordSheet = false to PasswordTable()
            },
            onEdit = {
                passwordViewModel.editPassword(it)
            }
        )
    }
}

// ListItem composable for displaying individual password items
@Composable
fun ListItem(title: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .border(
                width = 0.2.dp,
                shape = CircleShape,
                color = C_DIVIDER
            )
            .background(color = Color.White, shape = CircleShape)
            .clickable {
                onClick()
            }
            .padding(start = 20.dp, end = 10.dp)
    ) {
        // Account name
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.W500),
            fontSize = 19.sp
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Masked password
        Text(
            text = "∗∗∗∗∗∗∗∗",
            fontSize = 24.sp,
            color = Color.LightGray
        )

        Spacer(modifier = Modifier.weight(1f))

        // Arrow icon
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.LightGray.copy(0.7f),
            modifier = Modifier
                .size(28.dp)
        )
    }

    Spacer(modifier = Modifier.height(16.dp)) // Spacer for layout
}
