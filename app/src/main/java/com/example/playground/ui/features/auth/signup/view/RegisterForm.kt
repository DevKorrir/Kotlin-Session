package com.example.dlete.ui.features.auth.signup.presentation.view

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.playground.ui.features.auth.signup.componets.ReuseAbleTextField

@Composable
fun RegisterForm(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onPasswordVisibilityToggle: () -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    isConfirmPasswordVisible: Boolean,
    onConfirmPasswordVisibilityToggle: () -> Unit
) {
    ReuseAbleTextField(
        value = fullName,
        onValueChange = onFullNameChange,
        label = "Full Name",
        hint = "Aldo Kipyegon",
        leadingIcon = Icons.Default.EditNote,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next
        ),
        maxLines = 1,

    )

    Spacer(modifier = Modifier.height(16.dp))

    ReuseAbleTextField(
        value = email,
        onValueChange = onEmailChange,
        label = "Email",
        hint = "username123@gmail.com",
        leadingIcon = Icons.Default.MailOutline,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        maxLines = 1,
    )

    Spacer(modifier = Modifier.height(16.dp))

    ReuseAbleTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = "Password",
        leadingIcon = Icons.Default.LockOpen,
        isPassword = true,
        isPasswordVisible = isPasswordVisible,
        onPasswordVisibilityToggle = onPasswordVisibilityToggle,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),
    )

    Spacer(modifier = Modifier.height(16.dp))

    ReuseAbleTextField(
        value = confirmPassword,
        onValueChange = onConfirmPasswordChange,
        label = "Confirm Password",
        leadingIcon = Icons.Default.Lock,
        isPassword = true,
        isPasswordVisible = isConfirmPasswordVisible,
        onPasswordVisibilityToggle = onConfirmPasswordVisibilityToggle,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
    )
}